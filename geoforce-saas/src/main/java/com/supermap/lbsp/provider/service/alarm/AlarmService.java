package com.supermap.lbsp.provider.service.alarm;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.gps.CarAlarm;



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
	public Page queryCarAlarmByReport(String month, Page page,
			HashMap<String, Object> hm);
	
	public List<CarAlarm> queryCarAlarmByReport(String month, HashMap<String, Object> hm);
	/**
	 * 
	* 方法名: queryCarAlarmByRemind
	* 描述: 车辆监控报警提醒
	* @param hm
	* @param page
	* @return
	 */
	public List<CarAlarm> queryCarAlarmByRemind(HashMap<String, Object> hm,Page page);
	
	
	
	public List<CarAlarm> queryCarAlarm(String month, Page page,
			HashMap<String, Object> hm);
	public int updateCarAlarm(CarAlarm carAlarm);
	public CarAlarm getCarAlarm(String id,Date alarmDate);
	
	
}
