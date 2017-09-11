package com.supermap.egispservice.pathplan.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.RouteMapTaskCarEntity;

public interface RouteMapTaskCarDao extends CrudRepository<RouteMapTaskCarEntity, String>, PagingAndSortingRepository<RouteMapTaskCarEntity, String>, JpaSpecificationExecutor<RouteMapTaskCarEntity> {
	
}
