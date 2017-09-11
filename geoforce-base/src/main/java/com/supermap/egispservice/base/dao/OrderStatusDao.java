package com.supermap.egispservice.base.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.OrderStatusEntity;


public interface OrderStatusDao extends CrudRepository<OrderStatusEntity, String>, 
PagingAndSortingRepository<OrderStatusEntity, String>, 
JpaSpecificationExecutor<OrderStatusEntity>{

//	public void add(OrderStatusEntity status);
	
	public OrderStatusEntity findById(int id);
	
//	public void delete(int id);
	
	public OrderStatusEntity findByValue(String status);
}
