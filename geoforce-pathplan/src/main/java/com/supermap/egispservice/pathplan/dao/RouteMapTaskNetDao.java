package com.supermap.egispservice.pathplan.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.RouteMapTaskNetEntity;

public interface RouteMapTaskNetDao extends CrudRepository<RouteMapTaskNetEntity, String>, PagingAndSortingRepository<RouteMapTaskNetEntity, String>,
		JpaSpecificationExecutor<RouteMapTaskNetEntity> {

}
