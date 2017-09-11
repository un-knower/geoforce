package com.supermap.egispservice.lbs.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.lbs.entity.RegionSet;

public interface RegionSetDao  extends CrudRepository<RegionSet, String>,
PagingAndSortingRepository<RegionSet, String>, JpaSpecificationExecutor<RegionSet>{

	
	@Query(value="select region_id from lbs_region_set",nativeQuery=true)
    public List<String> findAllregionIds();
	
	public RegionSet findByid(String id);
     
    
}
