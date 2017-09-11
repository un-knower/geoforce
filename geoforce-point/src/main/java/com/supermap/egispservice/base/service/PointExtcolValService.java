package com.supermap.egispservice.base.service;

import java.util.List;

import com.supermap.egispservice.base.entity.PointExtcolValEntity;

public interface PointExtcolValService {
	/**
	 * 添加网点自定义单个字段值
	 * @param record
	 * @return
	 * @Author Juannyoh
	 * 2015-8-18上午10:25:36
	 */
	public String addPointExtcolVal(String cols,String value,String pointid,String userid);
	
	/**
	 * 修改网点单个字段值
	 * @param cols
	 * @param desc
	 * @param pointid
	 * @return
	 * @Author Juannyoh
	 * 2015-8-18上午10:31:15
	 */
	public String updatePointExtcolVal(String cols,String desc,String pointid,String userid);
	
	
	/**
	 * 根据网点id查找对应的自定义字段值
	 * @param pointid
	 * @return
	 * @Author Juannyoh
	 * 2015-8-18上午10:32:19
	 */
	public List<PointExtcolValEntity> findByPointidOrUserid(String pointid,String userid);
	
	/**
	 * 删除网点自定义字段值的整条记录
	 * @param record
	 * @Author Juannyoh
	 * 2015-8-18上午10:35:29
	 */
	public void deletePointExtcolVal(PointExtcolValEntity record);
	
	/**
	 * 根据网点id删除网点该条自定义字段
	 * @param pointid
	 * @Author Juannyoh
	 * 2015-8-19下午4:05:32
	 */
	public void deletePointExtcolValByPointid(String pointid);
	
	/**
	 * 保存
	 * @param record
	 * @return
	 * @Author Juannyoh
	 * 2015-8-25下午6:59:42
	 */
	public String save(PointExtcolValEntity record);
}
