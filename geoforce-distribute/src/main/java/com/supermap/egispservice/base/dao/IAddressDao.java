package com.supermap.egispservice.base.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.supermap.egispservice.base.entity.AddressEntity;

public interface IAddressDao extends CrudRepository<AddressEntity, String>, JpaSpecificationExecutor<AddressEntity> {
	
	
}
