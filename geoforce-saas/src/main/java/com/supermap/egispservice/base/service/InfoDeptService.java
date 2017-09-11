package com.supermap.egispservice.base.service;

import java.util.List;
import java.util.Map;

import com.supermap.egispservice.base.entity.InfoDeptEntity;

/**
 * InfoDeptService
 * 
 * @author QiuChao
 * 
 */
public interface InfoDeptService {

	/**
	 * 保存部门
	 * 
	 * @param dept
	 */
	void saveDept(InfoDeptEntity dept);

	/**
	 * 更新部门
	 * 
	 * @param dept
	 */
	void updateDept(InfoDeptEntity dept);

	/**
	 * 根据ID列表删除部门
	 * 
	 * @param ids
	 */
	void deleteDeptByIds(String ids);

	/**
	 * 查询全部部门
	 * 
	 * @return
	 */
	List<InfoDeptEntity> findAll();

	/**
	 * 根据id查找部门
	 * 
	 * @param id
	 * @return
	 */
	InfoDeptEntity findDeptById(String id);

	/**
	 * 获取当前部门及其后代部门集合
	 * 
	 * @param deptId
	 * @return
	 */
	List<InfoDeptEntity> getChildDepts(String deptId);

	/**
	 * 查询部门
	 * 
	 * @param name
	 * @param pageNumber
	 * @param pageSize
	 * @param sortType
	 * @param deptId
	 * @return
	 */
	Map<String, Object> getDepts(final String name, int pageNumber,
			int pageSize, String sortType, final String deptId);

	/**
	 * 判断在相同的上级部门下，传入的部门名称是否已经存在
	 * 
	 * @param name
	 * @param parentId
	 * @return
	 */
	boolean isReplicationNameInTheSameParent(String name, String parentId);

}