package com.supermap.egispservice.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispservice.base.entity.PointGroupEntity;
import com.supermap.egispservice.base.entity.PointStyleEntity;

public interface PointStyleDao extends CrudRepository<PointStyleEntity, String>,
PagingAndSortingRepository<PointStyleEntity, String>, JpaSpecificationExecutor<PointStyleEntity>{
	
	//public List<PointStyleEntity> findByGroupid(PointGroupEntity groupid);
	
	public PointStyleEntity findById(String id);
	
	@Transactional
	@Modifying
	@Query(value="delete from  biz_point_style where userid=?1 and def1='1'",nativeQuery=true)
	public int deleteUnuseStyles(String userid);
	
	@Transactional
	@Modifying
	@Query(value="update biz_point_style set appcustom=NULL where  appcustom=?1",nativeQuery=true)
	public int updateappCustomBycustomid(String customid);

}
