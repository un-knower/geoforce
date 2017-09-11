package com.supermap.egispservice.lbs.service;

import java.util.HashMap;
import java.util.List;

import com.supermap.egispservice.lbs.entity.AlarmType;


public interface AlarmTypeService {

	/**查询报警类型
	 * 
	 * @return
	 */
	public List<AlarmType> queryAlarmType(HashMap<String,Object> hm);

	/**
	 * 主键id获取报警类型
	* @throws
	 */
	public AlarmType getAlarmType(String id);

	/**
	 * 通过code查询报警类型
	* @Title: getAlarmTypeByCode
	* @param code
	* @return
	* AlarmType
	* @throws
	 */
	public AlarmType getAlarmTypeByCode(Integer code);

}
