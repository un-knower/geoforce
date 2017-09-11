package com.supermap.egispservice.pathplan.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.supermap.analyst.networkanalyst.TransportationAnalystResult;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.egispservice.base.entity.GroupType;
import com.supermap.egispservice.base.entity.PathPlanCar;
import com.supermap.egispservice.base.entity.WeightNameType;
import com.supermap.egispservice.pathplan.pojo.PathPlanOrder;

public interface INavigateAnalystEngineerService {

	/**
	 * 最近路径分析
	 * 
	 * @param orders
	 * @param returnDetail
	 * @return
	 * @throws Exception
	 */
	public abstract TransportationAnalystResult bestPathAnalyst(Point2Ds orders, boolean returnDetail) throws Exception;

	/**
	 * 物流配送分析（基于多旅行商分析）
	 * 
	 * @param orders
	 * @param netPoints
	 * @param returnDetail
	 * @return
	 * @throws Exception
	 */
	public abstract TransportationAnalystResult navigateAnalyst(Point2Ds orderPoints, Point2Ds netPoints, boolean prohibitViaduct, boolean returnDetail)
			throws Exception;

	public abstract ArrayList<Point2Ds> sortOrders(Point2D centerPoint, Point2Ds orderPoints, int carLoad, GroupType groupType);

	/**
	 * 根据 TransportationAnalystResult 计算路径长度
	 * 
	 * @param TransportationAnalystResult
	 *            路径分析结果对象
	 * @return 路径长度
	 */
	public abstract double calculateLength(TransportationAnalystResult result);

	/**
	 * 关闭objects java
	 */
	void closeObjectJava();

	/**
	 * 初始化objects java
	 */
	void initObjectJava();

	TransportationAnalystResult findVRPPath(List<Point2D> orderPointsList, List<Point2D> netPoints,List<PathPlanCar> pathPlanCars, boolean withDetail,WeightNameType weightNameType) throws Exception;

	Map<String,Object> sortOrders(String orderIds,List<Point2D> orderPointsList, List<Point2D> netPoints, double carLoad, GroupType groupType);
	
	public TransportationAnalystResult findVRPPathWithFixed(List<Point2D> orderPointsList,List<PathPlanOrder> orders,int carLoad,double carCost,int weightNameIndex);
	
	public TransportationAnalystResult findBestPath(List<Point2D> orderPoints,int weightNameIndex) throws Exception;

	/**
	 * 
	 * <p>Title ：findVRPPathWithTimeRange</p>
	 * Description：		带时间范围的物流配送
	 * @param orderPointsList
	 * @param orders
	 * @param carLoad
	 * @param carCost
	 * @param carStart
	 * @param carEnd
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-12-28 下午04:25:56
	 */
	public TransportationAnalystResult findVRPPathWithTimeRange(List<Point2D> orderPointsList,
			List<PathPlanOrder> orders, int carLoad, double carCost, Date carStart, Date carEnd) ;
}