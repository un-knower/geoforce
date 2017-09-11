package com.supermap.lbsp.provider.service.driver;

import java.util.HashMap;
import java.util.List;

import com.supermap.lbsp.provider.bean.CarDeptDriver;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.Driver;

public interface DriverService {
	/**
	 * 
	 * @param 查询司机
	 * @return
	 */
	public int addDriver(Driver driver);

	/**
	 * 修改司机
	 * 
	 * @param Driver
	 * @return
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
	 * 查询司机
	 * 
	 * @param map
	 * @return
	 */
	public List<CarDeptDriver> queryDriver(Page page, HashMap hm);
	public Page queryDriverPage(Page page, HashMap hm);
	/**
	 * 查询已绑定的司机
	 * @param page
	 * @param hm
	 * @return
	 */
	public Page queryBindDriver(Page page, HashMap hm);

	/**
	 * 判断是否有驾驶证
	 * 
	 * @param license
	 * @return
	 */
	public int haslicense(String license);
	public int hasName(String name, String eid);


	/**
	 *  删除司机
	 * @param dirver
	 * @return
	 */

   public int delDriver(Driver driver);
  
	public int delDriver(String driverId);
   
   public List<Driver> getDriversByCarId(String carId);

   public Driver getDriverByLicense(String license);
   
   public int getDriverCount(HashMap hm);
}
