package com.supermap.egispservice.base.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.StaffEntity;
import com.supermap.egispservice.base.entity.StaffStatusEntity;


public interface IStaffDao   extends CrudRepository<StaffEntity, String>, 
PagingAndSortingRepository<StaffEntity, String>, 
JpaSpecificationExecutor<StaffEntity>{

	public StaffEntity findById(String id);
	
	public StaffEntity findByUsername(String username);
	
//	public List<StaffEntity> queryStaffList();
	
//	public void addStaff(StaffEntity staff);
	
//	public void updateStaff(StaffEntity staff);
	
//	public void removeStaff(String id);
	
//	public StaffEntity detach(String id);
	
	public List<StaffEntity> findByUsernameOrStatus(String username,StaffStatusEntity status);
	
	
	public Page<StaffEntity> findByUsernameOrStatus(String username,StaffStatusEntity status,Pageable page);
	
}
