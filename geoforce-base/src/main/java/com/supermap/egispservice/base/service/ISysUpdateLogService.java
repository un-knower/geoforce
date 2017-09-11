package com.supermap.egispservice.base.service;

import java.util.Map;

import com.supermap.egispservice.base.entity.SysUpdateLogEntity;


public interface ISysUpdateLogService {
	
	/**
	 * 添加更新日志
	 * @param log
	 * @return
	 * @Author Juannyoh
	 * 2016-8-9上午10:40:38
	 */
	public SysUpdateLogEntity saveLog(SysUpdateLogEntity log);
	
	/**
	 * 修改更新日志
	 * @param log
	 * @return
	 * @Author Juannyoh
	 * 2016-8-9上午10:40:46
	 */
	public SysUpdateLogEntity updateLog(SysUpdateLogEntity log);
	
	
	/**
	 * 根据id查找日志
	 * @param logid
	 * @return
	 * @Author Juannyoh
	 * 2016-8-9上午10:48:02
	 */
	public SysUpdateLogEntity  findById(String logid);
	
	/**
	 * 删除更新日志  逻辑删
	 * @param logid
	 * @Author Juannyoh
	 * 2016-8-9上午10:40:55
	 */
	public void deleteLog(String logid);
	
	/**
	 * 按条件查询更新日志
	 * @param parammap
	 * @return
	 * @Author Juannyoh
	 * 2016-8-9上午10:41:43
	 */
	public Map<String,Object> getLogsByParams(Map<String,Object> parammap);
}
