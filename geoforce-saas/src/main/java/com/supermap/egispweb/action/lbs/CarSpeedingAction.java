package com.supermap.egispweb.action.lbs;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.lbs.entity.CarAlarmForeign;
import com.supermap.egispservice.lbs.entity.SpeedingSet;
import com.supermap.egispservice.lbs.pojo.CarDept;
import com.supermap.egispservice.lbs.service.AlarmTypeService;
import com.supermap.egispservice.lbs.service.CarAlarmForeginService;
import com.supermap.egispservice.lbs.service.CarService;
import com.supermap.egispservice.lbs.service.CarSpeedingService;
import com.supermap.egispweb.common.AjaxResult;
import com.supermap.egispweb.pojo.HasLinkedCarBean;

//@Controller
public class CarSpeedingAction extends BaseAction{
	
	@Autowired
	CarSpeedingService carSpeedingService;
	
	@Autowired
	CarAlarmForeginService  carAlarmForeginService;
	
	@Autowired
	CarService carService;
	
	@Autowired
	AlarmTypeService alarmTypeService;
	
	static Logger logger = Logger.getLogger(CarSpeedingAction.class.getName());
	@RequestMapping(value="/com/supermap/toCarSeepding")
	public String toRegion(HttpServletRequest request,HttpServletResponse response){
		logger.info("to carSeepList.jsp");
		return "region/carSeepList";
	}
	/**
	 * 超速列表
	 */
	@RequestMapping(value="/com/supermap/seepdingList")
	@ResponseBody
	public Map<String,Object>  seepdingList(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> result=new HashMap<String,Object>();
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		String eid=userEntity.getEid().getId();
		String speedName = request.getParameter("speedName");
		String speedStatus = request.getParameter("speedStatus");
		HashMap<String, Object> hm = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(eid)){
			hm.put("eid", eid);
		}
		if(StringUtils.isNotBlank(speedName)){
			hm.put("name", speedName);
		}
		if(StringUtils.isNotBlank(speedStatus)){
			hm.put("status", speedStatus);
		}
		try {
			//Page page = getPage(request);
			hm=getPageMap(request,hm);
			result = this.carSpeedingService.pageCarSpeeding(hm);
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 *   添加超速
	 * @param session
	 * @param request
	 * @param response 
	 * @return
	 */
	@RequestMapping(value="/com/supermap/addCarSeepding")
	@ResponseBody
	public AjaxResult addCarSeepding(HttpSession session,
			HttpServletRequest request,HttpServletResponse response){
		HashMap<String,Object> h = new HashMap<String, Object>();
		List<SpeedingSet> list =null;
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		String eId =userEntity.getEid().getId();
		String name = request.getParameter("seppName");
		h.put("speedName", name);
		h.put("eid", eId);
		if(name == null||"".equals(name)){
			return new AjaxResult((short)0,"请用户登录");
		}
		String value = request.getParameter("seepValue");
		//speed.doubleValue() < 1 || speed.doubleValue() > 300
		if(value == null||"".equals(value)){
			return new AjaxResult((short)0,"请填写超速值");
		}
		String status=request.getParameter("seepStatus");
		if(status == null||"".equals(status)){
			return new AjaxResult((short)0,"请选择状态");
		}
		String staTime=request.getParameter("seepStaTime");
		if(staTime == null||"".equals(staTime)){
			return new AjaxResult((short)0,"请填写开始时间");
		}
		String endTime=request.getParameter("seepEndTime");
		if(endTime == null||"".equals(endTime)){
			return new AjaxResult((short)0,"请填写结束时间");
		}
		String wookTIime=request.getParameter("wookTIime");
		if(wookTIime == null||"".equals(wookTIime)){
			return new AjaxResult((short)0,"请选择有效时间");
		}
		String remark=request.getParameter("remark");
		SpeedingSet	carSpeeding=new SpeedingSet();
		try {
			carSpeeding.setStatus(Short.parseShort(status));
			carSpeeding.setUserId(userEntity.getId());
			carSpeeding.setName(name);
			carSpeeding.setRemark(remark);
			carSpeeding.setWeek(wookTIime);
			Float fl = Float.valueOf(value);
			carSpeeding.setSpeed(fl);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date d =new Date();
			String dd =format.format(d);
			Date ddd =format.parse(dd);
			carSpeeding.setOperTime(ddd);
			carSpeeding.setStartTime(staTime);
			carSpeeding.setEndTime(endTime);
			carSpeeding.setEid(eId);
			list=this.carSpeedingService.queryCarSpeeding(h);
			if(list!=null && !list.isEmpty()){
				return new AjaxResult((short)0,"超速已经存在，请修改名称");
			}
			int addRet = this.carSpeedingService.addCarSpeeding(carSpeeding);
			if(addRet == 1){
				return new AjaxResult((short)1,"操作成功");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	/**
	 * 删除超速
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/delSpeeds")
	@ResponseBody
	public HashMap<String,Object> delSpeeds(HttpServletRequest request,HttpServletResponse response){
		String speedIds = request.getParameter("speedIds");
		HashMap<String,Object> m = new HashMap<String, Object>();
		int deleteRet=0;
		try {
			List<String> speedIdList = Arrays.asList(speedIds.split(","));
			for(String speedId:speedIdList){
				SpeedingSet carSpeeding =this.carSpeedingService.getCarSpeeding(speedId);
				if(carSpeeding == null){
					m.put("flag", "no");
					return null;
				}
				List<CarAlarmForeign> carAlarmForeign=this.carAlarmForeginService.getCarAlarmForeigeByForeignId(speedId);
				if(carAlarmForeign == null || carAlarmForeign.isEmpty()){
					deleteRet=this.carSpeedingService.delCarSpeeding(carSpeeding);
				}else {
					/*deleteRet=carAlarmForeginConsumer.delCarAlarmForeign(speedId, null);//删除和车辆关联
*/					deleteRet=this.carSpeedingService.delCarSpeeding(carSpeeding);
				}
			}
			if(deleteRet == 1){
				m.put("flag", "ok");
				return m;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	@RequestMapping(value="/com/supermap/editInit")
	@ResponseBody
	public SpeedingSet editInit(HttpServletRequest request,HttpServletResponse response){
		String speedId = request.getParameter("speedId");
		if(speedId == null||"".equals(speedId)){
			return null;
		}
		try {
			SpeedingSet	carSpeeding = this.carSpeedingService.getCarSpeeding(speedId);
			if(carSpeeding != null){
				return carSpeeding;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	/**
	 * 编辑超速报警
	* @Title: eidtSpeed
	* @return
	* String
	* @throws
	 */
	@RequestMapping(value="/com/supermap/updateCarSeepding")
	@ResponseBody
	public AjaxResult updateCarSeepding(HttpSession session,
			HttpServletRequest request,HttpServletResponse response){
		HashMap<String,Object> h = new HashMap<String, Object>();
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		List<SpeedingSet> list =null;
		String id=request.getParameter("seepId");
		String name = request.getParameter("seppName");
		if(name == null||"".equals(name)){
			return new AjaxResult((short)0,"请用输入名称");
		}
		String value = request.getParameter("seepValue");
		//speed.doubleValue() < 1 || speed.doubleValue() > 300
		if(value == null||"".equals(value)){
			return new AjaxResult((short)0,"请输入超速值");
		}
		String status=request.getParameter("seepStatus");
		if(status == null||"".equals(status)){
			return new AjaxResult((short)0,"请输选择状态");
		}
		String staTime=request.getParameter("seepStaTime");
		if(staTime == null||"".equals(staTime)){
			return new AjaxResult((short)0,"请输入开始时间");
		}
		String endTime=request.getParameter("seepEndTime");
		if(endTime == null||"".equals(endTime)){
			return new AjaxResult((short)0,"请输入结束时间");
		}
		String wookTIime=request.getParameter("wookTIime");
		if(wookTIime == null||"".equals(wookTIime)){
			return new AjaxResult((short)0,"请选择有效时间");
		}
		String remark=request.getParameter("remark");
		h.put("id", id);
		h.put("speedName", name);
		list=this.carSpeedingService.queryCarSpeeding(h);
		if(list!=null && !list.isEmpty()){//用户名已经存在
			return new AjaxResult((short)0,"已经存在");
		}
		SpeedingSet	carSpeeding=this.carSpeedingService.getCarSpeeding(id);
		try {
			carSpeeding.setEid(userEntity.getEid().getId());
			carSpeeding.setSpeed( Float.valueOf(value));
			carSpeeding.setUserId(userEntity.getId());
			carSpeeding.setStatus(Short.parseShort(status));
			carSpeeding.setName(name);
			carSpeeding.setRemark(remark);
			carSpeeding.setOperTime(new Date());
			carSpeeding.setStartTime(staTime);
			carSpeeding.setEndTime(endTime);
			carSpeeding.setWeek(wookTIime);
			int addRet = this.carSpeedingService.updateCarSpeeding(carSpeeding);
			if(addRet == 1){
				//创建成功
				return new AjaxResult((short)1,"操作成功");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	/**
	 * 获取超速已关联的车辆
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/com/supermap/getHasLinkedSeepCars")
	@ResponseBody
	public  List<HasLinkedCarBean>  getHasLinkedCars(HttpServletRequest request,HttpServletResponse response){
		String speedId =request.getParameter("speedId");
		if(StringUtils.isBlank(speedId)){
			return null;
		}
		HashMap<String, Object> hm = new HashMap<String, Object>();
		hm.put("foreignIds", speedId);
		//Page page = getPage(request);
		hm=getPageMap(request,hm);
		try {
			List<HasLinkedCarBean> cars = new ArrayList<HasLinkedCarBean>();
			List<CarDept> carDepts =this.carService.queryCar(hm);
			if(carDepts == null || carDepts.isEmpty()){
				HasLinkedCarBean bean = new HasLinkedCarBean();
				bean.setId("123");
				bean.setDeptName("无关联部门");
				bean.setLicense("无关联车辆");
				cars.add(bean);
				return cars;
			}
			for(CarDept carDept:carDepts){
				HasLinkedCarBean bean = new HasLinkedCarBean();
				bean.setId(carDept.getId());
				bean.setLicense(carDept.getLicense());
				bean.setDeptName(carDept.getDeptName());
				cars.add(bean);
			}
			return cars;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	/**
	 * 车辆关联超速报警
	* @Title: saveSelectedCars
	* @return
	* String
	* @throws
	 */
	@RequestMapping(value="/com/supermap/saveSelectedSeepCars")
	@ResponseBody
	public AjaxResult  saveSelectedCars(HttpServletRequest request,HttpServletResponse response){
		String speedId =request.getParameter("speedId"); //超速Id
		String carIds = request.getParameter("carIds"); //车辆id
		if(StringUtils.isBlank(speedId)){
			return new AjaxResult((short)0,"请用户登录");
		}
		int addRet=0;
		try {
			SpeedingSet carSpeeding =this.carSpeedingService.getCarSpeeding(speedId);
			if(carSpeeding == null){
				return new AjaxResult((short)0,"超速信息不存在");
			}
			String[] carIdArray = {};
			if(StringUtils.isNotBlank(carIds)){
				carIdArray = carIds.split(",");
			}
			//得到已经关联的超速车辆
			List<CarAlarmForeign> list = this.carAlarmForeginService.getCarAlarmForeigeByForeignId(speedId);
			List<String> addCarIds = new ArrayList<String>(Arrays.asList(carIdArray));
			if(list != null && list.size() > 0){
				for(CarAlarmForeign foreign:list){
					if(addCarIds.contains(foreign.getCarId())){//选择的carIds 在数据库存在 则不再添加
						addCarIds.remove(foreign.getCarId());
					}else{//选择的carIds 不包含数据库中已存在的，则删除数据库中的记录
						int delRet = this.carAlarmForeginService.delCarAlarmForeign(foreign);
							//carAlarmForeginConsumer().delCarAlarmForeign(foreign);
						if(delRet == 0){
							return new AjaxResult((short)0,"没有选择车辆");
						}
					}
				}
			}
			String alarmId=	this.alarmTypeService.getAlarmTypeByCode(303).getId();
			if(addCarIds!=null && !addCarIds.isEmpty()){//删除关联
				for(String carId:addCarIds){
					CarAlarmForeign carAlarmForeign = new CarAlarmForeign();
					carAlarmForeign.setCarId(carId);
					carAlarmForeign.setForeignId(speedId);
					carAlarmForeign.setType(alarmId);
					addRet= this.carAlarmForeginService.addCarAlarmForeign(carAlarmForeign);
				}
				if(addRet == 0){
					return null;
				}
				return new AjaxResult((short)1,"操作成功");
			}else{
				return new AjaxResult((short)1,"操作成功");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
		
	}
	
}
