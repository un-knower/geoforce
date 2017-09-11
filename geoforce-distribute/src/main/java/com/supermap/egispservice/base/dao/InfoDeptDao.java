package com.supermap.egispservice.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.supermap.egispservice.base.entity.InfoDeptEntity;

public interface InfoDeptDao extends CrudRepository<InfoDeptEntity, String>,
		JpaSpecificationExecutor<InfoDeptEntity> {

	public InfoDeptEntity findById(String id);

	public InfoDeptEntity findByNameAndParentId(String name, String parentId);

	public InfoDeptEntity findByCode(String code);

	public List<InfoDeptEntity> findByCodeLike(String codeLike);
}
