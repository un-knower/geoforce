package com.supermap.egispservice.base.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.supermap.egispservice.base.entity.MapPointCarEntity;


public interface MapPointCarDao extends CrudRepository<MapPointCarEntity, String>,
		PagingAndSortingRepository<MapPointCarEntity, String>, JpaSpecificationExecutor<MapPointCarEntity> {

	@Modifying
	@Query("DELETE FROM MapPointCarEntity t WHERE t.pointId=:id")
	public void deleteByNetIds(@Param("id")String id);
	
	@Query("SELECT COUNT(t) FROM MapPointCarEntity t WHERE t.pointId=:id")
	public long getBindCarsCount(@Param("id")String id);
	
	
}
