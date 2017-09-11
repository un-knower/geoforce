package com.supermap.egispservice.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.supermap.egispservice.base.entity.PointExtcolEntity;

public interface PointExtcolDao extends CrudRepository<PointExtcolEntity,String>,JpaSpecificationExecutor<PointExtcolEntity>{

	
	public List<PointExtcolEntity> findByUserid(String userid);
	
	
	
}
