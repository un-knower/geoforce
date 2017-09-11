package com.supermap.egispservice.lbs.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispservice.lbs.entity.CarAlarmForeign;

public interface CarAlarmForeignDao  extends CrudRepository<CarAlarmForeign, String>,
PagingAndSortingRepository<CarAlarmForeign, String>, JpaSpecificationExecutor<CarAlarmForeign>{

	public CarAlarmForeign findByid(String id);
	
	
	public List<CarAlarmForeign> findBycarIdIn(List<String> carids);
	
	
	public List<CarAlarmForeign> findByforeignId(String foreignId);
	
	
	public List<CarAlarmForeign> findBycarId(String carId);
	
	public CarAlarmForeign findByCarIdAndForeignId(String carid,String foreignid);
	
	
	@Query(value="select count(*) from CarAlarmForeign c where c.carId = ?1 and c.foreignId = ?2",nativeQuery=true)
	public long getCountBycarIdforeignId(String carid,String foreignid);
	
    
}
