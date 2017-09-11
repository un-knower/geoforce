package com.supermap.egispweb.consumer.carlocation.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispweb.common.AjaxResult;
import com.supermap.egispweb.consumer.carlocation.CarLocationConsumer;
import com.supermap.lbsp.provider.hibernate.gps.CarGps;
import com.supermap.lbsp.provider.service.carlocation.CarLocationService;

@Component("carLocationConsumer")
public class CarLocationConsumerImpl implements CarLocationConsumer{
	static Logger logger = Logger.getLogger(CarLocationConsumerImpl.class.getName());

	@Reference(version="2.5.3")
	private CarLocationService carLocationService;
	
	
	public AjaxResult carLocation(String carIds) {
		
		if(StringUtils.isBlank(carIds)){
			
			return new AjaxResult((short)0, "未找到对应车辆");
		}
		String[] carArray = carIds.split(",");
		List<String> carList = Arrays.asList(carArray);
		try {
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("carIds", carList);
			List<CarGps> list = carLocationService.carLocation(hm);
			if(list == null || list.isEmpty()){
				
				return new AjaxResult((short)0, "未找到对应车辆");
			}
			
			return new AjaxResult((short)1,list);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new AjaxResult((short)0, "定位失败");
	}

}
