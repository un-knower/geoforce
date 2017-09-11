package com.supermap.egispservice.pathplan.service;

import com.supermap.egispservice.base.entity.GroupType;
import com.supermap.egispservice.base.entity.Point;
import com.supermap.egispservice.base.entity.WeightNameType;

import java.util.List;
import java.util.Map;

public interface IPathPlanServiceAPI {

	/**
	 * 使用FindVRPPath方法实现路径分析
	 *
	 * @param netPoints
	 *            网点
	 * @param orderPoints
	 *            订单
	 * @throws Exception
	 */
	public abstract Map<String, Object> generateDataByVRPPathForAPI(List<Point> netPoints, List<Point> orderPoints, boolean withDetail, GroupType groupType, WeightNameType weightNameType) throws Exception;


}