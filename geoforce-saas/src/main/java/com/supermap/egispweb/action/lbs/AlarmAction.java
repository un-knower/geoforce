package com.supermap.egispweb.action.lbs;

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
import com.supermap.egispservice.lbs.entity.AlarmType;
import com.supermap.egispservice.lbs.pojo.CarAlarm;
import com.supermap.egispservice.lbs.service.AlarmService;
import com.supermap.egispservice.lbs.service.AlarmTypeService;
import com.supermap.egispservice.lbs.util.Pagination;
import com.supermap.egispweb.util.DateUtil;


@Controller
public class AlarmAction  extends BaseAction{
	
	@Autowired
	AlarmTypeService alarmTypeService;
	
	@Autowired
	AlarmService alarmService;
	
	
	static Logger logger = Logger.getLogger(AlarmAction.class.getName());
	
	
	@RequestMapping(value="/com/supermap/toAlerm")
	public String toRegion(HttpServletRequest request,HttpServletResponse response){
		HashMap<String, Object> hm = new HashMap<String, Object>();
		List<AlarmType>  queryAlarmType=this.alarmTypeService.queryAlarmType(hm);
		request.setAttribute("lists", queryAlarmType);
		logger.info("to alarm.jsp");
		return "alarm/alarm";
	}
	
	@RequestMapping(value="/com/supermap/alermList")
	@ResponseBody
	public Map<String,Object>  list(HttpServletRequest request,HttpSession session){
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
		String deptCode =userEntity.getDeptId().getCode(); //getUserSession().getDeptCode();
		HashMap<String, Object> hm = new HashMap<String, Object>();
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
		Pagination  page = getPage(request);
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
	 * 批量处理 已读未读
	 * @return
	 */
	@RequestMapping(value="/com/supermap/alarmHandl")
	@ResponseBody
	public HashMap<String,Object> alarmHandl(HttpSession session,HttpServletRequest request,HttpServletResponse response){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		HashMap<String,Object> m = new HashMap<String, Object>();
		String opinionType = request.getParameter("opinionType");//cl 处理；yd 已读；
		String alarmIds = request.getParameter("alarmId");
		String alarmTimes = request.getParameter("alarmTimes");
		String opinion=request.getParameter("opinion");//得到处理意见
		if(alarmIds == null || alarmIds.trim().equals("")){
			m.put("flag", "02");
			return m;
		}
		if(alarmTimes == null || alarmTimes.trim().equals("")){
			m.put("flag", "02");
			return m;
		}
		if(opinionType.equals("YD")){
			opinion="已读";	
		}
		int reg=0;
		try {
			String[] alarmIdArray = alarmIds.split(",");
			String[] alarmTimeArray = alarmTimes.split(",");
			for (int i = 0; i < alarmIdArray.length; i++) {
				Date alarmTime = DateUtil.formatStringToDate(alarmTimeArray[i], "yyyy-MM-dd");
				CarAlarm carAlarm=this.alarmService.getCarAlarm(alarmIdArray[i],alarmTime);
				if(carAlarm!= null){
					carAlarm.setStatus((short)1);
					carAlarm.setDealDate(new Date());
					carAlarm.setUserId(userEntity.getId());
					carAlarm.setOpinion(opinion);
					reg=this.alarmService.updateCarAlarm(carAlarm);
				}
				
			}
			if(reg==1){
				m.put("flag", "ok");
				return m;
			}
		} catch (Exception e) {
			logger.error("deal alarm fail, due to",e);
		}
		return null;
	}
	
}