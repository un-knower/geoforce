package com.supermap.egispweb.consumer.car;

import java.util.HashMap;
import java.util.List;

import com.supermap.egispweb.pojo.car.CarTerminal;
import com.supermap.lbsp.provider.bean.JsonZTree;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.Car;
import com.supermap.lbsp.provider.hibernate.lbsp.CarDriver;

public interface CarConsumer {
	/**
	 * 
	 * @param 增加车辆
	 * @return
	 */
	public int addCar(CarTerminal carTerminal);
	public int addCarDriver(CarDriver carDriver);
	public CarDriver getCarDriver(String carId,String driverId);
	/**
	 * 根据部门得到车辆tree
	 * @param deptId
	 * @return
	 */
	public List<JsonZTree>  getCarDeptTree(String deptId,String treeId);
	/**
	 * 
	* 方法名: carTreeSearch
	* 描述:根据查询条件获取车辆树
	* @param carSearch
	* @return
	 */
	public List<JsonZTree> carTreeSearch(String deptCode,String carSearch);
	/**
	 * 修改车辆
	 * 
	 * @param car
	 * @return
	 */

	public int updateCar(Car car);

	/**
	 * 根据ID获取车辆
	 * @param id
	 * @return
	 */

	public Car getCar(String id);

	/**
	 * 查询车辆
	 * @param map
	 * @return
	 */
	public List queryCar(Page page,HashMap hm);
	public Page queryCarPage(Page page,HashMap hm);
	
	/**
	 * 查询车辆
	 * @param map
	 * @return
	 */
	public List<Car> queryCar(List list);
	/**
	 * 判断是否有车牌号
	 * @param license
	 * @return
	 */
	public int haslicense(String license,String eid);
	public Boolean isPointHasCar(String carId);
	
	/**
	 * 
	 * @param 删除车辆
	 * @return
	 */
	public int delCar(Car car);
	
	
	/**
	 * 车辆关联司机
	 * 
	 * @param CarDriver
	 * @return
	 */

	public int setDriver(CarDriver CarDriver);
	
	public Car getCarByLicense(String license);
	public int delCarDriverByCar(CarDriver carDriver);
	public int delCarDriverbyCarId(String carId);
	public int delCarDriverbyDriver(String driver);
	
}
