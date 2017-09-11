package com.supermap.egispservice.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.PointStyleCustomEntity;

public interface PointStyleCustomDao extends CrudRepository<PointStyleCustomEntity, String>,
PagingAndSortingRepository<PointStyleCustomEntity, String>, JpaSpecificationExecutor<PointStyleCustomEntity>{
	
	public List<PointStyleCustomEntity> findByUserid(String userid);
	
	public PointStyleCustomEntity findById(String id);
	

}
