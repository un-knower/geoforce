package com.supermap.egispservice.base.service;

import com.supermap.egispservice.base.entity.PointExtcolEntity;

public interface PointExtcolService {
	
	/**
	 * 第一次添加自定义字段
	 * @param record
	 * @return
	 * @Author Juannyoh
	 * 2015-8-17上午10:39:33
	 */
	public String addPointExtcol(PointExtcolEntity record);
	
	/**
	 * 添加自定义字段
	 * @param record
	 * @return
	 * @Author Juannyoh
	 * 2015-8-17上午10:40:01
	 */
	//public String updatePointExtcol(PointExtcolEntity record);
	
	/**
	 * 根据用户ID，修改自定义字段描述
	 * @param cols
	 * @param value
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2015-8-17下午4:49:20
	 */
	public String updatePointExtcol(String cols,String desc,String userid);
	
	/**
	 * 根据用户id查找到自定义字段
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2015-8-17上午10:40:15
	 */
	public PointExtcolEntity findByUserid(String userid);
	
	/**
	 * 删除用户自定义字段整条记录
	 * @param record
	 * @Author Juannyoh
	 * 2015-8-17上午10:54:13
	 */
	public void deletePointExtcol(PointExtcolEntity record);
	
	/**
	 * 保存用户字段配置
	 * @param record
	 * @return
	 * @Author Juannyoh
	 * 2016-4-12上午10:59:18
	 */
	public PointExtcolEntity saveConfigcols(PointExtcolEntity record);
	
}
