package com.supermap.egispservice.lbs.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.egispservice.lbs.dao.AlarmTypeDao;
import com.supermap.egispservice.lbs.entity.AlarmType;

@Service("baseService")
public class BaseService {
	public static HashMap<String, String> ALARM_MAP ;//报警类型map

	@Autowired
	AlarmTypeDao alarmTypeDao;
	
	public HashMap<String, String> getALARM_MAP() {
		ALARM_MAP = new HashMap<String, String>();
		List<AlarmType> list = this.alarmTypeDao.findAll();
		if(list != null ){
			for(AlarmType alarmType:list){
				ALARM_MAP.put(alarmType.getCode(), alarmType.getName());
			}
		}
		return ALARM_MAP;
	}

	public static void setALARM_MAP(HashMap<String, String> aLARM_MAP) {
		ALARM_MAP = aLARM_MAP;
	}
	
}
