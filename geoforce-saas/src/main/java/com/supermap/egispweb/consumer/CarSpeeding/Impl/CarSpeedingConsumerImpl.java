package com.supermap.egispweb.consumer.CarSpeeding.Impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;

import com.supermap.egispweb.consumer.CarSpeeding.CarSpeedingConsumer;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.SpeedingSet;
import com.supermap.lbsp.provider.service.speeding.CarSpeedingService;


@Component("carSpeedingConsumer")
public class CarSpeedingConsumerImpl implements CarSpeedingConsumer {
	
	@Reference(version="2.5.3")
	private CarSpeedingService carSpeedingService;

	@Override
	public int addCarSpeeding(SpeedingSet carSpeeding) {
		// TODO Auto-generated method stub
		return this.carSpeedingService.addCarSpeeding(carSpeeding);
	}

	@Override
	public int delCarSpeeding(SpeedingSet carSpeeding) {
		// TODO Auto-generated method stub
		return this.carSpeedingService.delCarSpeeding(carSpeeding);
	}

	@Override
	public SpeedingSet getCarSpeeding(String id) {
		// TODO Auto-generated method stub
		return this.carSpeedingService.getCarSpeeding(id);
	}

	@Override
	public SpeedingSet getCarSpeedingBySpeed(String speed) {
		// TODO Auto-generated method stub
		return this.carSpeedingService.getCarSpeedingBySpeed(speed);
	}

	@Override
	public int hasName(String name, String eid) {
		// TODO Auto-generated method stub
		return this.carSpeedingService.hasName(name, eid);
	}

	@Override
	public List<SpeedingSet> queryCarSpeeding(Page page, HashMap hm) {
		// TODO Auto-generated method stub
		return this.carSpeedingService.queryCarSpeeding(page, hm);
	}

	@Override
	public int updateCarSpeeding(SpeedingSet carSpeeding) {
		// TODO Auto-generated method stub
		return this.carSpeedingService.updateCarSpeeding(carSpeeding);
	}

	@Override
	public Page pageCarSpeeding(Page page, HashMap<String, Object> hm) {
		// TODO Auto-generated method stub
		return this.carSpeedingService.pageCarSpeeding(page, hm);
	}
	
	

}
