package com.supermap.egispweb.consumer.alarm.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispweb.common.AjaxResult;
import com.supermap.egispweb.consumer.alarm.AlarmConsumer;
import com.supermap.egispweb.pojo.AlarmRemind;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.gps.CarAlarm;
import com.supermap.lbsp.provider.service.alarm.AlarmService;

@Component("alarmConsumer")
public class AlarmConsumerImpl implements AlarmConsumer {
	static Logger logger = Logger.getLogger(AlarmConsumerImpl.class.getName());
	@Reference(version="2.5.3")
	private AlarmService alarmService;
	@Override
	public Page queryCarAlarmByReport(String month, Page page,
			HashMap<String, Object> hm) {
		// TODO Auto-generated method stub
		return alarmService.queryCarAlarmByReport(month, page, hm);
	}
	@Override
	public List<CarAlarm> queryCarAlarmByReport(String month,
			HashMap<String, Object> hm) {
		// TODO Auto-generated method stub
		return alarmService.queryCarAlarmByReport(month, hm);
	}
	@Override
	public CarAlarm getCarAlarm(String id, Date alarmDate) {
		// TODO Auto-generated method stub
		return this.alarmService.getCarAlarm(id, alarmDate);
	}
	@Override
	public List<CarAlarm> queryCarAlarm(String month, Page page,
			HashMap<String, Object> hm) {
		// TODO Auto-generated method stub
		return this.alarmService.queryCarAlarm(month, page, hm);
	}
	@Override
	public int updateCarAlarm(CarAlarm carAlarm) {
		// TODO Auto-generated method stub
		return this.alarmService.updateCarAlarm(carAlarm);
		
	}
	public AjaxResult queryAlarmRemind(String deptCode,short status){
		if(StringUtils.isBlank(deptCode)){
			return new AjaxResult((short)0,"无报警信息");
		}
		try {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("deptCode", deptCode);
			hm.put("status", String.valueOf(status));
			Page page = new Page();
			page.setPageClick(1);
			page.setPageSize(1);
			page.setCurrentPageNum(1);
			
			List<CarAlarm> list = alarmService.queryCarAlarmByRemind(hm, page);
			if(list == null || list.isEmpty())
				return new AjaxResult((short)0,"无报警信息");
			CarAlarm alarm = list.get(0);
			AlarmRemind info = new AlarmRemind();
			info.setCount(page.getTotalNum());
			info.setAlarmTime(alarm.getAlarmDateStr());
			info.setAddr(alarm.getAddr());
			info.setAlarmType(alarm.getTypeName());
			info.setLicense(alarm.getCarLicense());
			info.setOthers(alarm.getOthers());
			info.setTemCode(alarm.getTemCode());
			
			return new AjaxResult((short)1, info);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new AjaxResult((short)0, "无报警信息");
	}
}
