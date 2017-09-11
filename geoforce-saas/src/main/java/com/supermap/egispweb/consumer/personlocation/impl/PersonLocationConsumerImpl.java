package com.supermap.egispweb.consumer.personlocation.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispweb.common.AjaxResult;
import com.supermap.egispweb.consumer.personlocation.PersonLocationConsumer;
import com.supermap.lbsp.provider.hibernate.gps.PersonGps;
import com.supermap.lbsp.provider.service.personlocation.PersonLocationService;

@Component("personLocationConsumer")
public class PersonLocationConsumerImpl implements PersonLocationConsumer{
	static Logger logger = Logger.getLogger(PersonLocationConsumerImpl.class.getName());
	
	@Reference(version="2.5.3")
	private PersonLocationService personLocationService;

	@Override
	public AjaxResult personLocation(String personIds) {
		if(StringUtils.isBlank(personIds)){
			
			return new AjaxResult((short)0, "未找到对应人员");
		}
		String[] personArray = personIds.split(",");
		List<String> personList = Arrays.asList(personArray);
		try {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("personIds", personList);
			List<PersonGps> list = personLocationService.personLocation(hm);
			if(list == null || list.isEmpty()){
				
				return new AjaxResult((short)0, "无位置信息");
			}
			
			return new AjaxResult((short)1,list);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new AjaxResult((short)0, "定位失败");
	}
}
