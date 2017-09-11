package com.supermap.egispservice.base.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.supermap.egispservice.base.entity.SysLogEntity;

public interface SysLogService {
	
	/**
	 * 保存用户日志
	 * @return
	 * @Author Juannyoh
	 * 2015-12-14下午1:46:21
	 */
	public SysLogEntity saveSysLogEntity(SysLogEntity record);
	
	/**
	 * 按条件查询用户日志信息，支持分页
	 * @param m
	 * @return
	 * @Author Juannyoh
	 * 2015-12-14下午1:46:35
	 */
	public Map<String, Object> findLogsByParam(final List<String> userid,final String eid,final List<String> deptid,final String loginname,final List<String> moduleid,final Date startTime,final Date endTime ,int pageNumber, int pageSize,String sortType);
}
