package com.supermap.egispweb.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.supermap.egispservice.area.AreaEntity;
import com.supermap.egispservice.area.PageQueryResult;
import com.supermap.egispservice.area.Point2D;
import com.supermap.egispservice.area.exceptions.AreaException;
import com.supermap.egispservice.area.exceptions.AreaException.ExceptionType;
import com.supermap.egispservice.area.service.IAreaService;
import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.entity.PointEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.service.InfoDeptService;
import com.supermap.egispservice.base.service.PointService;
import com.supermap.egispservice.base.service.UserService;
import com.supermap.egispservice.geocoding.service.IGeocodingService;
import com.supermap.egispweb.common.AreaConstants;
import com.supermap.egispweb.excelview.AreaExcelView;
import com.supermap.egispweb.util.FieldMap;
import com.supermap.egispweb.util.StringUtil;
import com.supermap.utils.FileUtil;

/**
 * 
 * <p>Title: AreaManagerAction</p>
 * Description: 区域面管理
 *
 * @author Huasong Huang
 * CreateTime: 2014-9-15 下午03:12:42
 */
@Controller
@RequestMapping("areaService")
@SessionAttributes(types = { UserEntity.class }, value = { "user" })
public class AreaManagerAction {
	private static Logger LOGGER = Logger.getLogger(AreaManagerAction.class);
	
	@Resource
	private IAreaService areaService;
	
	private ObjectMapper objMapper = new ObjectMapper();
	
	@Autowired
	private InfoDeptService infoDeptService;
	@Autowired
	private  UserService userService;
	
	@Autowired
	private PointService pointService;
	
	@Autowired
	private IGeocodingService geocodingService;
	
	/**
	 * 
	 * <p>Title ：addArea</p>
	 * Description：		添加区域面
	 * @param request
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-16 上午10:08:13
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="add")
	@ResponseBody
	public Map<String, Object> addArea(HttpServletRequest request, HttpSession session) {
		UserEntity user=(UserEntity)session.getAttribute("user");
		String areaName = request.getParameter("areaName");
		String areaNumber = request.getParameter("areaNum");
		String point2Ds = request.getParameter("point2Ds");
		String admincode = request.getParameter("admincode");
		
		String wgzCode = request.getParameter("wgzCode");//网格组编号
		String wgzName = request.getParameter("wgzName");//网格组名称
		String lineCode = request.getParameter("lineCode");//线路编号
		String lineName = request.getParameter("lineName");//线路名称
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			if (StringUtil.isStringEmpty(areaName)) {
				throw new AreaException(ExceptionType.NULL_NO_ALLOW, "areaName");
			}
			if (StringUtil.isStringEmpty(areaNumber)) {
				throw new AreaException(ExceptionType.NULL_NO_ALLOW, "areaNumber");
			}
			if (StringUtil.isStringEmpty(point2Ds)) {
				throw new AreaException(ExceptionType.NULL_NO_ALLOW, "point2Ds");
			}
			if (StringUtil.isStringEmpty(admincode)) {
				throw new AreaException(ExceptionType.NULL_NO_ALLOW, "admincode");
			}
			
			List<Map<String, Object>> list = objMapper.readValue(point2Ds, List.class);
			if (null == list) {
				throw new AreaException("参数解析失败[point2ds:" + point2Ds + "]");
			}
			
			Point2D[] point2ds = convert(list);
			String userId = user.getId();
			String enterpriseId = user.getEid().getId();
			String dcode = user.getDeptId().getCode();
			//7.29 增加判断逻辑：试用用户不超过50个面
			boolean flag=canSaveArea(user);
			if(flag){
				String id = areaService.addArea(areaName, areaNumber, null,admincode, userId, enterpriseId, dcode, point2ds,wgzCode,wgzName,lineCode,lineName);
				resultMap = buildResult("添加责任区成功[id:" + id + "]", null, true);
				
				//9.29增加 区划绑定网点
				String pointid = (request.getParameter("pointid")==null||request.getParameter("pointid").equals(""))?null:request.getParameter("pointid");
				if(pointid!=null&&!pointid.equals("")){
					PointEntity point=this.pointService.queryById(pointid);
					point.setAreaId(id);
					boolean updatepoint=this.pointService.updatePoint(point);
				}
			}else{
				resultMap = buildResult("您好，试用最多只能保存50个面", null, false);
			}
		} catch (Exception e) {
			resultMap = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultMap;
	}
	
	
	
	
	
	/**
	 * 
	 * <p>Title ：importZip</p>
	 * Description：		导入UDB.zip文件
	 * @param myFile
	 * @param response
	 * @param session
	 * Author：Huasong Huang
	 * CreateTime：2014-12-10 下午03:29:55
	 */
	@RequestMapping("import")
	@ResponseBody
	public Map<String,Object> importZip(MultipartFile myFile, HttpServletResponse response, HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		
		try {
			if(null == myFile){
				throw new Exception("上传文件为空");
			}
			String userId = user.getId();
			String enterpriseId = user.getEid().getId();
			String dcode = user.getDeptId().getCode();
			// 暂时保存到服务器本地，这样必须保证区域面服务和
			String fileName = saveUploadFile(myFile, AreaConstants.IMPORT_DIR);
			// 请求区域面管理服务处理该数据文件
			boolean isImportSuccess = this.areaService.importUDB(fileName, userId, enterpriseId, dcode);
			resultMap = buildResult("导入UDB数据"+(isImportSuccess?"成功":"失败"), null, isImportSuccess);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			resultMap = buildResult(e.getMessage(), null, false);
		}
		return resultMap;
	}
	
