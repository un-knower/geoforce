package com.supermap.egispservice.lbs.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.lbs.entity.TerminalType;

public interface TerminalTypeDao  extends CrudRepository<TerminalType, String>,
PagingAndSortingRepository<TerminalType, String>, JpaSpecificationExecutor<TerminalType>{

     public TerminalType findByid(String id);
     
     public List<TerminalType> findByType(Short type);
}
