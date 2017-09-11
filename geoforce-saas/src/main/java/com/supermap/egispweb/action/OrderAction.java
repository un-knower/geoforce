package com.supermap.egispweb.action;

import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springside.modules.mapper.JsonMapper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.supermap.egisp.addressmatch.beans.DefaultAddressMatchResult;
import com.supermap.egisp.addressmatch.beans.POIAddressMatchParam;
import com.supermap.egisp.addressmatch.beans.ReverseAddressMatchResult;
import com.supermap.egisp.addressmatch.service.IAddressMatchService;
import com.supermap.egispservice.base.entity.AddressEntity;
import com.supermap.egispservice.base.entity.OrderBaseEntity;
import com.supermap.egispservice.base.entity.OrderFendanEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.pojo.LogisticsResultInfo;
import com.supermap.egispservice.base.service.ICorrectAddressService;
import com.supermap.egispservice.base.service.ILogisticsFendanService;
import com.supermap.egispservice.base.service.ILogisticsOrderService;
import com.supermap.egispservice.base.service.ILogisticsService;
import com.supermap.egispservice.geocoding.service.IGeocodingService;
import com.supermap.egispweb.pojo.order.OrderBean;
import com.supermap.egispweb.util.ExcelUtil;
import com.supermap.egispweb.util.FieldMap;
import com.supermap.egispweb.util.StringUtil;

@Controller
@RequestMapping("orderService")
@SessionAttributes(types = { UserEntity.class }, value = { "user" })
public class OrderAction {
	
	private final static Logger LOGGER = Logger.getLogger(OrderAction.class);

	private static final String CHARSET = ";charset=UTF-8";
	
	@Autowired
	private ILogisticsOrderService orderService;
	
	@Autowired
	private ILogisticsService logisticsService;
	
	@Autowired
	private IAddressMatchService addressMatch;
	
	@Autowired
	private ILogisticsFendanService fendanService;

	@Autowired
	private ICorrectAddressService correctAddressService;
	
	@Autowired
	private IGeocodingService geocodingService;
	
	
	@RequestMapping("upload")
	@ResponseBody
	public void uploadOrder(MultipartFile myfile, HttpServletResponse response,MultipartHttpServletRequest request,
			HttpSession session) throws Exception{
		UserEntity user=(UserEntity)session.getAttribute("user");
		//response.setContentType("multipart/form-data");
		myfile=(MultipartFile) request.getFile("myFile");
		response.setContentType("text/plain;charset=UTF-8");
		Map<String,Object> map = null;
		try {
			if(null == user){
				throw new Exception("用户未登录");
			}
			String userId = user.getId();
			String enterpriseId = user.getEid().getId();
			String departmentId = user.getDeptId().getId();
			if(myfile.isEmpty()){  
				map = buildResult("文件为空", null, false);
			}else{  
				// 解析文件
				String fileName = myfile.getOriginalFilename();
				InputStream is = myfile.getInputStream();
				LOGGER.debug("## 开始解析Excell["+fileName+"]");
				List<OrderBean> orderBean = ExcelUtil.readOrderExcel(is, fileName);
				if(null == orderBean || orderBean.size() <= 0){
					throw new Exception("解析文件["+fileName+"]失败");
				}
				// 入库文件
				List<OrderBaseEntity> orderEntitys = buildOrders(orderBean, userId, enterpriseId, departmentId);
				LOGGER.debug("## 开始入库订单信息[count="+(orderBean.size())+"]");
				List<String> orderIds = orderService.saveOrderInfos(orderEntitys);
				// 触发分单
				if(null == orderIds || orderIds.size() <= 0){
					throw new Exception("导入订单为空");
				}
				LOGGER.debug("## 开始自动分单。。。");
				logisticsService.logistic(orderIds, userId, enterpriseId, departmentId);
				Map<String,Object> queryResult = null;
				if(orderEntitys != null && orderEntitys.size() > 0){
					 queryResult = orderService.queryByBatch( userId, enterpriseId, departmentId,orderEntitys.get(0).getBatch(), 1, 10);
				}
				// 返回结果
				map = buildResult(null,queryResult, true);
			}  
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			map = buildResult(e.getMessage(), null, false);
		}
		
		ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        String json = mapper.writeValueAsString(map);
        PrintWriter out=response.getWriter();
        out.write(json);
        out.close();
	}
	
