package com.supermap.egispservice.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.supermap.egispservice.base.entity.OrderItemsEntity;
import com.supermap.egispservice.base.entity.ServiceModuleEntity;

public interface OrderItemsDao extends 
CrudRepository<OrderItemsEntity, String>, 
PagingAndSortingRepository<OrderItemsEntity, String>, 
JpaSpecificationExecutor<OrderItemsEntity> {
	
	public OrderItemsEntity findById(String id);
	
	public List<OrderItemsEntity> findByModuleId(ServiceModuleEntity moduleId);
	
	@Modifying
	@Query(value="delete from EGISP_RSS_ORDER_ITEMS where id=:itemid",nativeQuery=true)
	public void deleteOrderItemsByid(@Param("itemid")String itemid);
}
