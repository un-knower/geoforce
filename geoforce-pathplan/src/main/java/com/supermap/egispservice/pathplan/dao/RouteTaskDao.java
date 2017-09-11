package com.supermap.egispservice.pathplan.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.supermap.egispservice.base.entity.RouteTaskEntity;

public interface RouteTaskDao extends CrudRepository<RouteTaskEntity, String>, PagingAndSortingRepository<RouteTaskEntity, String>,
		JpaSpecificationExecutor<RouteTaskEntity> {
	@Modifying
	@Query("DELETE FROM RouteTaskEntity t WHERE t.id IN (:ids)")
	void deleteByIds(@Param("ids") List<String> ids);
}
