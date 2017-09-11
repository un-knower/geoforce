package com.supermap.egispweb.consumer.alarm.impl;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispweb.consumer.alarm.AlarmTypeConsumer;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.AlarmType;
import com.supermap.lbsp.provider.service.alarm.AlarmService;
import com.supermap.lbsp.provider.service.alarm.AlarmTypeService;



@Component("alarmTypeConsumer")
public class AlarmTypeConsumerImpl implements AlarmTypeConsumer {
	static Logger logger = Logger.getLogger(AlarmConsumerImpl.class.getName());
	@Reference(version="2.5.3")
	private AlarmTypeService alarmTypeService;
	@Override
	public List<AlarmType> queryAlarmType(HashMap<String, Object> hm) {
		// TODO Auto-generated method stub
		return this.alarmTypeService.queryAlarmType(hm);
	}
	@Override
	public AlarmType getAlarmType(String id) {
		// TODO Auto-generated method stub
		return this.alarmTypeService.getAlarmType(id);
	}
	@Override
	public AlarmType getAlarmTypeByCode(Integer code) {
		// TODO Auto-generated method stub
		return this.alarmTypeService.getAlarmTypeByCode(code);
	}

	
}
