package com.supermap.egispweb.consumer.car.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispweb.consumer.car.CarDisMsgConsumer;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.CarDismsg;
import com.supermap.lbsp.provider.service.car.CarDisMsgService;


@Component("carDisMsgConsumer")
public class CarDisMsgConsumerImpl implements  CarDisMsgConsumer{
	@Reference(version="2.5.3")
	private CarDisMsgService carDisMsgService;

	@Override
	public int addCarMessage(CarDismsg carMessage) {
		// TODO Auto-generated method stub
		return carDisMsgService.addCarMessage(carMessage);
	}

	@Override
	public int delCarMessage(CarDismsg carDismsg) {
		// TODO Auto-generated method stub
		return carDisMsgService.delCarMessage(carDismsg);
	}

	@Override
	public CarDismsg getCarMessage(String id) {
		// TODO Auto-generated method stub
		return carDisMsgService.getCarMessage(id);
	}

	@Override
	public List<Object[]> queryCarMessage(Page page, HashMap hm) {
		// TODO Auto-generated method stub
		return carDisMsgService.queryCarMessage(page, hm);
	}
	public Page queryCarMessagePage(Page page, HashMap hm){
		return carDisMsgService.queryCarMessagePage(page, hm);
	}
	@Override
	public int updateCarMessage(CarDismsg carDismsg) {
		// TODO Auto-generated method stub
		return carDisMsgService.updateCarMessage(carDismsg);
	}
}
