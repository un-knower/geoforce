package com.supermap.egispservice.lbs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supermap.egispservice.lbs.entity.Driver;
import com.supermap.egispservice.lbs.pojo.CarDeptDriver;

public interface DriverService {
	/**
	 * 添加司机
	 * @param driver
	 * @return
	 * @Author Juannyoh
	 * 2016-1-18下午3:04:28
	 */
	public int addDriver(Driver driver);

	
	/**
	 * 修改司机
	 * @param driver
	 * @return
	 * @Author Juannyoh
	 * 2016-1-18下午3:04:53
	 */
	public int updateDriver(Driver driver);

	/**
	 * 根据ID获取司机
	 * 
	 * @param id
	 * @return
	 */

	public Driver getDriver(String id);

	/**
	 * 查询司机，返回车辆信息
	 * @param hm
	 * @return
	 * @Author Juannyoh
	 * 2016-1-18下午3:46:25
	 */
	public List<CarDeptDriver> queryDriver(HashMap hm);
	
	/**
	 * 司机信息
	 * @param hm
	 * @return
	 * @Author Juannyoh
	 * 2016-1-18下午3:48:14
	 */
	public Map<String,Object> queryDriverPage(HashMap hm);
	/**
	 * 查询已绑定的司机
	 * @param page
	 * @param hm
	 * @return
	 */
	public Map<String,Object> queryBindDriver(HashMap hm);
	/**
	 * 判断是否有驾驶证
	 * 
	 * @param license
	 * @return
	 */
	public int haslicense(String license,String eid);
	
	/**
	 * 判断司机名称是否重复
	 * @param name
	 * @param eid
	 * @return
	 * @Author Juannyoh
	 * 2016-1-18下午3:28:19
	 */
	public int hasName(String name, String eid);

	/**
	 * 删除司机 
	 * @param driver
	 * @return
	 * @Author Juannyoh
	 * 2016-1-18下午3:28:13
	 */
   public int delDriver(Driver driver);
   
   /**
    * 删除司机byid
    * @param driverId
    * @return
    * @Author Juannyoh
    * 2016-1-18下午3:28:41
    */
   public int delDriver(String driverId);
   
   /**
    * 根据车辆Id查询司机信息
    * @param carId
    * @return
    * @Author Juannyoh
    * 2016-1-18下午4:02:38
    */
   public List<Driver> getDriversByCarId(String carId);

   /**
    * 根据licenseid查询司机信息
    * @param license
    * @return
    * @Author Juannyoh
    * 2016-1-18下午4:03:01
    */
   public Driver getDriverByLicense(String license,String eid);
   
   /**
    * 查询司机数量
    * @param hm
    * @return
    * @Author Juannyoh
    * 2016-1-18下午4:04:05
    */
   public int getDriverCount(HashMap hm);
}
