package com.supermap.egispservice.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.PrivlegeEntity;


public interface IPrivilegeDao  extends CrudRepository<PrivlegeEntity, String>, 
PagingAndSortingRepository<PrivlegeEntity, String>, 
JpaSpecificationExecutor<PrivlegeEntity>{
	
	public PrivlegeEntity findById(String id);
	
	public PrivlegeEntity findByName(String name);
	
	public PrivlegeEntity findByCode(String code);
	
	public List<PrivlegeEntity> findByLevel(int level);
	
	public List<PrivlegeEntity> findByNameLikeOrCodeLike(String name,String code);
}
