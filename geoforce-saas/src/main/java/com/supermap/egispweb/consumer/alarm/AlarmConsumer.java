package com.supermap.egispweb.consumer.alarm;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.supermap.egispweb.common.AjaxResult;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.gps.CarAlarm;

public interface AlarmConsumer {
	
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
	public Page queryCarAlarmByReport(String month, Page page,HashMap<String, Object> hm);
	public List<CarAlarm> queryCarAlarmByReport(String month, HashMap<String, Object> hm);

	
	public int updateCarAlarm(CarAlarm carAlarm);
	
	public List<CarAlarm> queryCarAlarm(String month, Page page,
			HashMap<String, Object> hm);
	
	public CarAlarm getCarAlarm(String id,Date alarmDate);
	/**
	 * 
	* 方法名: queryAlarmRemind
	* 描述: 报警提醒 查询报警信息
	* @param deptCode
	* @param status
	* @return
	 */
	public AjaxResult queryAlarmRemind(String deptCode,short status);


}
