package com.supermap.egispservice.pathplan.service;

import java.util.List;

import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.egispservice.base.entity.GroupType;
import com.supermap.egispservice.base.entity.PathPlanCar;
import com.supermap.egispservice.base.entity.Point;
import com.supermap.egispservice.base.entity.WeightNameType;

public interface IDataGenerateService {

	/**
	 * 使用FindVRPPath方法实现路径分析
	 * 1.初始化文件
	 * 2.转换订单、网点
	 * 3.将导航结果生成json对象
	 * 4.写入Path 结果
	 * 
	 * @param netPoints
	 *            网点
	 * @param orderPoints
	 *            订单
	 * @throws Exception
	 */
	public abstract void generateDataByVRPPath(List<Point> netPoints, List<Point> orderPoints,List<PathPlanCar> pathPlanCars, String taskId, boolean withDetail,GroupType groupType,WeightNameType weightNameType ) throws Exception;

	/**
	 * 使用findMTSPPath方法实现路径分析
	 * 1.初始化文件
	 * 2.根据角度排序订单
	 * 3.将导航结果生成json对象
	 * 4.写入Path 结果
	 * 
	 * @param netPoints
	 *            网点
	 * @param orderPoints
	 *            订单
	 * @throws Exception
	 */
	public abstract void generateDataByMTSPPath(Point2D netPoint, Point2Ds orderPoints, String taskId, boolean withDetail, boolean prohibitViaduct)
			throws Exception;
	
	/**
	 * 
	 * <p>Title ：generateDataByVRPathWithTimeRange</p>
	 * Description：		代用配送时间范围的最优路径
	 * @param netPoints
	 * @param orderPoints
	 * @param pathPlanCars
	 * @param taskId
	 * @param withDetail
	 * @param groupType
	 * @param weightNameType
	 * @throws Exception
	 * Author：Huasong Huang
	 * CreateTime：2015-12-1 上午10:40:45
	 */
	public abstract void generateDataByVRPathWithTimeRange(List<Point> netPoints, List<Point> orderPoints,List<PathPlanCar> pathPlanCars, String taskId, boolean withDetail,GroupType groupType,WeightNameType weightNameType ) throws Exception;

	public abstract int generateVRPPathWithTimeFixed(List<Point2D> netPoints, List<Point2D> orderPoints,
			List<String> orderIds, List<String> timeRanges, int carLoad, int fixConsumeMin, String taskId,long start,long end)
			throws Exception;
}