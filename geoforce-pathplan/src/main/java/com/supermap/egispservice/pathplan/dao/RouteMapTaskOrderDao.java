package com.supermap.egispservice.pathplan.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.RouteMapTaskOrderEntity;

public interface RouteMapTaskOrderDao extends CrudRepository<RouteMapTaskOrderEntity, String>, PagingAndSortingRepository<RouteMapTaskOrderEntity, String>, JpaSpecificationExecutor<RouteMapTaskOrderEntity> {
	
}
