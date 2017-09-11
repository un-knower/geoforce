package com.supermap.egispservice.base.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.SysUpdateLogEntity;



public interface ISysUpdateLogDao extends CrudRepository<SysUpdateLogEntity, String>,
PagingAndSortingRepository<SysUpdateLogEntity, String>, JpaSpecificationExecutor<SysUpdateLogEntity>{

	/**
	 * 根据ID查找更新日志
	 * @param logid
	 * @param deleteflag
	 * @return
	 * @Author Juannyoh
	 * 2016-8-9上午10:49:07
	 */
	public SysUpdateLogEntity findByIdAndDeleteflag(String logid,int deleteflag);
	
}
