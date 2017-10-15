package com.supermap.egispweb.action.statistic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.supermap.egispservice.area.AreaEntity;
import com.supermap.egispservice.area.service.IAreaService;
import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.service.InfoDeptService;
import com.supermap.egispservice.geocoding.service.IGeocodingService;
import com.supermap.egispservice.statistic.service.IOrderStatisticService;
import com.supermap.egispweb.excelview.FendansExcelView;

//@Controller
//@RequestMapping(value = "/statistic/order")
//@SessionAttributes("user")
public class OrderStatisticsAction {

	@Autowired
	private IOrderStatisticService orderStatisticService;
	@Autowired
	private InfoDeptService infoDeptService;
	@Autowired
	private IAreaService areaService;
	
	@Autowired
	private IGeocodingService geocodingService;
	
	Logger LOGGER = Logger.getLogger(OrderStatisticsAction.class);

	@RequestMapping(value = "/show")
	public String show() {
		return "statistic/orderStatistic";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/getOrderNum", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getOrderNum(
			@RequestParam(defaultValue = "2014-01-01 01:01:01") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
			@RequestParam(defaultValue = "2114-01-01 01:01:01") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end,
			HttpSession session) {
		
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> result = null;
		try {
			
			List<InfoDeptEntity> deptList = infoDeptService.getChildDepts(user.getDeptId().getId());
			List<String> deptIdList = new ArrayList<String>(deptList.size());
			for (int i = 0; i < deptList.size(); i++) {
				deptIdList.add(deptList.get(i).getId());
			}
			
			List list = orderStatisticService.getOrderCountStatistic(deptIdList, start, end);
			
			List<Map<String, Object>> orderNumList = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = (Map<String, String>) list.get(i);
				String num = String.valueOf(map.get("num"));
				String area_id = map.get("area_id");
				AreaEntity areaEntity = areaService.queryByIdOrNumber(area_id, "",
						user.getDeptId().getCode(), true);

				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("area_id", area_id);
				resultMap.put("num", num);
				resultMap.put("area_entity", areaEntity);
				orderNumList.add(resultMap);
			}
			
			if (null != orderNumList) {
				result = buildResult(null, orderNumList, true);
			}
		} catch (Exception e) {
			result = buildResult(e.getMessage(), null, false);
		}
		return result;
	}

	private Map<String, Object> buildResult(String info, Object result,
			boolean isSuccess) {
		Map<String, Object> resultObj = new HashMap<String, Object>();
		resultObj.put("info", info);
		resultObj.put("result", result);
		resultObj.put("isSuccess", isSuccess);
		return resultObj;
	}
	
	/**
	 * 首页显示时，只显示10条数据
	 * @param start
	 * @param end
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-7-2上午9:31:32
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/getOrderNumTop10", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getOrderNumTop10(
			@RequestParam(defaultValue = "2014-01-01 01:01:01") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
			@RequestParam(defaultValue = "2114-01-01 01:01:01") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end,
			HttpSession session) {
		
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> result = null;
		try {
			
			List<InfoDeptEntity> deptList = infoDeptService.getChildDepts(user.getDeptId().getId());
			List<String> deptIdList = new ArrayList<String>(deptList.size());
			for (int i = 0; i < deptList.size(); i++) {
				deptIdList.add(deptList.get(i).getId());
			}
			
			List list = orderStatisticService.getOrderCountStatisticTop10(deptIdList, start, end);
			
			List<Map<String, Object>> orderNumList = new ArrayList<Map<String, Object>>();
			for (int i = 0; i < list.size(); i++) {
				Map<String, String> map = (Map<String, String>) list.get(i);
				String num = String.valueOf(map.get("num"));
				String area_id = map.get("area_id");
				AreaEntity areaEntity = areaService.queryByIdOrNumber(area_id, "",
						user.getDeptId().getCode(), true);

				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("area_id", area_id);
				resultMap.put("num", num);
				resultMap.put("area_entity", areaEntity);
				orderNumList.add(resultMap);
			}
			
			if (null != orderNumList) {
				result = buildResult(null, orderNumList, true);
			}
		} catch (Exception e) {
			result = buildResult(e.getMessage(), null, false);
		}
		return result;
	}
	
	/**
	 * 订单量统计
	 * @param start
	 * @param end
	 * @param admincode
	 * @param level
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-10-29上午10:46:01
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getOrderCountByAdminCode")
	@ResponseBody
	public Map<String, Object> getOrderCountByAdminCode(
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date start,
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date end,
			String admincode,String level,
			HttpSession session) {
		
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> result = null;
		int adminlevel=1;
		try {
			if(user==null){
				throw new Exception("用户未登录");
			}
			
			List<InfoDeptEntity> deptList = infoDeptService.getChildDepts(user.getDeptId().getId());
			List<String> deptIdList = new ArrayList<String>(deptList.size());
			for (int i = 0; i < deptList.size(); i++) {
				deptIdList.add(deptList.get(i).getId());
			}
			
			//转换admincode
			if(admincode!=null&&level!=null&&!admincode.equals("")&&!level.equals("")){
				admincode=getAdmincode(admincode,Integer.parseInt(level));
			}
			List<Map<String,Object>> list = orderStatisticService.getOrderCountByAdminCode(deptIdList, admincode, level, start, end);
			
			List<Map<String, Object>> orderNumList = new ArrayList<Map<String, Object>>();

			String key=null;
			if(level==null||level.equals("")){
				key="PROVINCE";
			}else if(level.equals("1")){
				//直辖市
				if(admincode!=null&&!admincode.equals("")&&(
						admincode.indexOf("110")==0||admincode.indexOf("120")==0
						||admincode.indexOf("500")==0||admincode.indexOf("310")==0))
				{
					key="COUNTY";
					adminlevel=3;
				}else{
					key="CITY";
					adminlevel=2;
				}
			}else if(level.equals("2")){
				key="COUNTY";
				adminlevel=3;
			}
			
			if(list!=null&&list.size()>0){
				for(Map<String,Object> map:list){
					if(map.get("admincode_")!=null&&!((String)map.get("admincode_")).equals("")){
						String code=formatString((String) map.get("admincode_"),6);
						String tempcode=code;
						if(code.equals("500000")){//重庆市特殊处理
							tempcode="500100";
						}
						String name=(String) this.geocodingService.getCountyByAdmincode(tempcode).get(key);
						Map<String, Object> resultMap = new HashMap<String, Object>();
						resultMap.put("admincode", code);
						resultMap.put("name", name);
						resultMap.put("count", map.get("count"));
						orderNumList.add(resultMap);
					}
				}
			}
			
			
			//所有城市列表查询
			List<Map<String,Object>> adminlist =new ArrayList<Map<String,Object>>();
			if(admincode==null||admincode.equals("")){
				adminlist = this.geocodingService.getAdminElement(null, adminlevel);
			}else{
				adminlist = this.geocodingService.getAdminElement(admincode, adminlevel);
			}
			
			//对比,没有数据的写0
			List<Map<String,Object>> resultlist =orderNumList;
			
			for(Map<String,Object> adminmap:adminlist){//所有城市列表
				String adminmapcode=(String) adminmap.get("ADMINCODE");
				adminmapcode = getAdmincode(adminmapcode,adminlevel);
				Map<String,Object> newmap=new HashMap<String,Object>();
				boolean flag=false;
				if(orderNumList!=null&&orderNumList.size()>0){//有数据的城市列表
					for(Map<String,Object> map:orderNumList){
						flag=false;
						String mapcode=(String) map.get("admincode");
						if(adminmapcode.equals(mapcode)){
							flag=true;
							break;
						}else{
							continue;
						}
					}
					if(!flag&&adminmapcode!=null&&!adminmapcode.equals("")){
						newmap.put("count", 0);
						newmap.put("admincode", adminmapcode);
						newmap.put("name", adminmap.get(key));
						resultlist.add(newmap);
					}
				}else{
					newmap.put("count", 0);
					newmap.put("admincode", adminmapcode);
					newmap.put("name", adminmap.get(key));
					resultlist.add(newmap);
				}
			}
			
			
			if (null != resultlist) {
				result = buildResult(null, resultlist, true);
			}
		} catch (Exception e) {
			result = buildResult(e.getMessage(), null, false);
		}
		return result;
	}
	
	
	public String formatString(String s,int len){
		if(s!=null){
			while(s.length()<len){
				s=s+"0";
			}
		}
		return s;
	}
	
	/**
	 * admincode处理
	 * @param adminmapcode
	 * @param adminlevel
	 * @return
	 * @Author Juannyoh
	 * 2015-10-29下午5:36:33
	 */
	public String getAdmincode(String adminmapcode,int adminlevel){
		switch (adminlevel) {
		case 1:
			adminmapcode=formatString(adminmapcode.substring(0, 2),6);
			break;
		case 2:
			adminmapcode=formatString(adminmapcode.substring(0, 4),6);
			break;
		default:
			break;
		}
		return adminmapcode;
	}
	
	
	/**
	 * 统计用户 订单量 按照 分单成功、坐标定位失败、无区划 三种情况
	 * @param start
	 * @param end
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2016-11-30下午7:40:03
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getAllByResulttype")
	@ResponseBody
	public Map<String, Object> getOrderCountByResulttype(String start,String end,
			HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> result = null;
		try {
			if(user==null){
				throw new Exception("用户未登录");
			}
//			List<String> deptIdList = getChildDeptids(user.getDeptId().getId());
			String eid=user.getEid().getId();
			Date bdate=null;
			Date edate=null;
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(!StringUtils.isEmpty(start)){
				bdate=sdf.parse(start);
			}
			if(!StringUtils.isEmpty(end)){
				edate=sdf.parse(end);
			}
			List<Map<String,Object>> list = this.orderStatisticService.queryAllOrderGroupByReultType(null, bdate, edate,eid);
			result = buildResult(null, list, true);
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
			result = buildResult(e.getMessage(), null, false);
		}
		return result;
	}
	
	/**
	 * 按照admincode、分单结果  进行统计
	 * @return
	 * @Author Juannyoh
	 * 2016-11-30下午8:11:50
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getCountByAdminResulttype")
	@ResponseBody
	public Map<String, Object> getOrderCountByAdminCodeResulttype(String start,String end,
			String admincode,String level,@RequestParam(defaultValue="1")int resulttype,
			HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> result = null;
		int adminlevel=1;
		try {
			if(user==null){
				throw new Exception("用户未登录");
			}
			
//			List<String> deptIdList = getChildDeptids(user.getDeptId().getId());
			String eid=user.getEid().getId();
			
			//转换admincode
			if(admincode!=null&&level!=null&&!admincode.equals("")&&!level.equals("")){
				admincode=getAdmincode(admincode,Integer.parseInt(level));
			}
			Date bdate=null;
			Date edate=null;
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(!StringUtils.isEmpty(start)){
				bdate=sdf.parse(start);
			}
			if(!StringUtils.isEmpty(end)){
				edate=sdf.parse(end);
			}
			List<Map<String,Object>> list = this.orderStatisticService.getOrderCountByAdminCode(null, admincode, level, bdate, edate,resulttype,eid);
			
			List<Map<String, Object>> orderNumList = new ArrayList<Map<String, Object>>();

			String key=null;
			if(level==null||level.equals("")){
				key="PROVINCE";
			}else if(level.equals("1")){
				//直辖市
				if(admincode!=null&&!admincode.equals("")&&(
						admincode.indexOf("110")==0||admincode.indexOf("120")==0
						||admincode.indexOf("500")==0||admincode.indexOf("310")==0))
				{
					key="COUNTY";
					adminlevel=3;
				}else{
					key="CITY";
					adminlevel=2;
				}
			}else if(level.equals("2")){
				key="COUNTY";
				adminlevel=3;
			}
			
			if(list!=null&&list.size()>0){
				for(Map<String,Object> map:list){
					if(map.get("admincode_")!=null&&!((String)map.get("admincode_")).equals("")){
						String code=formatString((String) map.get("admincode_"),6);
						String tempcode=code;
						if(code.equals("500000")){//重庆市特殊处理
							tempcode="500100";
						}
						String name=(String) this.geocodingService.getCountyByAdmincode(tempcode).get(key);
						Map<String, Object> resultMap = new HashMap<String, Object>();
						resultMap.put("admincode", code);
						resultMap.put("name", name);
						resultMap.put("count", map.get("count"));
						orderNumList.add(resultMap);
					}
				}
			}
			
			//所有城市列表查询
			List<Map<String,Object>> adminlist =new ArrayList<Map<String,Object>>();
			if(admincode==null||admincode.equals("")){
				adminlist = this.geocodingService.getAdminElement(null, adminlevel);
			}else{
				adminlist = this.geocodingService.getAdminElement(admincode, adminlevel);
			}
			
			//对比,没有数据的写0
			List<Map<String,Object>> resultlist =orderNumList;
			
			for(Map<String,Object> adminmap:adminlist){//所有城市列表
				String adminmapcode=(String) adminmap.get("ADMINCODE");
				adminmapcode = getAdmincode(adminmapcode,adminlevel);
				Map<String,Object> newmap=new HashMap<String,Object>();
				boolean flag=false;
				if(orderNumList!=null&&orderNumList.size()>0){//有数据的城市列表
					for(Map<String,Object> map:orderNumList){
						flag=false;
						String mapcode=(String) map.get("admincode");
						if(adminmapcode.equals(mapcode)){
							flag=true;
							break;
						}else{
							continue;
						}
					}
					if(!flag&&adminmapcode!=null&&!adminmapcode.equals("")){
						newmap.put("count", 0);
						newmap.put("admincode", adminmapcode);
						newmap.put("name", adminmap.get(key));
						resultlist.add(newmap);
					}
				}else{
					newmap.put("count", 0);
					newmap.put("admincode", adminmapcode);
					newmap.put("name", adminmap.get(key));
					resultlist.add(newmap);
				}
			}
			if (null != resultlist) {
				result = buildResult(null, resultlist, true);
			}
		} catch (Exception e) {
			result = buildResult(e.getMessage(), null, false);
		}
		return result;
	}
	
	/**
	 * 查询分单API与导入的订单  明细
	 * @return
	 * @Author Juannyoh
	 * 2016-11-30下午8:19:58
	 */
	@RequestMapping(value = "/getDetailByAdminResulttype")
	@ResponseBody
	public Map<String, Object> getOrderDetailByAdminResulttype(String start,String end,
			String admincode,String level,@RequestParam(defaultValue="3")int resulttype,
			@RequestParam(defaultValue="1",value="page")int pageNumber, @RequestParam(defaultValue="20",value="rows")int pageSize,
			String ordernum,@RequestParam(value="batch",required=false)String orderbatch,String address, 
			HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> result = null;
		try {
			if(user==null){
				throw new Exception("用户未登录");
			}
			
			if(pageSize>100){
				pageSize=100;
			}
//			List<String> deptIdList = getChildDeptids(user.getDeptId().getId());
			String eid=user.getEid().getId();
			//转换admincode
			if(admincode!=null&&level!=null&&!admincode.equals("")&&!level.equals("")){
				admincode=getAdmincode(admincode,Integer.parseInt(level));
			}
			Date bdate=null;
			Date edate=null;
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(!StringUtils.isEmpty(start)){
				bdate=sdf.parse(start);
			}
			if(!StringUtils.isEmpty(end)){
				edate=sdf.parse(end);
			}
			result = this.orderStatisticService.queryOrderDetailByAdminCode(null, admincode, level, bdate, edate, resulttype, pageNumber, pageSize, ordernum, orderbatch, address,eid);
		} catch (Exception e) {
			result = buildResult(e.getMessage(), null, false);
		}
		return result;
	}
	
	/**
	 * 导出分单明细
	 * @return
	 * @Author Juannyoh
	 * 2016-11-30下午8:28:17
	 */
	@RequestMapping(value = "/exportOrderDetail")
	@ResponseBody
	public ModelAndView ExportOrderDetailByAdminResulttype(String start,String end,
			String admincode,String level,@RequestParam(defaultValue="3")int resulttype,
			String ordernum,@RequestParam(value="batch",required=false)String orderbatch,String address, 
			HttpSession session) {
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> result = null;
		try {
			if(user==null){
				throw new Exception("用户未登录");
			}
			
//			List<String> deptIdList = getChildDeptids(user.getDeptId().getId());
			String eid=user.getEid().getId();
			
			//转换admincode
			if(admincode!=null&&level!=null&&!admincode.equals("")&&!level.equals("")){
				admincode=getAdmincode(admincode,Integer.parseInt(level));
			}
			Date bdate=null;
			Date edate=null;
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(!StringUtils.isEmpty(start)){
				bdate=sdf.parse(start);
			}
			if(!StringUtils.isEmpty(end)){
				edate=sdf.parse(end);
			}
			result = this.orderStatisticService.queryOrderDetailByAdminCode(null, admincode, level, bdate, edate, resulttype, -1, 10, ordernum, orderbatch, address,eid);
		} catch (Exception e) {
			result = null;
		}
		FendansExcelView ev=new FendansExcelView();
		return new ModelAndView(ev, result);
	}
	
	/**
	 * 获得部门下所有部门id
	 * @param deptid
	 * @return
	 * @Author Juannyoh
	 * 2016-11-30下午7:41:17
	 */
	public List<String> getChildDeptids(String deptid){
		List<InfoDeptEntity> deptList = infoDeptService.getChildDepts(deptid);
		List<String> deptIdList = new ArrayList<String>(deptList.size());
		for (int i = 0; i < deptList.size(); i++) {
			deptIdList.add(deptList.get(i).getId());
		}
		return deptIdList;
	}
}
