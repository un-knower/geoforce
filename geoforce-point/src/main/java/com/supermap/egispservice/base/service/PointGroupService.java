package com.supermap.egispservice.base.service;

import java.util.List;

import com.supermap.egispservice.base.entity.PointGroupEntity;

public interface PointGroupService {
	
	/**
	 * 查询所有的分组
	 * @return
	 * @Author Juannyoh
	 * 2015-11-3下午1:46:51
	 */
	public List<PointGroupEntity> queryAllGroups(String id,String groupname,String userid,String eid,String dcode);
	
	/**
	 * 添加分组
	 * @param groupname
	 * @param userid
	 * @param eid
	 * @param dcode
	 * @return
	 * @Author Juannyoh
	 * 2015-11-3下午1:47:09
	 */
	public String addGroup(String groupname,String userid,String eid,String dcode);
	
	/**
	 * 修改分组
	 * @param entity
	 * @return
	 * @Author Juannyoh
	 * 2015-11-3下午1:47:28
	 */
	public String updateGroup(PointGroupEntity entity);
	
	/**
	 * 删除分组
	 * @param id
	 * @Author Juannyoh
	 * 2015-11-3下午1:47:43
	 */
	public void deleteGroup(String id);
	
	/**
	 * 根据id查找分组
	 * @param id
	 * @return
	 * @Author Juannyoh
	 * 2015-11-3下午4:35:40
	 */
	public PointGroupEntity findByid(String id);
	
	/**
	 * 批量增加分组
	 * @param list
	 * @return
	 * @Author Juannyoh
	 * 2015-11-10下午2:12:16
	 */
	public List<String> addPointGroups(List<PointGroupEntity> list);
	
	/**
	 * 根据样式查找分组
	 * @param styleid
	 * @return
	 * @Author Juannyoh
	 * 2015-11-11下午1:40:54
	 */
	public PointGroupEntity findByStyleid(String styleid);
}
