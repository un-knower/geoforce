package com.supermap.egispservice.lbs.dao;



import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.lbs.entity.CarDismsg;

public interface CarDismsgDao  extends CrudRepository<CarDismsg, String>,
PagingAndSortingRepository<CarDismsg, String>, JpaSpecificationExecutor<CarDismsg>{

     public CarDismsg findByid(String id);
     
}
