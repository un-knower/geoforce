package com.supermap.egispservice.base.service;

import java.util.List;
import java.util.Map;

import com.supermap.egispservice.base.entity.PointStyleCustomEntity;
import com.supermap.egispservice.base.entity.PointStyleEntity;

public interface PointStyleService {
	
	/**
	 * 添加样式
	 * @param entity
	 * @return
	 * @Author Juannyoh
	 * 2015-11-11下午1:48:51
	 */
	public String addPointStyle(PointStyleEntity entity);
	/**
	 * 修改样式
	 * @param entity
	 * @return
	 * @Author Juannyoh
	 * 2015-11-11下午1:48:59
	 */
	public String updatePointStyle(PointStyleEntity entity);
	/**
	 * 删除样式
	 * @param id
	 * @Author Juannyoh
	 * 2015-11-11下午1:49:07
	 */
	public void deletePointStyle(String id);
	
	//public List<PointStyleEntity> findByGroupid(PointGroupEntity groupid);
	/**
	 * 根据参数查找样式
	 * @param m
	 * @return
	 * @Author Juannyoh
	 * 2015-11-11下午1:49:14
	 */
	public List<PointStyleEntity> findStyleByParam(Map m);
	/**
	 * 根据id查找样式
	 * @param id
	 * @return
	 * @Author Juannyoh
	 * 2015-11-11下午1:49:24
	 */
	public PointStyleEntity findById(String id);
	
	
	/**
	 * 根据用户id查找所有上传的自定义文件
	 * @param uderid
	 * @return
	 * @Author Juannyoh
	 * 2015-11-18上午9:27:38
	 */
	public List<PointStyleCustomEntity> findCustomsByUserid(String uderid);
	
	/**
	 * 保存用户上传的自定义 文件
	 * @param custom
	 * @return
	 * @Author Juannyoh
	 * 2015-11-18上午9:29:11
	 */
	public String saveCustomFiles(PointStyleCustomEntity custom);
	
	/**
	 * 根据用户上传的文件id查找文件
	 * @param customid
	 * @return
	 * @Author Juannyoh
	 * 2015-11-18上午9:29:59
	 */
	public PointStyleCustomEntity findCustomfileByid(String customid);
	
	/**
	 * 根据自定义文件id，删除文件
	 * @param customid
	 * @return
	 * @Author Juannyoh
	 * 2015-11-18上午9:30:55
	 */
	public boolean deleteCustomfileByid(String customid);
	
	/**
	 * 通过自定义的查询条件，查询用户上传的自定义文件
	 * @param m
	 * @return
	 * @Author Juannyoh
	 * 2015-11-18上午9:33:14
	 */
	public List<PointStyleCustomEntity> findCustomsByparam(Map m);
	
	/**
	 * 删除无用的子集样式
	 * @param userid
	 * @return
	 * @Author Juannyoh
	 * 2015-11-27上午9:51:37
	 */
	public int deleteUnuseStyles(String userid);
	
}
