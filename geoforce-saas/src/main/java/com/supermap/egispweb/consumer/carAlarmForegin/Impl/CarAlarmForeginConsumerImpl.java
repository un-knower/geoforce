package com.supermap.egispweb.consumer.carAlarmForegin.Impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispweb.consumer.carAlarmForegin.CarAlarmForeginConsumer;
import com.supermap.lbsp.provider.hibernate.lbsp.CarAlarmForeign;
import com.supermap.lbsp.provider.service.carAlarmForegin.CarAlarmForeginService;



@Component("carAlarmForeginConsumer")
public class CarAlarmForeginConsumerImpl implements CarAlarmForeginConsumer {
	
	@Reference(version="2.5.3")
	private CarAlarmForeginService carAlarmForeginService;

	@Override
	public int addCarAlarmForeign(CarAlarmForeign carAlarmForeign) {
		// TODO Auto-generated method stub
		return this.carAlarmForeginService.addCarAlarmForeign(carAlarmForeign);
	}

	@Override
	public int delCarAlarmForeign(CarAlarmForeign carAlarmForeign) {
		// TODO Auto-generated method stub
		return this.carAlarmForeginService.delCarAlarmForeign(carAlarmForeign);
	}

	@Override
	public int delCarAlarmForeign(String foreignId, String carId) {
		// TODO Auto-generated method stub
		return this.carAlarmForeginService.delCarAlarmForeign(foreignId, carId);
	}

	@Override
	public List<CarAlarmForeign> getCarAlarmForeigeByForeignId(String foreignId) {
		// TODO Auto-generated method stub
		return this.carAlarmForeginService.getCarAlarmForeigeByForeignId(foreignId);
	}

	@Override
	public List<CarAlarmForeign> getCarAlarmForeignByCarId(String carId) {
		// TODO Auto-generated method stub
		return this.carAlarmForeginService.getCarAlarmForeignByCarId(carId);
	}

	@Override
	public Long getCarAlarmForeignCount(String carId, String foreignId) {
		// TODO Auto-generated method stub
		return this.carAlarmForeginService.getCarAlarmForeignCount(carId, foreignId);
	}

	@Override
	public int saveOrUpdateCarAlarmForeign(CarAlarmForeign carAlarmForeign) {
		// TODO Auto-generated method stub
		return this.carAlarmForeginService.saveOrUpdateCarAlarmForeign(carAlarmForeign);
	}

	@Override
	public int updateCarAlarmForeign(CarAlarmForeign carAlarmForeign) {
		// TODO Auto-generated method stub
		return this.carAlarmForeginService.updateCarAlarmForeign(carAlarmForeign);
	}
	
	

}
