package com.supermap.egispservice.base.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.ComEntity;

public interface ComDao extends CrudRepository<ComEntity, String>, PagingAndSortingRepository<ComEntity, String>, JpaSpecificationExecutor<ComEntity> {
}
