package com.supermap.egispservice.lbs.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.supermap.egispservice.lbs.pojo.CarAlarm;
import com.supermap.egispservice.lbs.util.Pagination;


public interface AlarmDao {
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
	public List<CarAlarm> queryCarAlarmByReport(String month, Pagination page,
			HashMap<String, Object> hm);
	/**
	 * 
	* 方法名: getCarAlarmLastTable
	* 描述: 获取最新报警表名称 按月分表
	* @return
	 */
	public String getCarAlarmLastTable();
	
	public int updateCarAlarm(CarAlarm carAlarm);
	
	
	public CarAlarm getCarAlarm(String id,Date alarmDate);
}
