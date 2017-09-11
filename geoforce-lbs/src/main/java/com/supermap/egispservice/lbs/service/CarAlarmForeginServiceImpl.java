package com.supermap.egispservice.lbs.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispservice.lbs.dao.CarAlarmForeignDao;
import com.supermap.egispservice.lbs.entity.CarAlarmForeign;
import com.supermap.egispservice.lbs.util.BeanTool;

@Transactional
@Service("carAlarmForeginService")
public class CarAlarmForeginServiceImpl implements CarAlarmForeginService {
	
	@Autowired
	CarAlarmForeignDao carAlarmForeignDao;
	
	@Override
	public int addCarAlarmForeign(CarAlarmForeign carAlarmForeign) {
		this.carAlarmForeignDao.save(carAlarmForeign);
		return 1;
	}

	@Override
	public int saveOrUpdateCarAlarmForeign(CarAlarmForeign carAlarmForeign) {
		if(carAlarmForeign!=null&&carAlarmForeign.getId()!=null){
			this.updateCarAlarmForeign(carAlarmForeign);
		}else this.addCarAlarmForeign(carAlarmForeign);
		return 1;
	}

	@Override
	public int updateCarAlarmForeign(CarAlarmForeign carAlarmForeign) {
		CarAlarmForeign carAlarmForeign2=this.carAlarmForeignDao.findByid(carAlarmForeign.getId());
		BeanUtils.copyProperties(carAlarmForeign,carAlarmForeign2,BeanTool.getNullPropertyNames(carAlarmForeign));
		this.carAlarmForeignDao.save(carAlarmForeign2);
		return 1;
	}

	@Override
	public int delCarAlarmForeign(CarAlarmForeign carAlarmForeign) {
		this.carAlarmForeignDao.delete(carAlarmForeign);
		return 1;
	}

	@Override
	public int delCarAlarmForeign(String foreignId, String carId) {
		CarAlarmForeign carAlarmForeign=this.carAlarmForeignDao.findByCarIdAndForeignId(carId, foreignId);
		if(carAlarmForeign!=null){
			this.carAlarmForeignDao.delete(carAlarmForeign);
		}
		return 1;
	}

	@Override
	public List<CarAlarmForeign> getCarAlarmForeigeByForeignId(String foreignId) {
		return this.carAlarmForeignDao.findByforeignId(foreignId);
	}

	@Override
	public List<CarAlarmForeign> getCarAlarmForeignByCarId(String carId) {
		return this.carAlarmForeignDao.findBycarId(carId);
	}

	@Override
	public Long getCarAlarmForeignCount(String carId, String foreignId) {
		return this.carAlarmForeignDao.getCountBycarIdforeignId(carId, foreignId);
	}

}
