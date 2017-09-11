package com.supermap.egispservice.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispservice.base.entity.PointPicEntity;

public interface PointPicDao extends CrudRepository<PointPicEntity, String>,
PagingAndSortingRepository<PointPicEntity, String>, JpaSpecificationExecutor<PointPicEntity>{
	
	/**
	 * 根据网点id查找网点图片集
	 * @param pointid
	 * @return
	 * @Author Juannyoh
	 * 2016-2-22下午2:22:47
	 */
	public List<PointPicEntity> findByPointid(String pointid);
	
	public PointPicEntity findById(String id);
	
	@Transactional
	@Modifying
	@Query("delete from PointPicEntity where pointid=?1")
	public int deletePicByPointId(String pointid);

}
