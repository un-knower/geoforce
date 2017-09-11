package com.supermap.egispservice.base.dao;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispservice.base.entity.PointGroupEntity;
import com.supermap.egispservice.base.entity.PointStyleEntity;


public interface PointGroupDao extends CrudRepository<PointGroupEntity, String>,
PagingAndSortingRepository<PointGroupEntity, String>, JpaSpecificationExecutor<PointGroupEntity> {
	
	PointGroupEntity findById(String id);
	
	
	PointGroupEntity findByStyleid(PointStyleEntity styleid);
	
	@Transactional
	@Modifying
	@Query(value="update biz_point set groupid=null where groupid=?1",nativeQuery=true)
	public int updatePointGroupidtoNUll(String groupid);

}