	/**
	 * 
	 * <p>Title ：logistics</p>
	 * Description：	单条地址分单
	 * @param address
	 * @param session
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-10-15 下午01:57:50
	 */
	@RequestMapping("logistic")
	@ResponseBody
	public Map<String, Object> logistics(@RequestParam(required = true) String address, HttpSession session) {
		Map<String,Object> map = null;
		try {
		
			OrderBean ob = new OrderBean(null,address);
			List<OrderBean> obs = new ArrayList<OrderBean>();
			obs.add(ob);
			UserEntity user=(UserEntity)session.getAttribute("user");
			String userId = user.getId();
			String enterpriseId = user.getEid().getId();
			String departmentId = user.getDeptId().getId();
			List<OrderBaseEntity> obes = buildOrders(obs, userId, enterpriseId, departmentId);
			List<String> ids = this.orderService.saveOrderInfos(obes);
			if(null != ids && ids.size() > 0){
				LogisticsResultInfo li = this.logisticsService.logistic(ids.get(0),userId, enterpriseId, departmentId);
				map = buildResult(null, li, true);
			}else{
				map = buildResult("save the address failed",null,false);
			}
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			map = buildResult(e.getMessage(), null, false);
		}
		
		return map;
	}
	
	
	/**
	 * 
	 * <p>Title ：queryByBatch</p>
	 * Description：		按批次查询分单信息
	 * @param batch
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-25 上午09:36:04
	 */
	@RequestMapping("batchQuery")
	@ResponseBody
	public Map<String, Object> queryByBatch(String batch, String pageNo, String pageSize
			,HttpSession session) {
		UserEntity user=(UserEntity)session.getAttribute("user");
		int pageNoi = 1;
		int pageSizei = 10;
		Map<String, Object> map = null;
		try {
			if(null == user){
				throw new Exception("用户未登录");
			}
			String userId = user.getId();
			String enterpriseId = user.getEid().getId();
			String dcode = user.getDeptId().getCode();
			if (!StringUtils.isEmpty(pageNo)) {
				pageNoi = Integer.parseInt(pageNo);
				if (pageNoi < 1) {
					pageNoi = 1;
				}
			}
			if (!StringUtils.isEmpty(pageSize) && pageNoi != -1) {
				pageSizei = Integer.parseInt(pageSize);
				if (pageSizei > 20) {
					pageSizei = 20;
				}
			}

			Map<String, Object> resultObj = this.orderService.queryByBatch(userId, enterpriseId, dcode, batch,
					pageNoi, pageSizei);
			map = buildResult(null, resultObj, true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			map = buildResult(e.getMessage(), null, false);
		}
		return map;
	}
	
	/**
	 * 
	 * <p>Title ：addressMatch</p>
	 * Description：		地址匹配
	 * @param orderNum
	 * @param address
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-25 上午09:38:58
	 */
	@RequestMapping("addressMatch")
	@ResponseBody
	public Map<String, Object> addressMatch(@RequestParam(required = true) String orderNum,
			@RequestParam(required = true) String address,HttpSession session) {
		UserEntity user=(UserEntity)session.getAttribute("user");
		// 入库
		Map<String, Object> map = null;
		try {
			if(null == user){
				throw new Exception("用户未登录");
			}
			OrderBaseEntity obe = new OrderBaseEntity();
			obe.setAddress(address);
			obe.setNumber(orderNum);
			String batch = sdf.format(new Date());
			obe.setBatch(batch);
			obe.setUserId(user.getId());
			obe.setEnterpriseId(user.getEid().getId());
			obe.setDepartmentId(user.getDeptId().getId());
			obe.setDeleteFlag(Byte.valueOf("0"));
			obe.setImportTime(new Date());
			obe.setOrderStatusId(Byte.valueOf("3"));
			List<OrderBaseEntity> list = new ArrayList<OrderBaseEntity>();
			list.add(obe);
			List<String> orderIds = this.orderService.saveOrderInfos(list);
			if (null == orderIds || orderIds.size() <= 0) {
				throw new Exception("保存地址信息有误");
			}
			String orderId = orderIds.get(0);
			DefaultAddressMatchResult amr = (DefaultAddressMatchResult) this.addressMatch.addressMatch(orderId, address, "SMC");
			if (null != amr) {
				OrderBaseEntity orderEntity = new OrderBaseEntity();
				orderEntity.setId(orderId);
				orderEntity.setProvince(amr.getProvince());
				orderEntity.setCity(amr.getCity());
				orderEntity.setCounty(amr.getCounty());
				orderEntity.setAdmincode(amr.getAdmincode());
				list.clear();
				list.add(orderEntity);
			}
			this.orderService.update(list);
			map = buildResult(null, amr, true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			map = buildResult(e.getMessage(), null, false);
		}
		return map;
	}
	
	
	@RequestMapping(value="searchForCounty",produces=MediaType.APPLICATION_JSON_VALUE + CHARSET)
	@ResponseBody
	public Object searchForCounty(String callbacks,@RequestParam(required = true) String smx,
			@RequestParam(required = true) String smy,HttpServletResponse resp) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			if(StringUtil.isStringEmpty(smx) || StringUtil.isStringEmpty(smy)){
				throw new Exception( "smx or smy is not allowed be empty!");
			}
			double x = Double.parseDouble(smx);
			double y = Double.parseDouble(smy);
//			Map<String,String> result = this.addressMatch.searchForCounty(x, y);
			Map<String,String> result = geocodingService.searchForCounty(x, y);
			resultMap = buildResult(null, result, true);
		} catch (Exception e) {
			resultMap = buildResult(e.getMessage(), null, false);
		}
		if(!StringUtils.isEmpty(callbacks)){
			JsonMapper jm = new JsonMapper();
			String result = jm.toJsonP(callbacks, resultMap); 
			return result;
		}else{
			return resultMap;
		}
	}
	
	
	@RequestMapping(value="searchForCountyByCode",produces=MediaType.APPLICATION_JSON_VALUE + CHARSET)
	@ResponseBody
	public Object searchForCountyByCode(String callbacks,@RequestParam(required = true) String code,
			HttpServletResponse resp) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			List<Map<String,String>> result = geocodingService.searchCountyByAdmincode(code);
			resultMap = buildResult(null, result, true);
		} catch (Exception e) {
			resultMap = buildResult(e.getMessage(), null, false);
		}
		if(!StringUtils.isEmpty(callbacks)){
			JsonMapper jm = new JsonMapper();
			String result = jm.toJsonP(callbacks, resultMap); 
			return result;
		}else{
			return resultMap;
		}
	}
	
	@RequestMapping(value="getAdminByCode",produces=MediaType.APPLICATION_JSON_VALUE + CHARSET)
	@ResponseBody
	public Object getAdminByCode(String callbacks,@RequestParam(required = true,value="admincode") String code,
			HttpServletResponse resp) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try{
			Map<String,Object> result = geocodingService.getCountyByAdmincode(code);
			resultMap = buildResult(null, result, true);
		} catch (Exception e) {
			resultMap = buildResult(e.getMessage(), null, false);
		}
		if(!StringUtils.isEmpty(callbacks)){
			JsonMapper jm = new JsonMapper();
			String result = jm.toJsonP(callbacks, resultMap); 
			return result;
		}else{
			return resultMap;
		}
	}
	
	/**
	 * 
	 * <p>Title ：searchForAdminGeo</p>
	 * Description：		根据行政区划代码和级别查询行政区划集合对象属性
	 * @param callbacks
	 * @param code
	 * @param levelStr
	 * @param resp
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-7-29 下午02:49:13
	 */
	@RequestMapping(value="searchForAdminGeo",produces=MediaType.APPLICATION_JSON_VALUE + CHARSET)
	@ResponseBody
	public Object searchForAdminGeo(String callbacks, @RequestParam(required = true,value="admincode") String code,
			@RequestParam(required = true, value = "level") String levelStr, HttpServletResponse resp) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		try {
			int level = Integer.parseInt(levelStr);
			Map<String, Object> result = geocodingService.getAdminGeoByCode(code, level);
			resultMap = buildResult(null, result, true);
		} catch (Exception e) {
			resultMap = buildResult(e.getMessage(), null, false);
		}
		if (!StringUtils.isEmpty(callbacks)) {
			JsonMapper jm = new JsonMapper();
			String result = jm.toJsonP(callbacks, resultMap);
			return result;
		} else {
			return resultMap;
		}
	}
	
	
	
	/**
	 * 
	 * <p>Title ：getAdminElements</p>
	 * Description：	获取行政级别元素
	 * @param admincode
	 * @param levelStr
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-6-29 上午11:39:03
	 */
	@RequestMapping(value="getAdminElements",produces=MediaType.APPLICATION_JSON_VALUE + CHARSET)
	@ResponseBody
	public Object getAdminElements(@RequestParam(required = true) String admincode,
			@RequestParam(required = true, value = "level") String levelStr,String callbacks) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		try {
			LOGGER.info("## 查询行政元素参数[admincode="+admincode+"],[level="+levelStr+"]");
			int level =  2;
			try {
				level = Integer.parseInt(levelStr);
			} catch (Exception e) {
				level = 2;
			}
			List<Map<String,Object>> list = this.geocodingService.getAdminElement(admincode, level);
			if(null != list && list.size() > 0){
				resultMap = buildResult(null, list, true);
			}else{
				resultMap = buildResult("查询失败", null, false);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultMap = buildResult(e.getMessage(), e, false);
		}
		if(!StringUtils.isEmpty(callbacks)){
			JsonMapper jm = new JsonMapper();
			String result = jm.toJsonP(callbacks, resultMap); 
			return result;
		}else{
			return resultMap;
		}
	}
	
	
	@RequestMapping("move")
	@ResponseBody
	public Map<String, Object> updateFendan(@RequestParam(required = true) String id,
			@RequestParam(required = true) String smx, @RequestParam(required = true) String smy) {
		Map<String,Object> map = null;
		try {
			double smxD = Double.parseDouble(smx);
			double smyD = Double.parseDouble(smy);
			OrderFendanEntity ofe = new OrderFendanEntity();
			ofe.setId(id);
			ofe.setSmx(BigDecimal.valueOf(smxD));
			ofe.setSmy(BigDecimal.valueOf(smyD));
			fendanService.update(ofe);
			map = buildResult("移动分单点成功", null, true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			map = buildResult(e.getMessage(), null, false);
		}
		return map;
	}
	
	
	@RequestMapping("logisticsService")
	@ResponseBody
	public Map<String, Object> logisticsService(@RequestParam(required = true) String id,
			@RequestParam(required = true) String smx, @RequestParam(required = true) String smy) {
		Map<String, Object> map = null;
		try {
			double smxD = Double.parseDouble(smx);
			double smyD = Double.parseDouble(smy);
			this.logisticsService.logistics(id, smxD, smyD);
			map = buildResult("操作成功", null, true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			map = buildResult(e.getMessage(), null, false);
		}
		return map;
	}
	
	@RequestMapping("reverseMatch")
	@ResponseBody
	public Map<String, Object> reverseMatch(@RequestParam(required = true) String smx,
			@RequestParam(required = true) String smy,String admincode,String range) {
		Map<String,Object> map = null;
		try {
			double smxD = Double.parseDouble(smx);
			double smyD = Double.parseDouble(smy);
			int admincodeI = 0; 
			if(!StringUtils.isEmpty(admincode)){
				admincodeI = Integer.parseInt(admincode);
			}
			double rangeD = 500d;
			if(!StringUtils.isEmpty(range)){
				rangeD = Double.parseDouble(range);
				if(rangeD < 500d){
					rangeD = 500d;
				}
				if(rangeD > 3000d){
					rangeD = 3000d;
				}
			}
			
			ReverseAddressMatchResult rmr = (ReverseAddressMatchResult) this.addressMatch.addressMatchByCoor(smxD, smyD, admincodeI, rangeD);
			
			//坐标查询省市区
			Map<String,String> result = geocodingService.searchAdmincodeForCounty(smxD, smyD);
			
			if(null == rmr){
				//throw new Exception("未找到匹配结果");
				Map<String,String> m=new HashMap<String,String>();
				if(result!=null){
					m.put("admincode", result.get("ADMINCODE"));
					m.put("province", result.get("PROVINCE"));
					m.put("city", result.get("CITY2"));
					m.put("county", result.get("COUNTY"));
				}
				map = buildResult("未找到匹配结果", m, false);
			}
			else{
				Map<String,Object> rmrmap=new FieldMap().convertBean(rmr);
				if(result!=null){
					rmrmap.put("admincode", result.get("ADMINCODE"));
				}else rmrmap.put("admincode", "");
				map = buildResult(null, rmrmap, true);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			map = buildResult(e.getMessage(), null, false);
		}
		return map;
	}

	
	
	/**
	 * 
	 * <p>Title ：buildOrders</p>
	 * Description：		根据导入文件结果构建订单信息
	 * @param beas
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-25 上午09:36:34
	 */
	private List<OrderBaseEntity> buildOrders(List<OrderBean> beas,String userId,String enterpriseId,String departmentId){
		List<OrderBaseEntity> orderBaseEntity = new ArrayList<OrderBaseEntity>();
		for (int i = 0; i < beas.size(); i++) {
			OrderBean bean = beas.get(i);
			OrderBaseEntity baseEntity = new OrderBaseEntity();
			baseEntity.setNumber(bean.getNumber());
			baseEntity.setAddress(bean.getAddress());
			String batch = sdf.format(new Date());
			baseEntity.setBatch(batch);
			baseEntity.setImportTime(new Date());
			baseEntity.setDeleteFlag(Byte.valueOf("0"));
			baseEntity.setUserId(userId);
			baseEntity.setEnterpriseId(enterpriseId);
			baseEntity.setDepartmentId(departmentId);
			baseEntity.setOrderStatusId(Byte.valueOf("1"));
			baseEntity.setStartTime(bean.getStartTime());
			baseEntity.setEndTime(bean.getEndTime());
			orderBaseEntity.add(baseEntity);
		}
		return orderBaseEntity;
	}
	
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	
	/**
	 * 
	 * <p>Title ：buildResult</p>
	 * Description：		构建返回结果对象
	 * @param info
	 * @param result
	 * @param isSuccess
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-16 上午09:58:51
	 */
	private Map<String,Object> buildResult(String info,Object result,boolean isSuccess){
		Map<String,Object> resultObj = new HashMap<String,Object>();
		resultObj.put("info", info);
		resultObj.put("result", result);
		resultObj.put("isSuccess", isSuccess);
		return resultObj;
	}
	
	/**
	 * 
	 * <p>Title ：queryByArea</p>
	 * Description：根据区域ID查询分单成功的订单
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-10-28 下午03:55:59
	 */
	@RequestMapping("queryByArea")
	@ResponseBody
	public Map<String, Object> queryByArea(@RequestParam(required = true) String areaId,
			@RequestParam(required = true) String lot, String page, String rows,HttpSession session) {
		UserEntity user=(UserEntity)session.getAttribute("user");
		Map<String, Object> result = null;
		try {
			int pageNo = 1;
			int rowSize = 10;
			if(!StringUtils.isEmpty(page)){
				pageNo = Integer.parseInt(page);
			}
			if(!StringUtils.isEmpty(rows)){
				rowSize = Integer.parseInt(rows);
			}
			pageNo = pageNo <= 0 ? 1 : pageNo;
			rowSize = (rowSize <= 0 || rowSize > 50) ? 10 : rowSize;
			result = this.fendanService.queryByBatchAndArea(user.getId(), areaId, lot, pageNo, rowSize);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			result = new HashMap<String,Object>();
			result.put("total", 0);
			result.put("page", page);
			result.put("rows", rows);
			result.put("records", 0);
		}
		return result;
	}
	
	/**
	 * 
	 * <p>Title ：queryById</p>
	 * Description：		通过订单ID进行查询
	 * @param orderId
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-11-5 上午11:15:19
	 */
	@RequestMapping("queryById")
	@ResponseBody
	public Map<String,Object> queryById(@RequestParam(required=true)String orderId,HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String,Object> resultMap = null;
		try {
			resultMap = this.orderService.queryById(orderId, user.getId());
			if(null == resultMap){
				resultMap = new HashMap<String,Object>();
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultMap = new HashMap<String,Object>();
		}
		return resultMap;
	}
	
	/**
	 * 
	 * <p>Title ：poiSearch</p>
	 * Description：		POI关键词搜索
	 * @param parameter
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-11-18 下午03:00:56
	 */
	@RequestMapping("poiSearch")
	@ResponseBody
	public Map<String,Object> poiSearch(@RequestParam(required=true) String parameter){
		Map<String,Object> resultMap = null;
		try{
			if(StringUtil.isStringEmpty(parameter)){
				throw new Exception("查询参数为空");
			}
			LOGGER.info("## 查询参数："+parameter);
			JsonParser parser = new JsonParser();
			JsonObject jsonObject = parser.parse(parameter).getAsJsonObject();
//			JsonObject jsonObject = objMapper.convertValue(parameter, JsonObject.class);
			JsonElement startEle = jsonObject.get("startRecord");
			int startRecord = 0;
			// 解析开始记录数
			if(null != startEle){
				startRecord = startEle.getAsInt();
				if(startRecord < 0){
					startRecord = 0;
				}
			}
			// 解析期望返回的结果数
			int expectCount = 10;
			JsonElement expectEle = jsonObject.get("expectCount");
			if(null != expectEle){
				expectCount = expectEle.getAsInt();
				if(expectCount < 0 || expectCount > 50){
					expectCount = 10;
				}
			}
			// 解析查询过滤条件
			String filter = jsonObject.get("filter").getAsString();
			// 解析需要待返回的字段
			JsonElement fieldsEle = jsonObject.get("returnFields");
			List<String> returnFields = new ArrayList<String>();
			if(null != fieldsEle){
				JsonArray array = fieldsEle.getAsJsonArray();
				for(JsonElement element : array){
					returnFields.add(element.getAsString());
				}
			}
			POIAddressMatchParam pmp = new POIAddressMatchParam();
			pmp.setFilter(filter);
			pmp.setReturnFields(returnFields);
			pmp.setStartRecord(startRecord);
			pmp.setExpectCount(expectCount);
			resultMap = this.addressMatch.poiSearch(pmp);
		}catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultMap = new HashMap<String,Object>();
		}
		return resultMap;
	}
	
	/**
	 * 
	 * <p>Title ：addressMatchAndStore</p>
	 * Description：		地址解析
	 * @param address
	 * @param session
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-5-14 下午02:46:09
	 */
	@RequestMapping("addressMatchAndStore")
	@ResponseBody
	public Map<String, Object> addressMatchAndStore(@RequestParam(required = true) String address, HttpSession session) {
		Map<String,Object> resultMap = null;
		try {
			UserEntity user=(UserEntity)session.getAttribute("user");
			if(null == user){
				throw new Exception("用户信息不允许为空");
			}
			String userId = user.getId();
			String eid = user.getEid().getId();
			String dcode = user.getDeptId().getCode();
			AddressEntity ae = this.logisticsService.addrssMatchAndSave(address, userId, eid, dcode);
			if(null == ae){
				throw new Exception("存储地址出错");
			}
			resultMap = this.buildResult(null, ae, true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultMap = buildResult(e.getMessage(), null, false);
		}
		return resultMap;
	}
	

	/**
	 * 
	 * <p>Title ：correctAddress</p>
	 * Description：	添加纠错地址
	 * @param address
	 * @param desc
	 * @param session
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-9-29 上午10:48:16
	 */
	@RequestMapping("correct_add")
	@ResponseBody
	public Map<String, Object> correctAdd(@RequestParam(required = true) String address,String desc, HttpSession session) {
		Map<String,Object> resultMap = null;
		try {
			UserEntity user=(UserEntity)session.getAttribute("user");
			if(null == user){
				throw new Exception("用户信息不允许为空");
			}
			String userId = user.getId();
			String eid = user.getEid().getId();
			String dcode = user.getDeptId().getCode();
			
			//判断地址是否已存在,已存在则不允许用户添加
			boolean isExsit=this.correctAddressService.exsitAddress(address, eid);
			if(isExsit){
				resultMap = buildResult("地址已存在", null, false);
				return resultMap;
			}
			// 调用添加纠错地址服务接口
			boolean success = this.correctAddressService.addCorrectAddress(address, desc, userId, eid, dcode);
			// 组装结果
			resultMap = this.buildResult("添加纠错地址"+(success?"成功":"失败"), null, success);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultMap = buildResult(e.getMessage(), null, false);
		}
		return resultMap;
	}
	
	/**
	 * 
	 * <p>Title ：queryCorrectAddress</p>
	 * Description：	查询纠错地址列表
	 * @param keyword
	 * @param statusStr
	 * @param pageNoStr
	 * @param pageSizeStr
	 * @param session
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-10-9 上午11:38:42
	 */
	@RequestMapping("correct_query")
	@ResponseBody
	public Map<String, Object> queryCorrectAddress(String keyword,
			@RequestParam(defaultValue = "2") int status,
			@RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "20") int pageSize,HttpSession session) {
		Map<String, Object> resultMap = null;
		try {
			
			if(status != 0 && status != 1 && status != 2){
				status = 2;
			}
			if(pageNo <= 0){
				pageNo = 1;
			}
			if(pageSize <= 0 || pageSize > 100){
				pageSize = 20;
			}
			UserEntity user=(UserEntity)session.getAttribute("user");
			if(null == user){
				throw new Exception("用户信息不允许为空");
			}
//			String userId = user.getId();
			String eid = user.getEid().getId();
//			String dcode = user.getDeptId().getCode();
			Map<String,Object> map = this.correctAddressService.query(keyword, status, pageNo, pageSize, null, eid, null);
			resultMap = buildResult(null, map, true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultMap = buildResult(e.getMessage(), null, false);
		}
		return resultMap;
	}
	
	/**
	 * 
	 * <p>Title ：correctAddressMove</p>
	 * Description：	移动纠错地址位置
	 * @param x
	 * @param y
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-10-9 下午01:56:35
	 */
	@RequestMapping("correct_move")
	@ResponseBody
	public Map<String, Object> correctAddressMove(@RequestParam(required = true) double x,
			@RequestParam(required = true) double y, @RequestParam(required = true) String id,HttpSession session) {
		Map<String, Object> resultMap = null;
		try {
			UserEntity user=(UserEntity)session.getAttribute("user");
			if(null == user){
				throw new Exception("用户信息不允许为空");
			}
			String userId = user.getId();
			String eid = user.getEid().getId();
			String dcode = user.getDeptId().getCode();
			boolean success = this.correctAddressService.moveCorrectAddress(id, x, y, userId, eid, dcode);
			resultMap = buildResult("移动纠错地址"+(success?"成功":"失败"), null, success);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultMap = buildResult(e.getMessage(), null, false);
		}
		return resultMap;
	}
	
	
	/**
	 * 
	 * <p>Title ：correctAddressDel</p>
	 * Description：	删除纠错地址
	 * @param id
	 * @param session
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-10-9 下午04:11:48
	 */
	@RequestMapping("correct_del")
	@ResponseBody
	public Map<String,Object> correctAddressDel(@RequestParam(required=true)String id,HttpSession session){
		Map<String, Object> resultMap = null;
		try {
			UserEntity user=(UserEntity)session.getAttribute("user");
			if(null == user){
				throw new Exception("用户信息不允许为空");
			}
			String userId = user.getId();
			boolean success = this.correctAddressService.delCorrectAddress(id,userId);
			resultMap = buildResult("删除纠错地址"+(success?"成功":"失败"), null, success);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultMap = buildResult(e.getMessage(), null, false);
		}
		return resultMap;
	}
	
	
	@RequestMapping("queryAddressList")
	@ResponseBody
	public Map<String, Object> queryAddressList(String keyword, @RequestParam(value = "pageSize",required=false) String pageSizeStr,
			@RequestParam(value = "pageNo",required=false) String pageNoStr,HttpSession session) {
		Map<String, Object> resultMap = null;
		try {
			UserEntity user = (UserEntity) session.getAttribute("user");
			if (null == user) {
				throw new Exception("用户信息不允许为空");
			}
			String userId = user.getId();
			int pageSize = 10;
			if(!StringUtils.isEmpty(pageSizeStr)){
				pageSize = Integer.parseInt(pageSizeStr);
				if(pageSize < 1 || pageSize > 100){
					pageSize = 10;
				}
			}
			int pageNo = 1;
			if(!StringUtils.isEmpty(pageNoStr)){
				pageNo = Integer.parseInt(pageNoStr);
				if(pageNo < 1 ){
					pageNo = 1;
				}
			}
			resultMap = this.logisticsService.queryAddressList(keyword, pageNo, pageSize, userId);
			resultMap = buildResult(null, resultMap, true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultMap = buildResult(e.getMessage(), null, false);
		}
		return resultMap;
	}
	
	@RequestMapping("queryByIds")
	@ResponseBody
	public Map<String,Object> queryByIds(@RequestParam(required=true)String orderIds,HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String,Object> resultMap = null;
		try {
			
			if (null == user) {
				throw new Exception("用户信息不允许为空");
			}
			
			if(StringUtils.isEmpty(orderIds)){
				resultMap = buildResult("订单编号不能为空", null, false);
			}
			String ids[]=orderIds.split(",");
			List<Map<String, Object>> result = this.orderService.queryByIds(ids);
			
			//按传的id顺序返回
			List<Map<String, Object>> tempresult=new ArrayList<Map<String,Object>>();
			if(ids!=null&&result!=null&&ids.length>0&&result.size()>0){
				for(int i=0;i<ids.length;i++){
					String id=ids[i];
					Map<String,Object> tempmap=new HashMap<String,Object>();
					for(int j=0;j<result.size();j++){
						Map<String,Object> map=result.get(j);
						String tempid=(String) map.get("id");
						if(tempid!=null&&tempid.equals(id)){
							tempmap=map;
							break;
						}
					}
					tempresult.add(tempmap);
				}
			}
			//
			
			resultMap=buildResult(null, tempresult, true);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultMap = buildResult("后台异常", null, false);
		}
		return resultMap;
	}
	
}
