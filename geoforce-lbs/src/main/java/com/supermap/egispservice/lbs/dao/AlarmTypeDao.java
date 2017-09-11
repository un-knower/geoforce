package com.supermap.egispservice.lbs.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.lbs.entity.AlarmType;

public interface AlarmTypeDao  extends CrudRepository<AlarmType, String>,
PagingAndSortingRepository<AlarmType, String>, JpaSpecificationExecutor<AlarmType>{

     public AlarmType findByid(String id);
     
     public List<AlarmType> findAll();
     
     public AlarmType findOneByCode(String code);
}
