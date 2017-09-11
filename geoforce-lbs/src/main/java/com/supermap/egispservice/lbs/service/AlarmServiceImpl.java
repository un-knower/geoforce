package com.supermap.egispservice.lbs.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.service.InfoDeptService;
import com.supermap.egispservice.base.service.UserService;
import com.supermap.egispservice.lbs.dao.AlarmDao;
import com.supermap.egispservice.lbs.entity.AlarmType;
import com.supermap.egispservice.lbs.entity.Car;
import com.supermap.egispservice.lbs.pojo.CarAlarm;
import com.supermap.egispservice.lbs.util.DateUtil;
import com.supermap.egispservice.lbs.util.Pagination;

@Transactional
@Service("alarmService")
public class AlarmServiceImpl implements AlarmService {
	
	@Autowired
	AlarmDao alarmDao;
	
	@Autowired
	CarService carService;
	
	@Autowired
	InfoDeptService infoDeptService;
	
	@Autowired
	AlarmTypeService alarmTypeService;
	
	@Autowired
	UserService userService;

	@Override
	public Pagination queryCarAlarmByReportByPage(String month,Pagination page,
			HashMap<String, Object> hm) {
		List<CarAlarm> list = alarmDao.queryCarAlarmByReport(month, page, hm);
		try {
		
			for(CarAlarm carAlarm:list){
				carAlarm = loadCarAlarm(carAlarm);
			}
		} catch (Exception e) {
		}
		page.setResult(list);
		return page;
	}

	@Override
	public List<CarAlarm> queryCarAlarmByReport(String month,
			HashMap<String, Object> hm) {
		List<CarAlarm> list = alarmDao.queryCarAlarmByReport(month, null, hm);
		if(list == null || list.size() == 0){
			return null;
		}
		try {
			for(CarAlarm carAlarm:list){
				carAlarm = loadCarAlarm(carAlarm);
			}
		} catch (Exception e) {
		}
		return list;
	}

	@Override
	public List<CarAlarm> queryCarAlarmByRemind(HashMap<String, Object> hm,Pagination page)
			throws Exception {
		String lastCarAlarmTable = alarmDao.getCarAlarmLastTable();
		if(StringUtils.isBlank(lastCarAlarmTable)){
			return null;
		}
		
		List<CarAlarm> list = alarmDao.queryCarAlarmByReport(lastCarAlarmTable.split("_")[1], page, hm);
		if(list == null || list.isEmpty())
			return null;
		for(CarAlarm carAlarm:list){
			carAlarm = loadCarAlarm(carAlarm);
		}
		return list;
	}

	@Override
	public CarAlarm loadCarAlarm(CarAlarm carAlarm) {
		Date dealDate = carAlarm.getDealDate();
		if(dealDate != null)
			carAlarm.setDealDateStr(DateUtil.format(dealDate, "yyyy-MM-dd HH:mm:ss"));
		Date alarmDate = carAlarm.getAlarmDate();
		if(alarmDate != null)
			carAlarm.setAlarmDateStr(DateUtil.format(alarmDate, "yyyy-MM-dd HH:mm:ss"));
		String carId = carAlarm.getCarId();
		carAlarm.setLastTimeStr(carAlarm.getDifTime()+"分钟");
		try {
			if(carId != null){
				Car car = this.carService.getCar(carId);
				if(car != null){
					carAlarm.setCarLicense(car.getLicense());
					InfoDeptEntity dept = this.infoDeptService.findDeptById(car.getDepId());
					if(dept != null)
						carAlarm.setDeptName(dept.getName());
				}
			}
			Short status = carAlarm.getStatus();
			if(status != null){
				String statusName = status.intValue()==0?"未处理":"已处理";
				carAlarm.setStautsName(statusName);
			}
		
			String typeId = carAlarm.getTypeId();
			if(typeId != null){
				AlarmType carAlarmType = this.alarmTypeService.getAlarmType(typeId);
				if(carAlarmType != null){
					carAlarm.setTypeName(carAlarmType.getName());
				}
			}
		
			String userId = carAlarm.getUserId();
			if(StringUtils.isNotBlank(userId)){
				UserEntity user = this.userService.findUserById(userId);
				if(user != null){
					carAlarm.setUserName(user.getUsername());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return carAlarm;
	}

	@Override
	public int updateCarAlarm(CarAlarm carAlarm) {
		return this.alarmDao.updateCarAlarm(carAlarm);
	}

	@Override
	public CarAlarm getCarAlarm(String id, Date alarmDate) {
		return this.alarmDao.getCarAlarm(id, alarmDate);
	}

	@Override
	public List<CarAlarm> queryCarAlarm(String month, HashMap<String, Object> hm,Pagination page) {
		return this.alarmDao.queryCarAlarmByReport(month, page, hm);
	}

}
