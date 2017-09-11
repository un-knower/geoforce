package com.supermap.egispservice.base.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.supermap.egispservice.base.entity.APILogEntity;

public interface APILogDao  extends CrudRepository<APILogEntity, String>,
JpaSpecificationExecutor<APILogEntity> {
	
	
}
