package com.supermap.egispservice.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.supermap.egispservice.base.entity.ServiceModuleEntity;

public interface ServiceModuleDao extends 
CrudRepository<ServiceModuleEntity, String>, 
PagingAndSortingRepository<ServiceModuleEntity, String>, 
JpaSpecificationExecutor<ServiceModuleEntity> {
	
	public ServiceModuleEntity findById(String id);
	
	@Query("select u from ServiceModuleEntity u where u.id=:id or u.parent.id=:id")
	public List<ServiceModuleEntity> queryModuleTree(@Param("id")String id);
	
	
	@Query("select u.code from ServiceModuleEntity u order by u.code asc ")
	List<String> getCodes();
	
	
	@Query("select u.code from ServiceModuleEntity u where u.id=:pid")
	public String getMaxCode(@Param("pid")String pid);
	
	
	@Query("select distinct t.type  from ServiceModuleEntity t order by t.type asc")
	public List<String> getTypes();
	
}
