package com.supermap.egispservice.base.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.RoleEntity;
import com.supermap.egispservice.base.entity.RoleStatusEntity;


public interface IRoleDao  extends CrudRepository<RoleEntity, String>, 
PagingAndSortingRepository<RoleEntity, String>, 
JpaSpecificationExecutor<RoleEntity>{

//	public void addRole(RoleEntity re);
	
	public RoleEntity findById(String id);
	
	public RoleEntity findByName(String name);
	
	public List<RoleEntity> findByNameLikeOrStatus(String name,RoleStatusEntity status);
	
	public Page<RoleEntity> findByNameLike(String name,Pageable pageable);
	
//	public void deleteRole(String id);
	
//	public void update(RoleEntity role);
	
//	public RoleEntity detach(String id);
	
}
