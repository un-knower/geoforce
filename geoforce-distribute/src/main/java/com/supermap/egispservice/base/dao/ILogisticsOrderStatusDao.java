package com.supermap.egispservice.base.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.DimOrderStatusEntity;
import com.supermap.egispservice.base.entity.FendanStatusEntity;

public interface ILogisticsOrderStatusDao  extends CrudRepository<DimOrderStatusEntity, Byte>, PagingAndSortingRepository<DimOrderStatusEntity, Byte>, JpaSpecificationExecutor<DimOrderStatusEntity>{

}
