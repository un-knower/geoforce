package com.supermap.egispservice.base.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.UserStatusEntity;


public interface UserStatusDao  extends CrudRepository<UserStatusEntity, String>, 
PagingAndSortingRepository<UserStatusEntity, String>, 
JpaSpecificationExecutor<UserStatusEntity>{
	
	public UserStatusEntity findById(byte id);
	
	public UserStatusEntity findByValue(String value);
	
}
