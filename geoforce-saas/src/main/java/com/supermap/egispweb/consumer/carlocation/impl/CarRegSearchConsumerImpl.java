package com.supermap.egispweb.consumer.carlocation.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispweb.common.AjaxResult;
import com.supermap.egispweb.consumer.carlocation.CarRegSearchConsumer;
import com.supermap.lbsp.provider.hibernate.gps.CarGps;
import com.supermap.lbsp.provider.service.carlocation.CarRegSearchService;

@Component("carRegSearchConsumer")
public class CarRegSearchConsumerImpl implements CarRegSearchConsumer{
	static Logger logger = Logger.getLogger(CarRegSearchConsumerImpl.class.getName());
	
	@Reference(version="2.5.3")
	private CarRegSearchService carRegSearchService;
	@Override
	public AjaxResult carRegSearch(String lngLats,String mapType,
			String deptCode) {
		
		if(StringUtils.isBlank(lngLats)){
			return new AjaxResult((short)0, "无效的区域");
		}
		if(StringUtils.isBlank(mapType)){
			mapType = "supermap";
		}
		//区域范围：两个经纬度之间","分隔，经度和纬度之间","分隔
		String[] regionArray = lngLats.split(";");
		if (regionArray.length < 3){
			return new AjaxResult((short)0, "无效的区域");
		}
		try {
			List<CarGps> list = carRegSearchService.queryCarsByRegion(lngLats,mapType,deptCode);
			if(list == null || list.isEmpty()){
				return new AjaxResult((short)0,"区域内无车辆");
			}
			return new AjaxResult((short)1,list);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new AjaxResult((short)0,"区域内无车辆");
		}
	}

}
