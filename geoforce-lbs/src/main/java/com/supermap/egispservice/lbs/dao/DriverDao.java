package com.supermap.egispservice.lbs.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.lbs.entity.Driver;

public interface DriverDao  extends CrudRepository<Driver, String>,
PagingAndSortingRepository<Driver, String>, JpaSpecificationExecutor<Driver>{

	
	/**
	 * 根据ID获取司机
	 * 
	 * @param id
	 * @return
	 */

	public Driver findDriverById(String id);

	/**
	 * 查询司机
	 * 
	 * @param map
	 * @return
	 */
	//public List queryDriver(HashMap hm);
	
	/**
	 * 查询已绑定的司机
	 * @param page
	 * @param hm
	 * @return
	 */
	//public List queryBindDriver(HashMap hm);

	/**
	 * 判断是否有驾驶证
	 * 
	 * @param license
	 * @return
	 */
	public List findByLicenseAndEid(String license,String eid);
	
	/**
	 * 判断司机名称重复
	 * @param name
	 * @param eid
	 * @return
	 * @Author Juannyoh
	 * 2016-1-18下午3:31:47
	 */
	public List findByNameAndEid(String name, String eid);


	/**
	 * 根据车辆id查询司机信息
	 * @param carId
	 * @return
	 * @Author Juannyoh
	 * 2016-1-18下午3:32:14
	 */
   @Query(value="select d.* from lbs_driver d, lbs_car_driver cd where cd.driver_id = d.id " +
				"and cd.car_id =?1",nativeQuery=true)
   public List<Driver> getDriversByCarId(String carId);

   /**
    * 根据司机license、企业id查找司机
    * @param license
    * @param eid
    * @return
    * @Author Juannyoh
    * 2016-1-18下午3:38:22
    */
   public Driver findOneDriverByLicenseAndEid(String license,String eid);
   
}
