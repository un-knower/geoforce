package com.supermap.egispservice.base.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.ModuleStatusEntity;


public interface ModuleStatusDao  extends CrudRepository<ModuleStatusEntity, String>, 
PagingAndSortingRepository<ModuleStatusEntity, String>, 
JpaSpecificationExecutor<ModuleStatusEntity>{

//	public void add(ModuleStatusEntity status);
	
	public ModuleStatusEntity findById(int id);
	
//	public void delete(int  id);
	
	public ModuleStatusEntity findByValue(String status);
	
	
}
