package com.supermap.egispservice.lbs.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.supermap.egispservice.lbs.pojo.CarAlarm;
import com.supermap.egispservice.lbs.util.Pagination;


public interface AlarmService {
	/**
	 * 报表查询报警
	* @Title: queryCarAlarmByReport
	* @param month
	* @param page
	* @param hm
	* @return
	* @throws Exception
	* List<CarAlarm>
	* @throws
	 */
	public Pagination queryCarAlarmByReportByPage(String month,Pagination page,HashMap<String, Object> hm);
	
	
	public List<CarAlarm> queryCarAlarmByReport(String month, HashMap<String, Object> hm);
	/**
	 * 
	* 方法名: queryCarAlarmByRemind
	* 描述: 车辆监控报警提醒
	* @param hm
	* @param page
	* @return
	 */
	public List<CarAlarm> queryCarAlarmByRemind(HashMap<String, Object> hm,Pagination page) throws Exception;
	
	public CarAlarm loadCarAlarm(CarAlarm carAlarm);
	
	public int updateCarAlarm(CarAlarm carAlarm);
	
	
	public CarAlarm getCarAlarm(String id,Date alarmDate);
	
	public List<CarAlarm> queryCarAlarm(String month,HashMap<String, Object> hm,Pagination page);
	
}
