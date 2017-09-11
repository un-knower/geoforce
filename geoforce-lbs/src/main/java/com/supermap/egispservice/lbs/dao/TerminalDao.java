package com.supermap.egispservice.lbs.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.lbs.entity.Terminal;

public interface TerminalDao  extends CrudRepository<Terminal, String>,
PagingAndSortingRepository<Terminal, String>, JpaSpecificationExecutor<Terminal>{

	public Terminal findById(String id);
	
	public List<Terminal> findByMobile(String mobile);
	
	public List<Terminal> findByCode(String code);
	
	public List<Terminal> findByCarId(String carid);
	
	public List<Terminal> findByCarIdIn(List ids);
	
}
