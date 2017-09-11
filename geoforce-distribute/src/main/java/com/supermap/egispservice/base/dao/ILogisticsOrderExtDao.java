package com.supermap.egispservice.base.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.OrderExtEntity;

public interface ILogisticsOrderExtDao extends  CrudRepository<OrderExtEntity, String>, PagingAndSortingRepository<OrderExtEntity, String>, JpaSpecificationExecutor<OrderExtEntity>{

}
