package com.supermap.egispservice.base.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.PrivilegeStatusEntity;


public interface IPrivilegeStatusDao  extends CrudRepository<PrivilegeStatusEntity, String>, 
PagingAndSortingRepository<PrivilegeStatusEntity, String>, 
JpaSpecificationExecutor<PrivilegeStatusEntity>{
	
	public PrivilegeStatusEntity findById(int id);
	
	public PrivilegeStatusEntity findByStatus(String status);
	
}
