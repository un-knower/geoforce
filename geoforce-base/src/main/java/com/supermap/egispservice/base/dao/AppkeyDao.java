package com.supermap.egispservice.base.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.AppkeyEntity;


public interface AppkeyDao  extends CrudRepository<AppkeyEntity, String>, 
PagingAndSortingRepository<AppkeyEntity, String>, 
JpaSpecificationExecutor<AppkeyEntity>{
	
	public AppkeyEntity findById(String id);
	
	public AppkeyEntity findByAppKey(String appKey);
	
}
