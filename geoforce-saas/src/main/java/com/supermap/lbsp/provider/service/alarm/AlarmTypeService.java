package com.supermap.lbsp.provider.service.alarm;

import java.util.HashMap;
import java.util.List;

import com.supermap.lbsp.provider.hibernate.lbsp.AlarmType;



public interface AlarmTypeService {
	/**查询报警类型
	 * 
	 * @return
	 */
	public List<AlarmType> queryAlarmType(HashMap<String,Object> hm);
	
	public AlarmType getAlarmType(String id);
	public AlarmType getAlarmTypeByCode(Integer code);
}