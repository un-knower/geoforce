package com.supermap.egispweb.consumer.driver.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispweb.consumer.driver.DriverConsumer;

import com.supermap.lbsp.provider.bean.CarDeptDriver;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.Driver;
import com.supermap.lbsp.provider.service.driver.DriverService;

@Component("driverConsumer")
public class DriverConsumerImpl implements DriverConsumer{
	@Reference(version="2.5.3")
	private DriverService driverServiceOLD;

	public void setDriverService(DriverService driverService) {
		this.driverServiceOLD = driverService;
	}

	@Override
	public int addDriver(Driver driver) {
		// TODO Auto-generated method stub
		return driverServiceOLD.addDriver(driver);
	}

	@Override
	public int delDriver(Driver driver) {
		// TODO Auto-generated method stub
		return driverServiceOLD.delDriver(driver);
	}

	@Override
	public Driver getDriver(String id) {
		// TODO Auto-generated method stub
		return driverServiceOLD.getDriver(id);
	}

	@Override
	public Driver getDriverByLicense(String license) {
		// TODO Auto-generated method stub
		return driverServiceOLD.getDriverByLicense(license);
	}

	@Override
	public int getDriverCount(HashMap hm) {
		// TODO Auto-generated method stub
		return driverServiceOLD.getDriverCount(hm);
	}

	@Override
	public List<Driver> getDriversByCarId(String carId) {
		// TODO Auto-generated method stub
		return driverServiceOLD.getDriversByCarId(carId);
	}

	@Override
	public int haslicense(String license) {
		// TODO Auto-generated method stub
		return driverServiceOLD.haslicense(license);
	}

	@Override
	public List<CarDeptDriver> queryDriver(Page page, HashMap hm) {
		// TODO Auto-generated method stub
		return driverServiceOLD.queryDriver(page, hm);
	}
	public Page queryDriverPage(Page page, HashMap hm){
		return driverServiceOLD.queryDriverPage(page, hm);
	}

	@Override
	public int updateDriver(Driver driver) {
		// TODO Auto-generated method stub
		return driverServiceOLD.updateDriver(driver);
	}

	@Override
	public int delDriver(String driverId) {
		// TODO Auto-generated method stub
		return driverServiceOLD.delDriver(driverId);
	}

	@Override
	public int hasName(String name, String eid) {
		// TODO Auto-generated method stub
		return driverServiceOLD.hasName(name, eid);
	}

	@Override
	public Page queryBindDriver(Page page, HashMap hm) {
		// TODO Auto-generated method stub
		return driverServiceOLD.queryBindDriver(page, hm);
	}
}
