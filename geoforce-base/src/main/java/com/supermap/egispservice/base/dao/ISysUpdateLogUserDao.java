package com.supermap.egispservice.base.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.supermap.egispservice.base.entity.SysUpdateLogUserEntity;

public interface ISysUpdateLogUserDao extends CrudRepository<SysUpdateLogUserEntity, String>,
PagingAndSortingRepository<SysUpdateLogUserEntity, String>, JpaSpecificationExecutor<SysUpdateLogUserEntity>{

	/**
	 * 查找当前用户没有读取最新的更新日志记录
	 * @param updateLogid
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2016-8-12上午9:41:34
	 */
	public SysUpdateLogUserEntity findByUpdateLogidAndUserid(String updateLogid,String userid);
}
