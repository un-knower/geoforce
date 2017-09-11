package com.supermap.egispservice.lbs.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.lbs.entity.SpeedingSet;

public interface SpeedingSetDao  extends CrudRepository<SpeedingSet, String>,
PagingAndSortingRepository<SpeedingSet, String>, JpaSpecificationExecutor<SpeedingSet>{

	public SpeedingSet findByid(String id);
	
	
	public List<SpeedingSet> findBySpeedAndEid(String speed,String eid);
    
	
	public List<SpeedingSet> findByNameAndEid(String name,String eid);
    
}
