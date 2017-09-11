package com.supermap.egispservice.lbs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supermap.egispservice.lbs.entity.Car;
import com.supermap.egispservice.lbs.entity.CarDriver;
import com.supermap.egispservice.lbs.pojo.CarDept;
import com.supermap.egispservice.lbs.pojo.JsonZTree;


public interface CarService {
	
	/**
	 * 添加车辆
	 * @param car
	 * @return
	 * @Author Juannyoh
	 * 2016-1-19下午5:01:56
	 */
	public Car addCar(Car car);
	
	/**
	 * 添加车辆、司机关联关系
	 * @param carDriver
	 * @return
	 * @Author Juannyoh
	 * 2016-1-19下午5:02:07
	 */
	public int addCarDriver(CarDriver carDriver);
	
	/**
	 * 根据车辆、司机id查找关联关系
	 * @param carId
	 * @param driverId
	 * @return
	 * @Author Juannyoh
	 * 2016-1-19下午5:24:13
	 */
	public CarDriver getCarDriver(String carId,String driverId);
	
	/**
	 * 根据部门得到车辆tree
	 * @param deptId
	 * @return
	 */
	public List<JsonZTree>  getCarDeptTree(String deptId,String treeId);

	/**
	 * 修改车辆信息
	 * @param car
	 * @return
	 * @Author Juannyoh
	 * 2016-1-19下午5:26:48
	 */
	public int updateCar(Car car);

	
	/**
	 * 根据车辆id查找车辆信息
	 * @param id
	 * @return
	 * @Author Juannyoh
	 * 2016-1-19下午5:27:02
	 */
	public Car getCar(String id);

	/**
	 * 查询车辆信息
	 * @param hm
	 * @return
	 * @Author Juannyoh
	 * 2016-1-19下午5:27:48
	 */
	public List<CarDept> queryCar(HashMap<String,Object> hm);
	
	/**
	 * 查询车辆信息，并分页
	 * @param hm
	 * @return
	 * @Author Juannyoh
	 * 2016-1-19下午5:28:01
	 */
	public Map<String,Object> queryCarPage(HashMap<String,Object> hm);
	
	/**
	 * 根据车辆ids查询车辆
	 * @param map
	 * @return
	 */
	public List queryCar(List caridlist);
	
	/**
	 * 判断车辆是否有车牌号
	 * @param license
	 * @param eid
	 * @return
	 * @Author Juannyoh
	 * 2016-1-19下午5:30:17
	 */
	public int haslicense(String license,String eid);
	
	/**
	 * 车辆是否绑定网点？
	 * @param carId
	 * @return
	 * @Author Juannyoh
	 * 2016-1-19下午5:32:59
	 */
	public Boolean isPointHasCar(String carId);
	
	/**
	 * 删除车辆信息
	 * @param car
	 * @return
	 * @Author Juannyoh
	 * 2016-1-19下午5:35:11
	 */
	public int delCar(Car car);
	
	/**
	 * 删除车辆、司机关联信息
	 * @param carDriver
	 * @return
	 * @Author Juannyoh
	 * 2016-1-19下午5:35:26
	 */
	public int delCarDriverByCar(CarDriver carDriver);
	
	/**
	 * 根据车辆id删除关联关系
	 * @param carId
	 * @return
	 * @Author Juannyoh
	 * 2016-1-19下午5:36:46
	 */
	public int delCarDriverbyCarId(String carId);
	
	/**
	 * 根据司机id删除关联关系
	 * @param driver
	 * @return
	 * @Author Juannyoh
	 * 2016-1-19下午5:37:06
	 */
	public int delCarDriverbyDriver(String driver);
	
	
	/**
	 * 保存车辆、司机关联关系
	 * @param CarDriver
	 * @return
	 * @Author Juannyoh
	 * 2016-1-19下午5:37:56
	 */
	public int setDriver(CarDriver CarDriver);

	/**
	 * 解除车辆、司机关联关系
	 * @param CarDriver
	 * @return
	 * @Author Juannyoh
	 * 2016-1-20上午9:01:56
	 */
	public int delDriver(CarDriver CarDriver);
	
	
	/**
	 * 根据车牌号查询车辆信息（车辆status！=3  ？）
	 * @param license
	 * @return
	 * @Author Juannyoh
	 * 2016-1-20上午9:17:29
	 */
	public Car getCarByLicense(String license,String eid);
	
	
	/**
	 * 按条件查询车辆数量
	 * @param hm
	 * @return
	 * @Author Juannyoh
	 * 2016-1-20上午9:19:54
	 */
	public int getCarCount(HashMap hm);
	
	
	/**
	 * 根据车辆id查询车辆及部门信息
	 * @param carId
	 * @return
	 * @Author Juannyoh
	 * 2016-1-20上午9:21:08
	 */
	public List getCarDept(String carId) ;
	
	


}
