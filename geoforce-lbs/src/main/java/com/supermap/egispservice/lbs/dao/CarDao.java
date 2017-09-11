package com.supermap.egispservice.lbs.dao;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.lbs.entity.Car;

public interface CarDao  extends CrudRepository<Car, String>,
PagingAndSortingRepository<Car, String>, JpaSpecificationExecutor<Car>{

	/**
	 * 通过id查找车辆信息
	 * @param carid
	 * @return
	 * @Author Juannyoh
	 * 2016-1-19下午4:02:11
	 */
	public Car findById(String carid);
	
}
