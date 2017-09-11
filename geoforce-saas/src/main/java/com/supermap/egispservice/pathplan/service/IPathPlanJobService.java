package com.supermap.egispservice.pathplan.service;

import com.supermap.egispservice.base.entity.GroupType;
import com.supermap.egispservice.base.entity.RouteTaskEntity;
import com.supermap.egispservice.base.entity.WeightNameType;

public interface IPathPlanJobService {

	/**
	 * 添加线路规划任务
	 * 
	 * @param routeTaskEntity
	 * @throws Exception
	 */
	public abstract boolean addJob(RouteTaskEntity routeTaskEntity,GroupType groupType,WeightNameType weightNameType ) throws Exception;

	/**
	 * 删除定时任务
	 * @param jobId
	 * @throws Exception
	 */
	void deleteJob(String jobId) throws Exception;

}