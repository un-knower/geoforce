package com.supermap.egispservice.lbs.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispservice.lbs.entity.CarDriver;

public interface CarDriverDao  extends CrudRepository<CarDriver, String>,
PagingAndSortingRepository<CarDriver, String>, JpaSpecificationExecutor<CarDriver>{

	/**
	 * 根据司机id查找司机-车辆关联关系
	 * @param driverid
	 * @return
	 * @Author Juannyoh
	 * 2016-1-19下午3:47:58
	 */
	public List<CarDriver> findCarDriverByDriverId(String driverid);
	
	/**
	 * 根据车辆id查找司机-车辆关联关系
	 * @param carid
	 * @return
	 * @Author Juannyoh
	 * 2016-1-19下午3:48:46
	 */
	public List<CarDriver> findCarDriverByCarId(String carid);
	
	/**
	 * 根据司机id、车辆id查找关联关系
	 * @param driverid
	 * @param carid
	 * @return
	 * @Author Juannyoh
	 * 2016-1-20上午9:27:25
	 */
	public CarDriver findByDriverIdAndCarId(String driverid,String carid);
	
	
	/**
	 * 根据carid删除车辆司机关系
	 * @param carid
	 * @Author Juannyoh
	 * 2016-1-21上午10:03:16
	 */
	@Transactional
	@Modifying
	@Query(value="delete from lbs_car_driver where CAR_ID = ?1",nativeQuery=true)
	public void deleteCarDriverByCarid(String carid);
	
	@Transactional
	@Modifying
	@Query(value="delete from lbs_car_driver where DRIVER_ID = ?1",nativeQuery=true)
	public void deleteCarDriverByDriverid(String driverid);
   
}
