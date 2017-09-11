package com.supermap.egispservice.base.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.RoleStatusEntity;


public interface IRoleStatusDao  extends CrudRepository<RoleStatusEntity, String>, 
PagingAndSortingRepository<RoleStatusEntity, String>, 
JpaSpecificationExecutor<RoleStatusEntity>{
	
//	public void add(RoleStatusEntity status);
	
	public RoleStatusEntity findById(int id);
	
//	public void delete(int id);
	
	public RoleStatusEntity findByValue(String status);
	
}
