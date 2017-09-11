package com.supermap.egispweb.consumer.car.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispservice.lbs.service.TerminalService;
import com.supermap.egispweb.consumer.car.CarConsumer;
import com.supermap.egispweb.pojo.car.CarTerminal;
import com.supermap.lbsp.provider.bean.CarDept;
import com.supermap.lbsp.provider.bean.JsonZTree;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.Car;
import com.supermap.lbsp.provider.hibernate.lbsp.CarDriver;
import com.supermap.lbsp.provider.hibernate.lbsp.Terminal;
import com.supermap.lbsp.provider.service.car.CarService;
//import com.supermap.lbsp.provider.service.terminal.TerminalService;
@Component("carConsumer")
public class CarConsumerImpl implements CarConsumer{
	static Logger logger = Logger.getLogger(CarConsumerImpl.class.getName());
	
	@Reference(version="2.5.3")
	private CarService carServiceOLD;
	@Reference(version="2.5.3")
	private TerminalService terminalService;


	@Override
	public int addCar(CarTerminal carTerminal) {
		int ret = 0;
		try {
			
			
			
			/*Terminal terminal = carTerminal.getTerminal();
			Car car = carService.addCar(carTerminal.getCar());
			if(car != null){
				terminal.setCarId(car.getId());
				terminalService.addTerminal(carTerminal.getTerminal());
				ret = 1;
			}*/
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		return ret;
	}

	@Override
	public int delCar(Car car) {
		// TODO Auto-generated method stub
		return carServiceOLD.delCar(car);
	}
	public int delCarDriverByCar(CarDriver carDriver){
		return carServiceOLD.delCarDriverByCar(carDriver);
	}
	@Override
	public Car getCar(String id) {
		// TODO Auto-generated method stub
		return carServiceOLD.getCar(id);
	}

	@Override
	public int haslicense(String license,String eid) {
		// TODO Auto-generated method stub
		return carServiceOLD.haslicense(license ,eid);
	}
	
	public Boolean isPointHasCar(String carId){
		return carServiceOLD.isPointHasCar(carId);
	}

	@Override
	public List queryCar(Page page, HashMap hm) {
		// TODO Auto-generated method stub
		return carServiceOLD.queryCar(page, hm);
	}
	public Page queryCarPage (Page page, HashMap hm) {
		// TODO Auto-generated method stub
		return carServiceOLD.queryCarPage(page, hm);
	}

	@Override
	public List<Car> queryCar(List list) {
		// TODO Auto-generated method stub
		return carServiceOLD.queryCar(list);
	}

	@Override
	public int setDriver(CarDriver CarDriver) {
		// TODO Auto-generated method stub
		return carServiceOLD.setDriver(CarDriver);
	}

	@Override
	public int updateCar(Car car) {
		// TODO Auto-generated method stub
		return carServiceOLD.updateCar(car);
	}

	@Override
	public int addCarDriver(CarDriver carDriver) {
		// TODO Auto-generated method stub
		return carServiceOLD.addCarDriver(carDriver);
	}
	public CarDriver getCarDriver(String carId,String driverId){
		return carServiceOLD.getCarDriver(carId, driverId);
	}

	@Override
	public List<JsonZTree> getCarDeptTree(String deptId, String treeId) {
		// TODO Auto-generated method stub
		return carServiceOLD.getCarDeptTree(deptId, treeId);
	}
	public List<JsonZTree> carTreeSearch(String deptCode,String carSearch){
		List<JsonZTree> list = new ArrayList<JsonZTree>();
		if (StringUtils.isBlank(carSearch)) {
			return list;
		}
		
		//根目录
		JsonZTree rootNode = new JsonZTree();
		rootNode.setId("111");
		rootNode.setpId("0");
		rootNode.setName("车辆信息");
		rootNode.setEname("车辆信息");
		rootNode.setOpen(true);
		rootNode.setIsParent(false);
		list.add(rootNode);
		try {
			
			HashMap<String, Object> hm = new HashMap<String, Object>();
			hm.put("deptCode", deptCode);
			hm.put("carTreeSearch", carSearch);
			List<CarDept> cars = carServiceOLD.queryCar(null, hm);
			if(cars == null || cars.isEmpty()){
				return list;
			}
			rootNode.setIsParent(true);
			for (CarDept carDept: cars){
				JsonZTree ztree = new JsonZTree();
				ztree.setId(carDept.getId());
				ztree.setpId("111");
				ztree.setName(carDept.getLicense());
				ztree.setEname(carDept.getLicense());
				if(carDept.getLicense().length() > 10){
					ztree.setEname(carDept.getLicense().substring(0,10)+"..");
				}
				ztree.setChecked(false);
				ztree.setIsParent(false);
				list.add(ztree);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return list;
	}
	@Override
	public Car getCarByLicense(String license) {
		
		return carServiceOLD.getCarByLicense(license);
	}

	@Override
	public int delCarDriverbyCarId(String carId) {
		// TODO Auto-generated method stub
		return carServiceOLD.delCarDriverbyCarId(carId);
	}

	@Override
	public int delCarDriverbyDriver(String driverid) {
		// TODO Auto-generated method stub
		return carServiceOLD.delCarDriverbyDriver(driverid);
	}

	
}
