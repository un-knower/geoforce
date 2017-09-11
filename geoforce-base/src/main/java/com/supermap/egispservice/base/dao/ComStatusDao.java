package com.supermap.egispservice.base.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.ComStatusEntity;

public interface ComStatusDao extends 
CrudRepository<ComStatusEntity, Byte>, 
PagingAndSortingRepository<ComStatusEntity, Byte>, 
JpaSpecificationExecutor<ComStatusEntity> {
	
	public ComStatusEntity findByValue(String value);
	
	
}
