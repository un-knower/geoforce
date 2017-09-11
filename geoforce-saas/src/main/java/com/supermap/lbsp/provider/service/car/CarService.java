package com.supermap.lbsp.provider.service.car;

import java.util.HashMap;
import java.util.List;

import com.supermap.lbsp.provider.bean.CarDept;
import com.supermap.lbsp.provider.bean.JsonZTree;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.Car;
import com.supermap.lbsp.provider.hibernate.lbsp.CarDriver;

public interface CarService {
	
	/**
	 * 
	 * @param 增加车辆
	 * @return
	 */
	public Car addCar(Car car);
	public int addCarDriver(CarDriver carDriver);
	public CarDriver getCarDriver(String carId,String driverId);
	
	/**
	 * 根据部门得到车辆tree
	 * @param deptId
	 * @return
	 */
	public List<JsonZTree>  getCarDeptTree(String deptId,String treeId);
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
	public List<CarDept> queryCar(Page page,HashMap<String,Object> hm);
	public Page queryCarPage(Page page,HashMap<String,Object> hm);
	
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
	 * 删除车司机关联
	 * @param carDriver
	 * @return
	 */
	public int delCarDriverByCar(CarDriver carDriver);
	public int delCarDriverbyCarId(String carId);
	public int delCarDriverbyDriver(String driver);
	
	
	/**
	 * 车辆关联司机
	 * 
	 * @param CarDriver
	 * @return
	 */

	public int setDriver(CarDriver CarDriver);

	/**
	 * 车辆解除关联司机
	 * 
	 * @param CarDriver
	 * @return
	 */

	public int delDriver(CarDriver CarDriver);
	
	

	public Car getCarByLicense(String license);
	
	public int getCarCount(HashMap hm);
	/**
	 * 根据车辆ID查询车辆和所属部门
	 * @param carId
	 * @return
	 */
	
	public List getCarDept(String carId) ;
	
	public Page queryCarHasLiked(Page page,HashMap<String,Object> hm);

}
