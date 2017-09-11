package com.supermap.egispservice.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.supermap.egispservice.base.entity.InfoRoleFunctionEntity;

public interface InfoRoleFunctionDao extends CrudRepository<InfoRoleFunctionEntity, String>, PagingAndSortingRepository<InfoRoleFunctionEntity, String>, JpaSpecificationExecutor<InfoRoleFunctionEntity> {
	List<InfoRoleFunctionEntity> findByRoleId(String roleId);
	
	@Modifying
	@Query("DELETE FROM InfoRoleFunctionEntity t WHERE t.roleId = :roleId")
	void deleteByRoleId(@Param("roleId")String roleId);
	
	@Modifying
	@Query("DELETE FROM InfoRoleFunctionEntity t WHERE t.roleId IN (:ids)")
	void deleteByIds(@Param("ids")List<String> ids);
}
