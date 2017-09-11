package com.supermap.egispservice.base.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.StaffStatusEntity;

public interface IStaffStatusDao   extends CrudRepository<StaffStatusEntity, String>, 
PagingAndSortingRepository<StaffStatusEntity, String>, 
JpaSpecificationExecutor<StaffStatusEntity>{
	
	
//	public void add(StaffStatusEntity status);
	
	public StaffStatusEntity findById(int id);
	
//	public void delete(int id);
	
	public StaffStatusEntity findByValue(String status);
	
}
