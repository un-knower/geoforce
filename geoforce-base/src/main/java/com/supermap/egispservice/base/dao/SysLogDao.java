package com.supermap.egispservice.base.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.SysLogEntity;

public interface SysLogDao extends CrudRepository<SysLogEntity,String>,
PagingAndSortingRepository<SysLogEntity,String>,JpaSpecificationExecutor<SysLogEntity>{
	
	

}
