package com.supermap.egispservice.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.supermap.egispservice.base.entity.InfoDeptEntity;

public interface InfoDeptDao extends CrudRepository<InfoDeptEntity, String>,
		JpaSpecificationExecutor<InfoDeptEntity> {

	public InfoDeptEntity findById(String id);

	public InfoDeptEntity findByNameAndParentId(String name, String parentId);

	public InfoDeptEntity findByCode(String code);

	public List<InfoDeptEntity> findByCodeLike(String codeLike);
	
	/**
	 * 根据上级部门id查找下级部门
	 * @param parentId
	 * @return
	 * @Author Juannyoh
	 * 2016-10-24下午4:09:09
	 */
	public List<InfoDeptEntity> findByParentIdOrderByOperDateAsc(String parentId);
	
	//"select max(s.code) from (select left(code,8) code from INFO_DEPT) s"
	
	@Query(value="select max(s.code) from (select left(code,8) code from egisp_dev.INFO_DEPT) s",nativeQuery=true)
	public String queryMaxCode();
}
