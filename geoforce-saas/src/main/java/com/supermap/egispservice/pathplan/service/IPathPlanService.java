package com.supermap.egispservice.pathplan.service;

import java.util.List;
import java.util.Map;

import com.supermap.egispservice.base.entity.GroupType;
import com.supermap.egispservice.base.entity.Point;
import com.supermap.egispservice.base.entity.WeightNameType;

public interface IPathPlanService {

	/**
	 * 获取线路点长度
	 * 
	 * @param points
	 * @param returnDetail
	 * @return Map<String, Object> length：int:线路长度，detail：String：中文道路信息，flag：ok,error;info:描述信息
	 * @throws Exception
	 */
	Map<String, Object> getLength(List<Point> points, boolean returnDetail) throws Exception;

	/**
	 * 
	 * @param orderPoints
	 *            订单点
	 * @param netPoints
	 *            网点
	 * @param prohibitViaduct
	 *            禁止高架 暂不支持，默认false
	 * @param returnDetail
	 *            默认false
	 *            返回详细信息
	 * @return true：已加入计划，false：加入失败
	 * @throws Exception
	 */
	boolean planByVRPPath(String taskId, boolean prohibitViaduct, boolean returnDetail,GroupType groupType,WeightNameType weightNameType ) throws Exception;

	boolean planByMTSPPath(List<Point> orderPoints, List<Point> netPoints, String taskId, boolean prohibitViaduct, boolean returnDetail);
}