	/**
	 * 
	 * <p>Title ：saveUploadFile</p>
	 * Description：		保存上传的文件
	 * @param multiFile
	 * @param dirName
	 * @return
	 * @throws IOException
	 * Author：Huasong Huang
	 * CreateTime：2014-12-10 下午04:29:48
	 */
	private String saveUploadFile(MultipartFile multiFile, String dirName) throws IOException {
		String picPath = null;
		if (multiFile != null) {
			InputStream is = multiFile.getInputStream();
			if (null != is) {
				String picName = multiFile.getOriginalFilename();
				picName = randomFileName(picName);
				boolean isUploadSuccess = FileUtil.saveToFile(is, picName, dirName);
				if (isUploadSuccess) {
					picPath = picName;
					LOGGER.info("## 上传UDB成功[" + picPath + "]");
				}
			}
		}

		return picPath;
	}
	
	/**
	 * 
	 * <p>Title ：randomFileName</p>
	 * Description：		对文件名加上
	 * @param fileName
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-12-10 下午03:57:46
	 */
	private String randomFileName(String fileName){
		UUID uuid = UUID.randomUUID();
		return uuid+"_"+fileName;
	}
	
	
	
	@RequestMapping("updateAttr")
	@ResponseBody
	public Map<String,Object> updateAttr(String id,String areaName,String netId,String areaNum,String admincode,String pointid,
			@RequestParam(required=false,defaultValue="0")int areastatus,@RequestParam(required=false)String relationareaid,
			@RequestParam(required=false)String wgzCode,@RequestParam(required=false)String wgzName,@RequestParam(required=false)String lineCode,@RequestParam(required=false)String lineName,
			HttpSession session){
		Map<String,Object> resultObj = null;
		UserEntity user=(UserEntity)session.getAttribute("user");
		try{
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String enterpriseId=user.getEid().getId();
			String deptid=user.getDeptId().getId();
			
			boolean isSuccess = this.areaService.updateAreaAttribution(id, areaName.replaceAll("'", "''"), areaNum.replaceAll("'", "''"), netId,admincode,areastatus,relationareaid,wgzCode,wgzName,lineCode,lineName);
			
			
			if(isSuccess){
				//9.29增加 区划绑定网点
				String pointids = (pointid==null||pointid.equals(""))?null:pointid;
				if(pointids!=null&&!pointids.equals("")){
					//20151012需要增加逻辑，自动解除原区划网点关系
					//将网点名称查出来
					List<PointEntity> pointlist=this.pointService.queryByAreaId(id, enterpriseId, deptid);
					if(pointlist!=null&&pointlist.size()>0){
						for(PointEntity entity:pointlist){
							entity.setAreaId("");//将区划设置为空，解除绑定关系
							boolean flag=this.pointService.updatePoint(entity);
						}
					}
					PointEntity point=this.pointService.queryById(pointids);
					point.setAreaId(id);
					boolean updatepoint=this.pointService.updatePoint(point);
				}
			}
			
			resultObj = buildResult("更新区域属性["+id+"]"+(isSuccess?"成功":"失败"), null, isSuccess);
		}catch(Exception e){
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("updateArea")
	@ResponseBody
	public Map<String,Object> updateRegion(HttpServletRequest request){
		Map<String,Object> map = null;
		String id = request.getParameter("id");
		String point2ds = request.getParameter("point2Ds");
		String parts = request.getParameter("parts");
		try {
			if(StringUtil.isStringEmpty(id)){
				throw new AreaException(ExceptionType.NULL_NO_ALLOW, "id");
			}
			if(StringUtil.isStringEmpty(point2ds)){
				throw new AreaException(ExceptionType.NULL_NO_ALLOW,"point2Ds");
			}
			if(StringUtil.isStringEmpty(parts)){
				throw new AreaException(ExceptionType.NULL_NO_ALLOW,"point2Ds");
			}
			List<Map<String,Object>> list = objMapper.readValue(point2ds, List.class);
			List<Integer> partLists = objMapper.readValue(parts, List.class);
			
			Point2D[] point2Ds = convert(list);
			boolean isSuccess = this.areaService.updateAreaRegion(id, point2Ds,partLists);
			map = buildResult("更新区域面["+id+"]"+(isSuccess?"成功":"失败"), null, isSuccess);
		} catch (Exception e) {
			map = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}
	
	/**
	 * 
	 * <p>Title ：lineSpilt</p>
	 * Description：		使用线条拆分区域面
	 * @param request
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-12-10 上午10:38:39
	 */
	@RequestMapping("lineSplit")
	@ResponseBody
	public Map<String,Object> lineSpilt(HttpServletRequest request,HttpSession session){
		Map<String,Object> map = null;
		UserEntity user=(UserEntity)session.getAttribute("user");
		String departmentCode = user.getDeptId().getCode();
		String id = request.getParameter("id");
		String point2ds = request.getParameter("point2Ds");
		try {
			if(StringUtil.isStringEmpty(id)){
				throw new AreaException(ExceptionType.NULL_NO_ALLOW, "id");
			}
			if(StringUtil.isStringEmpty(point2ds)){
				throw new AreaException(ExceptionType.NULL_NO_ALLOW,"point2Ds");
			}
			List<Map<String,Object>> list = objMapper.readValue(point2ds, List.class);
			Point2D[] point2Ds = convert(list);
			if(null == point2Ds || point2Ds.length < 2){
				throw new AreaException("拆分线节点数为空或不能构成线");
			}
			
			//8.7增加
			boolean flag=canSaveArea(user);
			if(flag){
				boolean isSuccess = this.areaService.lineSplit(point2Ds, id, departmentCode);
				map = buildResult("拆分区域面["+id+"]"+(isSuccess?"成功":"失败"), null, isSuccess);
			}
			else {
				map = buildResult("您好，试用最多只能保存50个面", null, false);
			}
			
		} catch (Exception e) {
			map = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}
	
	
	/**
	 * 
	 * <p>Title ：regionSplit</p>
	 * Description：		使用面进行区域面拆分
	 * @param request
	 * @param session
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-12-10 上午10:51:09
	 */
	@RequestMapping("regionSplit")
	@ResponseBody
	public Map<String,Object> regionSplit(HttpServletRequest request , HttpSession session){
		Map<String,Object> map = null;
		UserEntity user=(UserEntity)session.getAttribute("user");
		String departmentCode = user.getDeptId().getCode();
		String id = request.getParameter("id");
		String point2ds = request.getParameter("point2Ds");
		try {
			if(StringUtil.isStringEmpty(id)){
				throw new AreaException(ExceptionType.NULL_NO_ALLOW, "id");
			}
			if(StringUtil.isStringEmpty(point2ds)){
				throw new AreaException(ExceptionType.NULL_NO_ALLOW,"point2Ds");
			}
			List<Map<String,Object>> list = objMapper.readValue(point2ds, List.class);
			Point2D[] point2Ds = convert(list);
			if(null == point2Ds || point2Ds.length < 4){
				throw new AreaException("拆分面节点数为空或不能构成面");
			}
			
			//8.7增加
			boolean flag=canSaveArea(user);
			if(flag){
				boolean isSuccess = this.areaService.regionSplit(point2Ds, id, departmentCode);
				map = buildResult("拆分区域面["+id+"]"+(isSuccess?"成功":"失败"), null, isSuccess);
			}
			else {
				map = buildResult("您好，试用最多只能保存50个面", null, false);
			}
			
			
		} catch (Exception e) {
			map = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}
	
	
	/**
	 * 
	 * <p>Title ：union</p>
	 * Description：		面合并
	 * @param ids
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-12-10 上午11:02:37
	 */
	@RequestMapping("union")
	@ResponseBody
	public Map<String,Object> union(@RequestParam(required=true)String ids,HttpSession session){
		Map<String,Object> map = null;
		UserEntity user=(UserEntity)session.getAttribute("user");
		String departmentCode = user.getDeptId().getCode();
		try {
			String areaIds[] = ids.split("_");
			if(ids.length() < 2){
				throw new AreaException("面合并至少需要两个面");
			}
			boolean isSuccess = this.areaService.mergeRegion(areaIds, departmentCode);
			map = buildResult("合并区域面"+(isSuccess?"成功":"失败"), null, isSuccess);
		} catch (Exception e) {
			map = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}
	
	
	
	
	
	@RequestMapping("deleteArea")
	@ResponseBody
	public Map<String,Object> deleteArea(@RequestParam(required=true)String id,HttpSession session){
		Map<String,Object> map = null;
		try{
			
			UserEntity user=(UserEntity)session.getAttribute("user");
			if (null == user) {
				throw new Exception("用户未登录");
			}
			String userid=user.getId();
			String enterpriseId=user.getEid().getId();
			String deptid=user.getDeptId().getId();
			
			LOGGER.info("## 调用删除区域面接口,区域ID["+id+']');
			boolean isSuccess = this.areaService.deleteRegion(id);
			
			//删除区域面时，同时将网点中绑定的该区划id清空  10.22
			if(isSuccess){
				 List<PointEntity> pointlist=this.pointService.queryByAreaId(id, enterpriseId, deptid);
				 if(pointlist!=null&&pointlist.size()>0){
					for(PointEntity entity:pointlist){
						entity.setAreaId("");
						this.pointService.updatePoint(entity);
					} 
				 }
			}
			
			
			map = buildResult("删除区域面["+id+"]"+(isSuccess?"成功":"失败"), null, isSuccess);
		}catch(Exception e){
			map = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}
	
	@RequestMapping("bindNet")
	@ResponseBody
	public Map<String,Object> bindNet(@RequestParam( required=true)String id,@RequestParam( required=true)String netId){
		Map<String,Object> map = null;
		try {
			boolean isSuccess = this.areaService.bindNet(id, netId);
			map = buildResult("为区域面绑定网点["+netId+"]"+(isSuccess?"成功":"失败"), null, isSuccess);
		} catch (Exception e) {
			map = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}
	
	
	@RequestMapping("queryAllArea")
	@ResponseBody
	public Map<String,Object> queryAllArea(String areaName,String areaNumber,String admincode,@RequestParam(defaultValue="true",required=false)String isNeedPoint
			,HttpSession session){
		UserEntity user=(UserEntity)session.getAttribute("user");
		Map<String,Object> map = null;
		try {
			String enterpriseId = user.getEid().getId();
			String dcode = user.getDeptId().getCode();
			List<AreaEntity> list = this.areaService.queryByEnOrDe(areaName,areaNumber,admincode,enterpriseId, dcode, Boolean.parseBoolean(isNeedPoint));
			if(null != list && list.size() > 0){
				AreaEntity[] areaEntitys = new AreaEntity[list.size()];
				list.toArray(areaEntitys);
				map = buildResult(null, areaEntitys, true);
			}else{
				map = buildResult("查询结果为空", null, false);
			}
		} catch (AreaException e) {
			map = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}
	
	@RequestMapping("queryArea")
	@ResponseBody
	public Map<String,Object> queryArea(HttpServletRequest request,HttpSession session){
		UserEntity user=(UserEntity)session.getAttribute("user");
		String id = request.getParameter("id");
		String areaNum = request.getParameter("areaNum");
		Map<String,Object> map = null;
		try {
			AreaEntity ae = this.areaService.queryByIdOrNumber(id, areaNum, null, true);
			if(null != ae){
				map = buildResult(null, ae, true);
			}else{
				map = buildResult("未查询到相关结果", null, false);
			}
		} catch (Exception e) {
			map = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}
	
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
	 * <p>Title ：convert</p>
	 * Description：		点串对象转换
	 * @param list
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-9-16 上午10:07:48
	 */
	private Point2D[] convert(List<Map<String,Object>> list){
		Point2D[] point2ds = new Point2D[list.size()];
		for(int i=0;i<list.size();i++){
			Map<String,Object> map = list.get(i);
			point2ds[i] = new Point2D();
			Object xO = map.get("x");
			Object yO = map.get("y"); 
			Double xD = null;
			Double yD = null;
			// 解决整型无法转换为双精度
			if(xO instanceof Integer){
				Integer xI = (Integer) xO;
				xD = new Double(xI.doubleValue());
			}else{
				xD = (Double) map.get("x");
			}
			if(yO instanceof Integer){
				Integer yI = (Integer) yO;
				yD = new Double(yI.doubleValue());
			}else{
				yD = (Double) map.get("y");
			}
			point2ds[i].setX(xD);
			point2ds[i].setY(yD);
		}
		return point2ds;
	}
	
	@RequestMapping("queryAreaByPage")
	@ResponseBody
	public PageQueryResult queryAreaByPage(String areaName,String page,String rows,HttpSession session){
		UserEntity user=(UserEntity)session.getAttribute("user");
		PageQueryResult pqr = null;
		try {
			int pageInt = 1;
			int pageCount = 10;
			if(!StringUtils.isEmpty(page)){
				pageInt = Integer.parseInt(page);
				pageInt = pageInt <= 0?1:pageInt;
			}
			if(!StringUtils.isEmpty(rows)){
				pageCount = Integer.parseInt(rows);
				pageCount = (pageCount > 30 || pageCount <= 0)?10:pageCount;
			}
			 pqr = this.areaService.queryAreaByNamePage(user.getId(), areaName, pageInt, pageCount, false);
			return pqr;
		} catch (Exception e) {
			pqr = new PageQueryResult();
			pqr.setPage(0);
			pqr.setRecords(0);
			pqr.setTotal(0);
			LOGGER.error(e.getMessage(), e);
		}
		return pqr;
	}

	/**
	 * 
	 * <p>Title ：exportUDB</p>
	 * Description：			导出UDB数据
	 * @param areaIds
	 * @param seesion
	 * @param resp
	 * Author：Huasong Huang
	 * CreateTime：2014-12-11 下午03:30:33
	 */
	@RequestMapping("export")
	@ResponseBody
	public void exportUDB(@RequestParam(required = true)String ids,HttpServletResponse response,HttpSession session){
		UserEntity user=(UserEntity)session.getAttribute("user");
		try {
			if(StringUtils.isEmpty(ids)){
				throw new Exception("areaIds不允许为空");
			}
			String areaIds[] = ids.split("_");
			String dcode = user.getDeptId().getCode(); 
			String path = this.areaService.exportUDB(areaIds, dcode);
			File f = new File(path);
			LOGGER.info("## 导出文件路径["+path+"]");
			if(f.exists() && f.isFile()){
				FileInputStream is = new FileInputStream(f);
				byte[] buff = new byte[1024];
				OutputStream os = response.getOutputStream();
				int size = 0;
				while((size = is.read(buff)) > 0){
					os.write(buff,0,size);
				}
				os.flush();
				os.close();
				is.close();
			}else{
				throw new Exception(path+"不存在或指定的路径不是文件");
			}
		} catch (Exception e) {
			try {
				LOGGER.error(e.getMessage(), e);
				response.getWriter().write(e.getMessage());
			} catch (IOException e1) {
				LOGGER.error(e.getMessage(), e);
			}
		}
		
	}
	
	
	/**
	 * 首页 查询前10条 区域面
	 * @param areaName
	 * @param areaNumber
	 * @param admincode
	 * @param isNeedPoint
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-6-29
	 */
	@RequestMapping("queryAllAreaTop10")
	@ResponseBody
	public Map<String,Object> queryAllAreaTop10(String areaName,String areaNumber,String admincode,@RequestParam(defaultValue="true",required=false)String isNeedPoint,HttpSession session){
		UserEntity user=(UserEntity)session.getAttribute("user");
		Map<String,Object> map = null;
		try {
			String enterpriseId = user.getEid().getId();
			String dcode = user.getDeptId().getCode();
			List<AreaEntity> list = this.areaService.queryByEnOrDeTop10(areaName,areaNumber,admincode,enterpriseId, null, true);
			if(null != list && list.size() > 0){
				AreaEntity[] areaEntitys = new AreaEntity[list.size()];
				list.toArray(areaEntitys);
				map = buildResult(null, areaEntitys, true);
			}else{
				map = buildResult("查询结果为空", null, false);
			}
		} catch (AreaException e) {
			map = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}
	
	/**
	 * 按省市区查询区域面数据
	 * @param admincode
	 * @param level
	 * @param isNeedPoint
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2015-7-27下午4:42:15
	 */
	@RequestMapping("queryAllAreaByLevel")
	@ResponseBody
	public Map<String,Object> queryAllAreaByLevel(String areaName,String areaNumber,String admincode, int level,@RequestParam(defaultValue="true",required=false)String isNeedPoint,HttpSession session){
		UserEntity user=(UserEntity)session.getAttribute("user");
		Map<String,Object> map = null;
		String admincodetemp="110";//默认北京
		
		try {
			if(null==user){
				throw new AreaException("用户未登录");
			}
			String userid=user.getId();
			String enterpriseId = user.getEid().getId();
			String dcode = user.getDeptId().getCode();
			String deptid=user.getDeptId().getId();
			
			//查询子账号
			Map<String,String> usermaps=this.userService.findAllUserByEid(enterpriseId);
			if(null==usermaps){
				usermaps=new HashMap<String,String>();
			}
			
			if(!StringUtils.isEmpty(admincode)){
				admincodetemp=formatAdmincode(admincode,level);
			}
			
			if(!StringUtils.isEmpty(areaName)){
				areaName=areaName.replaceAll("'", "''");
			}
			if(!StringUtils.isEmpty(areaNumber)){
				areaNumber=areaNumber.replaceAll("'", "''");
			}
			
//			List<AreaEntity> list = this.areaService.queryByEnOrDe(areaName,areaNumber,admincodetemp,enterpriseId, dcode, Boolean.parseBoolean(isNeedPoint));
			List<AreaEntity> list = this.areaService.queryByEnOrDe(areaName,areaNumber,admincodetemp,enterpriseId, null, Boolean.parseBoolean(isNeedPoint));

			if(null != list && list.size() > 0){
				List<Map> maplist=new ArrayList<Map>();
				for(AreaEntity Entity:list){
					//坐标加密
					Point2D[]  points=Entity.getPoints();
					if(null!=points&&points.length>0){
						int xp=951753;int yp=852456;
						for(Point2D point:points){
							point.setX((int)(point.getX()*100)^xp);
							point.setY((int)(point.getY()*100)^yp);
						}
					}
					
					Map m=FieldMap.convertBean(Entity);
//					List<String> pointnamelist=new ArrayList<String>();
					StringBuilder pointnamesb=new StringBuilder();
					//将网点名称查出来
					List<PointEntity> pointlist=this.pointService.queryByAreaId(Entity.getId(),  enterpriseId, null);
					if(pointlist!=null&&pointlist.size()>0){
						for(PointEntity point:pointlist){
							pointnamesb.append(point.getName()).append(",");
						}
					}
					if(pointnamesb!=null&&pointnamesb.length()>0){
						m.put("pointnames", pointnamesb.substring(0, pointnamesb.length()-1));
					}
					else m.put("pointnames", "");
					//所属帐号的用户名
//					if(!userid.equals(Entity.getUser_id())){
						m.put("username", usermaps.get(Entity.getUser_id()));
//					}else{
//						m.put("username", null);
//					}
					maplist.add(m);
				}
				map = buildResult(null, maplist, true);
			}else{
				map = buildResult("查询结果为空", null, false);
			}
		} catch (AreaException e) {
			map = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return map;
	}
	
	
	/**
	 * 根据部门id查找顶级部门
	 * @param id
	 * @return
	 * @Author Juannyoh
	 * 2015-7-29下午3:10:12
	 */
	public InfoDeptEntity findTopParentById(String id) {
		InfoDeptEntity record=new InfoDeptEntity();
		record=this.infoDeptService.findDeptById(id);
		if(record.getParentId()!=null&&!record.getParentId().equals("")){
			record=this.findTopParentById(record.getParentId());
		}
		return record;
	}
	
	/**
	 * 判断用户画面的限制
	 * 试用用用户最多画50个面
	 * @param user
	 * @return
	 * @Author Juannyoh
	 * 2015-8-7下午4:42:16
	 */
	public boolean canSaveArea(UserEntity user){
		boolean flag=true;
		//7.29 增加判断逻辑：试用用户不超过50个面
		InfoDeptEntity dept=findTopParentById(user.getDeptId().getId());//获取顶级部门
		List<UserEntity> userlist=this.userService.getUsersByCurrentDept(dept.getId());//根据顶级部门查找用户
		UserEntity topuser=userlist.get(0);//顶级部门按时间升序排序，所以get0
		int status=this.userService.findCountByUserStat(topuser.getId());//查找订单表该用户是否是试用用户 ；count是该用户下正式订单数量
		try {
			if(status!=2){//试用用户,2表示的是正式用户 
				//查询已有的区域面数量
				List<AreaEntity> exsitEntityList= areaService.queryByEnOrDe(null, null, null, user.getEid().getId(), dept.getCode(), false);
				if(exsitEntityList!=null&&exsitEntityList.size()>=50){
					flag=false;
				}
				else{
					flag=true;
				}
			}
			else flag=true;
		} catch (AreaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * 修改区划所属用户
	 * @param id
	 * @param userid
	 * @param dcode
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2016-10-20下午4:03:36
	 */
	@RequestMapping("updateOwner")
	@ResponseBody
	public Map<String,Object> updateOwner(@RequestParam(required=true)String id,@RequestParam(required=true)String userid,HttpSession session){
		Map<String,Object> resultObj = null;
		UserEntity user=(UserEntity)session.getAttribute("user");
		try{
			if (null == user) {
				throw new Exception("用户未登录");
			}
			UserEntity changeuser=this.userService.findUserById(userid);
			if(null==changeuser){
				return buildResult("未找到该用户信息", null, false);
			}
			boolean isSuccess = this.areaService.updateAreaOwner(id, userid, user.getEid().getId(), changeuser.getDeptId().getCode());
			
			resultObj = buildResult("更新区域所属["+id+"]"+(isSuccess?"成功":"失败"), null, isSuccess);
		}catch(Exception e){
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	/**
	 * 保存用户反选的行政区空白区
	 * @param admincode
	 * @param levelstr
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2016-10-27下午3:57:05
	 */
	@RequestMapping("saveReverseSelectionArea")
	@ResponseBody
	public Map<String,Object> saveReverseSelectionArea(@RequestParam(required=true)String admincode,@RequestParam(required=true,value="level")String levelstr,HttpSession session){
		Map<String,Object> resultObj = null;
		UserEntity user=(UserEntity)session.getAttribute("user");
		try{
			if (null == user) {
				throw new Exception("用户未登录");
			}
			boolean flag=canSaveArea(user);
			if(flag){
				int level=Integer.parseInt(levelstr);
				String userid=user.getId();
				String eid=user.getEid().getId();
				String dcode=user.getDeptId().getCode();
				String id = this.areaService.saveReverseSelectionArea(admincode,level, userid,eid, dcode);
				if(StringUtils.isEmpty(id)){
					resultObj = buildResult("生成区划失败", null, false);
				}else{
					resultObj = buildResult("生成区划["+id+"]"+"成功", id, true);
				}
			}else{
				resultObj = buildResult("您好，试用最多只能保存50个面", null, false);
			}
		}catch(Exception e){
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}
	
	
	/**
	 * 导出所有区划属性数据
	 * @param areaName
	 * @param areaNumber
	 * @param admincode
	 * @param level
	 * @param isNeedPoint
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2016-11-30下午5:03:41
	 */
	@RequestMapping("exportAllArea")
	@ResponseBody
	public ModelAndView ExportAllArea(String areaName,String areaNumber,String admincode, @RequestParam(required=false,defaultValue="1")int level,@RequestParam(defaultValue="false",required=false)String isNeedPoint,
			@RequestParam(value="start",required=false)String bdate,@RequestParam(value="end",required=false)String edate,
			HttpSession session){
		UserEntity user=(UserEntity)session.getAttribute("user");
		Map<String,Object> map = null;
		String admincodetemp=null;
		try {
			if(null==user){
				throw new AreaException("用户未登录");
			}
			String enterpriseId = user.getEid().getId();
			String dcode = user.getDeptId().getCode();
			String deptid=user.getDeptId().getId();
			
			//查询子账号
			Map<String,String> usermaps=this.userService.findAllUserByEid(enterpriseId);
			if(null==usermaps){
				usermaps=new HashMap<String,String>();
			}
			
			if(!StringUtils.isEmpty(admincode)){
				admincodetemp=formatAdmincode(admincode,level);
			}
			
			if(!StringUtils.isEmpty(areaName)){
				areaName=areaName.replaceAll("'", "''");
			}
			if(!StringUtils.isEmpty(areaNumber)){
				areaNumber=areaNumber.replaceAll("'", "''");
			}
			long start=System.currentTimeMillis();
			List<AreaEntity> list = this.areaService.queryByEnOrDeAndDate(areaName,areaNumber,admincodetemp,enterpriseId, null, Boolean.parseBoolean(isNeedPoint),bdate,edate);
			long end=System.currentTimeMillis();
			LOGGER.info("查询耗时："+(end-start)+"ms");
			if(null != list && list.size() > 0){
				List<Map> maplist=new ArrayList<Map>();
				for(AreaEntity Entity:list){
					Map m=FieldMap.convertBean(Entity);
					StringBuilder pointnamesb=new StringBuilder();
					//将网点名称查出来
					List<PointEntity> pointlist=this.pointService.queryByAreaId(Entity.getId(), enterpriseId, null);
					if(pointlist!=null&&pointlist.size()>0){
						for(PointEntity point:pointlist){
							pointnamesb.append(point.getName()).append(",");
						}
					}
					if(pointnamesb!=null&&pointnamesb.length()>0){
						m.put("pointnames", pointnamesb.substring(0, pointnamesb.length()-1));
					}
					else m.put("pointnames", "");
					m.put("username", usermaps.get(Entity.getUser_id()));
					
					//省市区查询
					if(!StringUtils.isEmpty(Entity.getAdmincode())){
						Map<String,Object> adminmap=this.geocodingService.getCountyByAdmincode(Entity.getAdmincode());
						if(adminmap!=null){
							m.putAll(adminmap);
						}
					}
					
					maplist.add(m);
				}
				map = new HashMap<String,Object>();
				map.put("rows", maplist);
				long end1=System.currentTimeMillis();
				LOGGER.info("关联耗时："+(end1-end)+"ms");
			}else{
				return null;
			}
		} catch (AreaException e) {
			map = null;
			LOGGER.error(e.getMessage(), e);
		}
		AreaExcelView ve=new AreaExcelView();
		return new ModelAndView(ve, map);
	}
	
	
	/**
	 * 按层级截取admincode
	 * @param admincode
	 * @param level
	 * @return
	 * @Author Juannyoh
	 * 2016-11-30下午5:15:09
	 */
	public String formatAdmincode(String admincode,int level){
		if(StringUtils.isEmpty(admincode)){
			return null;
		}
		String admincodetemp=null;
		//判断直辖市 北京110，天津120，重庆500，上海310
		if(admincode.indexOf("110")==0||admincode.indexOf("120")==0||admincode.indexOf("500")==0||admincode.indexOf("310")==0){
			switch (level) {
			case 1://市
				admincodetemp=admincode.substring(0, 3);
				break;
			case 3://区
				admincodetemp=admincode.substring(0, 6);
				break;
			default:
				admincodetemp=admincode;
				break;
			}
		}
		//其他省市区
		else {
			switch (level) {
			case 1://省
				admincodetemp=admincode.substring(0, 2);
				break;
			case 2://市
				admincodetemp=admincode.substring(0, 4);
				break;
			case 3://区
				admincodetemp=admincode.substring(0, 6);
				break;
			default:
				admincodetemp=admincode;
				break;
			}
		}
		return admincodetemp;
	}
	
	/**
	 * 修改区划状态
	 * @param id
	 * @param areastatus
	 * @param session
	 * @return
	 * @Author Juannyoh
	 * 2016-12-20下午1:40:58
	 */
	@RequestMapping("updateAreaStatus")
	@ResponseBody
	public Map<String,Object> updateAreaStatus(@RequestParam(required=true)String id,@RequestParam(required=true,defaultValue="0")int areastatus,HttpSession session){
		Map<String,Object> resultObj = null;
		UserEntity user=(UserEntity)session.getAttribute("user");
		try{
			if (null == user) {
				throw new Exception("用户未登录");
			}
			boolean isSuccess = this.areaService.changeStatus(id, areastatus);
			resultObj = buildResult("更新区划状态["+id+"]"+(isSuccess?"成功":"失败"), null, isSuccess);
		}catch(Exception e){
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}


	/**
	 * 批量保存行政区转区划
	 * @param adminsLevels 行政区代码及级别信息，格式：510000,1,四川省;420000,1,湖北省;
	 * @param session
	 * @return
	 */
	@RequestMapping(value="saveSelectedAdmins",method= RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> saveSelectedAdmins(@RequestParam(required=true)String adminsLevels,HttpSession session){
		Map<String,Object> resultObj = null;
		UserEntity user=(UserEntity)session.getAttribute("user");
		try{
			if (null == user) {
				throw new Exception("用户未登录");
			}
			if(StringUtils.isEmpty(adminsLevels)){
				return null ;
			}
			session.removeAttribute("currentNum");
			StringBuilder failAdminNames=new StringBuilder();
			String userid=user.getId();
			String eid=user.getEid().getId();
			String dcode=user.getDeptId().getCode();
			String adminLevelArr[]=adminsLevels.split(";");
			int currentCount=1;//当前处理第几条
			int sumCount=adminLevelArr.length;//总条数
			session.setAttribute("sumNum",sumCount);//总条数保存到session
			for(String adminlevel:adminLevelArr){
				String infos[]=adminlevel.split(",");
				String admin=infos[0];
				int level=Integer.parseInt(infos[1]);
				String adminName=infos[2];
				String id =null;
				try {
					id=this.areaService.saveReverseSelectionArea(admin,level,userid,eid, dcode);
				}catch (Exception e){
					if(StringUtils.isEmpty(id)){//添加失败
						failAdminNames.append(adminName).append(",");/*.append("[").append(e.getMessage()).append("],")*/;
					}
				}
				session.setAttribute("currentNum", currentCount);//将当前条数保存到session
				if(currentCount<sumCount){
					currentCount++;
				}
			}
			if(failAdminNames.length()>0){
				failAdminNames.substring(0,failAdminNames.length()-1);
			}
			if(StringUtils.isEmpty(failAdminNames.toString())){
				resultObj = buildResult("载入行政区划成功", null, true);
			}else{
				resultObj = buildResult("载入行政区划完成,以下载入失败["+failAdminNames.toString()+"]", null, true);
			}
		}catch(Exception e){
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
			session.removeAttribute("sumNum");
			session.removeAttribute("currentNum");
		}
		return resultObj;
	}


	/**
	 * 获取载入行政区划进度
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "getDataProcess",method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getDataProcess(HttpSession session){
		UserEntity user = (UserEntity) session.getAttribute("user");
		Map<String, Object> resultObj = null;
		try {
			if (null == user) {
				throw new Exception("session已失效");
			}
			long total=session.getAttribute("sumNum")==null?0:Long.parseLong(session.getAttribute("sumNum").toString());
			long current=session.getAttribute("currentNum")==null?0:Long.parseLong(session.getAttribute("currentNum").toString());
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("current", current);
			map.put("total", total);
			if(current==total){
				session.removeAttribute("sumNum");
				session.removeAttribute("currentNum");
			}
			resultObj = buildResult(null,map,true);
		} catch (Exception e) {
			resultObj = buildResult(e.getMessage(), null, false);
			LOGGER.error(e.getMessage(), e);
		}
		return resultObj;
	}

}
