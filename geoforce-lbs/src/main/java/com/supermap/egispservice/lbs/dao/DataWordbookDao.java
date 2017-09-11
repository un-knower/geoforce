package com.supermap.egispservice.lbs.dao;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.lbs.entity.DataWordbook;

public interface DataWordbookDao  extends CrudRepository<DataWordbook, String>,
PagingAndSortingRepository<DataWordbook, String>, JpaSpecificationExecutor<DataWordbook>{

	/**
	 * 根据code查询 词典
	 * @return
	 * @Author Juannyoh
	 * 2016-1-20上午10:18:59
	 */
	public DataWordbook findByCode(String code);
	
	/**
	 * 通过id查词典
	 * @param id
	 * @return
	 * @Author Juannyoh
	 * 2016-1-25下午5:09:31
	 */
	public DataWordbook findById(String id);
   
}
