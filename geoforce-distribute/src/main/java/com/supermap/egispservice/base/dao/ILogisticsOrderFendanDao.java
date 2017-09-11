package com.supermap.egispservice.base.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.OrderFendanEntity;

public interface ILogisticsOrderFendanDao extends CrudRepository<OrderFendanEntity, String>, PagingAndSortingRepository<OrderFendanEntity, String>, JpaSpecificationExecutor<OrderFendanEntity>{

}
