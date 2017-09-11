package com.supermap.egispservice.base.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.supermap.egispservice.base.entity.OrderEntity;
import com.supermap.egispservice.base.entity.OrderStatusEntity;
import com.supermap.egispservice.base.entity.UserEntity;

public interface OrderDao extends CrudRepository<OrderEntity, String>, 
PagingAndSortingRepository<OrderEntity, String>, 
JpaSpecificationExecutor<OrderEntity> {
	
	@Query("SELECT o  from OrderEntity o where o.mainModuleId=:moduleId and o.user.id=:userId")
	public List<OrderEntity> queryByMainModuleId(@Param("moduleId") String moduleId, @Param("userId") String userId);

	public OrderEntity findById(String id);
	
	
//	public Page<OrderEntity> findByUserAndStatusAndOrderType(UserEntity user,OrderStatusEntity status,int orderType,Pageable page);
	
	@Query("SELECT o  from OrderEntity o where o.user.id=:userId")
	public List<OrderEntity> queryByUserid(@Param("userId")String userid);

}
