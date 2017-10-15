package com.supermap.egispweb.action.lbs;

import java.util.ArrayList;
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

import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.service.InfoDeptService;
import com.supermap.egispservice.lbs.entity.AlarmType;
import com.supermap.egispservice.lbs.pojo.CarAlarm;
import com.supermap.egispservice.lbs.service.AlarmService;
import com.supermap.egispservice.lbs.service.AlarmTypeService;
import com.supermap.egispservice.lbs.util.Pagination;
import com.supermap.egispweb.pojo.export.Alarm;
import com.supermap.egispweb.util.DateUtil;


//@Controller
public class AlarmReportAcion extends BaseAction{
	static Logger logger = Logger.getLogger(AlarmReportAcion.class.getName());
	
	@Autowired
	AlarmTypeService alarmTypeService;
	
	@Autowired
	InfoDeptService infoDeptService;
	
	@Autowired
	AlarmService alarmService;
	
	/**
	 * 历史轨迹初始化
	* @Title: init
	* @return
	* String
	* @throws
	 */
	@RequestMapping(value="/com/supermap/toAlarmList")
	public String init(HttpServletRequest request){
		String statTime = DateUtil.getMonthFirstDay("yyyy-MM-dd");
		String endTime = DateUtil.getMonthLastDay("yyyy-MM-dd");
		request.setAttribute("statTime", statTime);
		request.setAttribute("endTime", endTime);
		HashMap<String, Object> hm = new HashMap<String, Object>();
		List<AlarmType>  queryAlarmType=this.alarmTypeService.queryAlarmType(hm);
		request.setAttribute("lists", queryAlarmType);
		return "report/alarmReport";
	}
	/**
	 *  得到列表
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/com/supermap/alarmList")
	@ResponseBody
	public Map<String,Object> list(HttpServletRequest request,HttpSession session){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		String status = request.getParameter("status");
		String license = request.getParameter("license");
		String startDate = request.getParameter("starttime");//开始时间和结束时间处理
		String endDate = request.getParameter("endtime");
		String difTime = request.getParameter("difTime");
		String typeId = request.getParameter("typeId");
		String deptId = request.getParameter("deptId");
		String deptCode =userEntity.getDeptId().getCode(); 
		HashMap<String, Object> hm = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(deptId)){
			InfoDeptEntity dept = this.infoDeptService.findDeptById(deptId);
			if(dept != null){
				deptCode = this.infoDeptService.findDeptById(deptId).getCode();
				logger.info("deptId is "+deptId);
			}
		}
		if(StringUtils.isNotBlank(deptCode)){
			hm.put("deptCode", deptCode);
		}
		if(StringUtils.isNotBlank(status )){
			hm.put("status", status);
		}
		if(StringUtils.isNotBlank(difTime)){
			hm.put("difTime", difTime);
		}
		if(StringUtils.isBlank(startDate)||StringUtils.isBlank(endDate)){
			return builderResult(null,"查询条件：时间不能不能为空");
		}
		Date startTime = DateUtil.formatStringByDate(startDate+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
		hm.put("startDate", startTime);
		Date endTime = DateUtil.formatStringByDate(endDate+" 23:59:59", "yyyy-MM-dd HH:mm:ss");
		hm.put("endDate", endTime);
		
		if(StringUtils.isNotBlank(license)){
			hm.put("license", license.trim().toUpperCase());
		}
		if(StringUtils.isNotBlank(typeId)){
			hm.put("typeId", typeId);
		}
		Pagination page = getPage(request);
		String month = getQueryMonth(startTime,endTime);
		page = this.alarmService.queryCarAlarmByReportByPage(month, page, hm);
		Map<String,Object> map=builderResult(page,null);
		return map;
	}
	
	
	
	/**
	 * 根据开始时间和结束时间获取查询的报警月表
	* @Title: getQueryMonth
	* @param startTime
	* @param endTime
	* @return
	* String
	* @throws
	 */
	public String getQueryMonth(Date startTime,Date endTime){
		String month = "";//报警信息按月分表，如果有开始时间则按开始时间月，如果有结束时间按结束时间 否则按当前月
		if(startTime != null){
			month = DateUtil.formatDateByFormat(startTime, "yyyyMM");
		}else if(endTime != null){
			month = DateUtil.formatDateByFormat(endTime, "yyyyMM");
		}else {
			month = DateUtil.formatDateByFormat(new Date(), "yyyyMM");
		}
		return month;
	}
	
	/**
	 * 导出报表
	 * @param request
	 * @param session
	 * @param response
	 */
	@RequestMapping(value="/com/supermap/alarmExport")
	public void export(HttpServletRequest request,HttpSession session,HttpServletResponse response){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return;
		}
		String status = request.getParameter("status");
		String license = request.getParameter("license");
		String deptCode =userEntity.getDeptId().getCode(); //getUserSession().getDeptCode();
		String startDate = request.getParameter("startTime");//开始时间和结束时间处理
		String endDate = request.getParameter("endTime");
		String difTime = request.getParameter("difTime");
		String typeId = request.getParameter("typeId");
		
		HashMap<String, Object> hm = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(deptCode)){
			hm.put("deptCode", deptCode);
		}
		if(StringUtils.isNotBlank(status)){
			hm.put("status", status);
		}
		if(StringUtils.isNotBlank(difTime)){
			hm.put("difTime", difTime);
		}
		if(StringUtils.isBlank(startDate)||StringUtils.isBlank(endDate)){
			return ;
		}
		Date startTime = DateUtil.formatStringByDate(startDate+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
		hm.put("startDate", startTime);
		Date endTime = DateUtil.formatStringByDate(endDate+" 23:59:59", "yyyy-MM-dd HH:mm:ss");
		hm.put("endDate", endTime);
		
		if(StringUtils.isNotBlank(license)){
			hm.put("license", license.trim().toUpperCase());
		}
		if(StringUtils.isNotBlank(typeId)){
			hm.put("typeId", typeId);
		}
		String month = getQueryMonth(startTime,endTime);
		List<CarAlarm> carAlarmList = this.alarmService.queryCarAlarmByReport(month, hm);
		if(carAlarmList == null || carAlarmList.size()==0){
			return ;
		}
		String filename = "报警报表"+System.currentTimeMillis()+".xls";
		exportExcel(getExportAlarm(carAlarmList), filename,"exportFence", startDate ,response, request);
				
		return ;
	}
	
	/**
	 * 封装导出对象
	 * @param list
	 * @return
	 */
	private List<Alarm> getExportAlarm(List<CarAlarm> list){
		List<Alarm> alarmList = new ArrayList<Alarm>();
		for (int i = 0; i < list.size(); i++) {
			Alarm alarm = new Alarm();
			CarAlarm carAlarm = list.get(i);
			alarm.setAddr(carAlarm.getAddr());
			alarm.setAlarmDate(carAlarm.getAlarmDate());
			alarm.setAlarmName(carAlarm.getTypeName());
			alarm.setCarLicense(carAlarm.getCarLicense());
			alarm.setDealDate(carAlarm.getDealDate());
			alarm.setDeptName(carAlarm.getDeptName());
			alarm.setDifTime(carAlarm.getDifTime());
			alarm.setSpeed(carAlarm.getSpeed());
			alarm.setStautsName(carAlarm.getStautsName());
			alarm.setTypeName(carAlarm.getTypeName());
			alarm.setUserName(carAlarm.getUserName());
			alarmList.add(alarm);
		}
		return alarmList;
	}
	
}
