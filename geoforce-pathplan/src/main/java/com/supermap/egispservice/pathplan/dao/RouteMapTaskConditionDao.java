package com.supermap.egispservice.pathplan.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.RouteMapTaskConditionEntity;

public interface RouteMapTaskConditionDao extends CrudRepository<RouteMapTaskConditionEntity, String>, PagingAndSortingRepository<RouteMapTaskConditionEntity, String>, JpaSpecificationExecutor<RouteMapTaskConditionEntity> {
	
}
