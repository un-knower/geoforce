package com.supermap.egispservice.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.supermap.egispservice.base.entity.UserRoleMapEntity;

public interface UserRoleMapDao extends CrudRepository<UserRoleMapEntity, String>, PagingAndSortingRepository<UserRoleMapEntity, String>, JpaSpecificationExecutor<UserRoleMapEntity> {

	List<UserRoleMapEntity> findByUserId(String userId);
	
	@Modifying
	@Query("DELETE FROM UserRoleMapEntity t WHERE t.userId = :userId")
	void deleteByUserId(@Param("userId")String userId);
}
