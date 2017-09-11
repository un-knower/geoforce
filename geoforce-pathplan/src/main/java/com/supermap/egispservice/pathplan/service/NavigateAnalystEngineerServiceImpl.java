package com.supermap.egispservice.pathplan.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.analyst.networkanalyst.CenterPointInfo;
import com.supermap.analyst.networkanalyst.DemandPointInfo;
import com.supermap.analyst.networkanalyst.TransportationAnalyst;
import com.supermap.analyst.networkanalyst.TransportationAnalystParameter;
import com.supermap.analyst.networkanalyst.TransportationAnalystResult;
import com.supermap.analyst.networkanalyst.VRPAnalystParameter;
import com.supermap.analyst.networkanalyst.VehicleInfo;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.egispservice.base.entity.GroupType;
import com.supermap.egispservice.base.entity.PathPlanCar;
import com.supermap.egispservice.base.entity.WeightNameType;
import com.supermap.egispservice.pathplan.constant.Config;
import com.supermap.egispservice.pathplan.pojo.PathPlanOrder;
import com.supermap.egispservice.pathplan.util.BasePathAnalystEngine;

/**
 * 
 * Title 导航分析
 * Description 采用object-java进行导航分析
 * 
 */
@Service
public class NavigateAnalystEngineerServiceImpl extends BasePathAnalystEngine implements INavigateAnalystEngineerService {

	@Autowired
	private Config config;

	private final Logger logger = Logger.getLogger(NavigateAnalystEngineerServiceImpl.class);

	/**
	 * 使用objects java 710beta 的导航路径分析类的findVRPPath方法实现路径分析服务
	 */
	@Override
	public TransportationAnalystResult findVRPPath(List<Point2D> orderPointsList, List<Point2D> netPoints,List<PathPlanCar> pathPlanCars, boolean withDetail,WeightNameType weightNameType ) throws Exception {
		// 1.构建一个物流配送分析参数对象
		VRPAnalystParameter vrpAnalystParameter = new VRPAnalystParameter();
		// 设置障碍结点 ID 列表
		int[] barrierNodes = new int[0];
		vrpAnalystParameter.setBarrierNodes(barrierNodes);
		// 设置障碍弧段 ID 列表
		int[] barrierEdges = new int[0];
		vrpAnalystParameter.setBarrierEdges(barrierEdges);
		// 设置权值字段信息的名字标识
		String [] weightNames=config.getWeightName().split(",");
		int weightNameIndex=0; 
		if(WeightNameType.MileAndTruck==weightNameType){
			weightNameIndex=0;
		}else if(WeightNameType.MileBlockTruck ==weightNameType){
			weightNameIndex=1;
		}else if(WeightNameType.TimeAndTruck ==weightNameType){
			weightNameIndex=2;
		}else {
			weightNameIndex=3;
		}
		vrpAnalystParameter.setWeightName(weightNames[weightNameIndex].trim());

		// 设置分析结果中包含路由对象的集合(即 GeoLineM 的集合)。
		vrpAnalystParameter.setRoutesReturn(true);
		// 设置分析结果中包含经过弧段集合
		vrpAnalystParameter.setEdgesReturn(withDetail);
		// 设置分析结果中包含行驶导引集合
		vrpAnalystParameter.setPathGuidesReturn(withDetail);
		vrpAnalystParameter.setNodesReturn(withDetail);
		// 设置分析结果中要包含站点索引的集合
		vrpAnalystParameter.setStopIndexesReturn(withDetail);

		// 2.车辆信息数组
		VehicleInfo[] vehicleInfos = new VehicleInfo[pathPlanCars.size()];
		for (int i = 0; i < pathPlanCars.size(); i++) {
			PathPlanCar car=pathPlanCars.get(i);
			VehicleInfo vehicleInfo = new VehicleInfo();
			// 设置车辆的最大耗费值
			vehicleInfo.setCost(config.getCost());
			// 设置车辆的负载量
			vehicleInfo.setLoadWeight(car.getLoadOrderNumber());
			vehicleInfos[i] = vehicleInfo;
		}
		
		

		// 3.中心点信息数组
		CenterPointInfo[] centerPointInfos = new CenterPointInfo[netPoints.size()];
		for (int i = 0; i < netPoints.size(); i++) {
			CenterPointInfo centerPointInfo = new CenterPointInfo();
			// 设置中心点ID
			// centerPointInfo.setCenterID(i + 1);
			// 设置中心点的坐标
			Point2D centerPoint = netPoints.get(i);
			centerPointInfo.setCenterPoint(centerPoint);
			centerPointInfos[i] = centerPointInfo;
		}

		// 4.需求点信息数组
		DemandPointInfo[] demandPointInfos = new DemandPointInfo[orderPointsList.size()];
		for (int i = 0; i < orderPointsList.size(); i++) {
			DemandPointInfo demandPointInfo = new DemandPointInfo();
			// 需求点(订单)的坐标
			demandPointInfo.setDemand(1);
			// demandPointInfo.setDemandID(i + 1);
			demandPointInfo.setDemandPoint(orderPointsList.get(i));
			demandPointInfos[i] = demandPointInfo;
		}
		return getTransportationAnalyst().findVRPPath(vrpAnalystParameter, vehicleInfos, centerPointInfos, demandPointInfos);
	}
	
	
	public TransportationAnalystResult findVRPPathWithFixed(List<Point2D> orderPointsList,List<PathPlanOrder> orders,int carLoad,double carCost,int weightNameIndex){
		
		// 1.构建一个物流配送分析参数对象
		VRPAnalystParameter vrpAnalystParameter = new VRPAnalystParameter();
		// 设置障碍结点 ID 列表
		int[] barrierNodes = new int[0];
		vrpAnalystParameter.setBarrierNodes(barrierNodes);
		// 设置障碍弧段 ID 列表
		int[] barrierEdges = new int[0];
		vrpAnalystParameter.setBarrierEdges(barrierEdges);
		// 设置权值字段信息的名字标识
		String [] weightNames=config.getWeightName().split(",");
		vrpAnalystParameter.setWeightName(weightNames[weightNameIndex].trim());

		// 设置分析结果中包含路由对象的集合(即 GeoLineM 的集合)。
		vrpAnalystParameter.setRoutesReturn(true);
		// 设置分析结果中包含经过弧段集合
		vrpAnalystParameter.setEdgesReturn(false);
		// 设置分析结果中包含行驶导引集合
		vrpAnalystParameter.setPathGuidesReturn(true);
		vrpAnalystParameter.setNodesReturn(true);
		// 设置分析结果中要包含站点索引的集合
		vrpAnalystParameter.setStopIndexesReturn(true);
		// 2.车辆信息数组
		VehicleInfo[] vehicleInfos = new VehicleInfo[1];
//		double loadWeight =  Math.ceil(Double.valueOf(orders.size())/ carCount);
		for (int i = 0; i < vehicleInfos.length; i++) {
			VehicleInfo vehicleInfo = new VehicleInfo();
			// 设置车辆的最大耗费值
			vehicleInfo.setCost(carCost);
			// 设置车辆的负载量
			vehicleInfo.setLoadWeight(carLoad);
			vehicleInfos[i] = vehicleInfo;
		}
		
		

		// 3.中心点信息数组
		CenterPointInfo[] centerPointInfos = new CenterPointInfo[orderPointsList.size()];
		for (int i = 0; i < orderPointsList.size(); i++) {
			CenterPointInfo centerPointInfo = new CenterPointInfo();
			// 设置中心点ID
			// centerPointInfo.setCenterID(i + 1);
			// 设置中心点的坐标
			Point2D centerPoint = orderPointsList.get(i);
			centerPointInfo.setCenterPoint(centerPoint);
			centerPointInfos[i] = centerPointInfo;
		}

		// 4.需求点信息数组
		DemandPointInfo[] demandPointInfos = new DemandPointInfo[orders.size()];
		for (int i = 0; i < orders.size(); i++) {
			DemandPointInfo demandPointInfo = new DemandPointInfo();
			// 需求点(订单)的坐标
			demandPointInfo.setDemand(1);
			// demandPointInfo.setDemandID(i + 1);
			demandPointInfo.setDemandPoint(orders.get(i).getP());
			demandPointInfos[i] = demandPointInfo;
		}
		TransportationAnalyst analyst = getTransportationAnalyst();
		return analyst.findVRPPath(vrpAnalystParameter, vehicleInfos, centerPointInfos, demandPointInfos);
	}
	

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
			List<PathPlanOrder> orders, int carLoad, double carCost, Date carStart, Date carEnd) {

		// 1.构建一个物流配送分析参数对象
		VRPAnalystParameter vrpAnalystParameter = new VRPAnalystParameter();
		// 设置障碍结点 ID 列表
		int[] barrierNodes = new int[0];
		vrpAnalystParameter.setBarrierNodes(barrierNodes);
		// 设置障碍弧段 ID 列表
		int[] barrierEdges = new int[0];
		vrpAnalystParameter.setBarrierEdges(barrierEdges);
		// 设置权值字段信息的名字标识
		String[] weightNames = config.getWeightName().split(",");
		vrpAnalystParameter.setWeightName(weightNames[config.getDistanceIndex()].trim());
		// 研发提供
		vrpAnalystParameter.setTimeWeight(weightNames[config.getTimeIndex()].trim());

		// 设置分析结果中包含路由对象的集合(即 GeoLineM 的集合)。
		vrpAnalystParameter.setRoutesReturn(true);
		// 设置分析结果中包含经过弧段集合
		vrpAnalystParameter.setEdgesReturn(true);
		// 设置分析结果中包含行驶导引集合
		vrpAnalystParameter.setPathGuidesReturn(true);
		vrpAnalystParameter.setNodesReturn(true);
		// 设置分析结果中要包含站点索引的集合
		vrpAnalystParameter.setStopIndexesReturn(true);

		// 2.车辆信息数组
		VehicleInfo[] vehicleInfos = new VehicleInfo[1];
		VehicleInfo vehicleInfo = new VehicleInfo();
		// 设置车辆耗费
		vehicleInfo.setCost(Double.MAX_VALUE);
		// 设置车辆的负载量
		vehicleInfo.setLoadWeight(carLoad);
		// 研发提供
		vehicleInfo.setStartTime(carStart);
		vehicleInfo.setEndTime(carEnd);
		vehicleInfos[0] = vehicleInfo;

		// 3.中心点信息数组
		CenterPointInfo[] centerPointInfos = new CenterPointInfo[orderPointsList.size()];
		for (int i = 0; i < orderPointsList.size(); i++) {
			CenterPointInfo centerPointInfo = new CenterPointInfo();
			Point2D centerPoint = orderPointsList.get(i);
			centerPointInfo.setCenterPoint(centerPoint);
			centerPointInfos[i] = centerPointInfo;
		}

		// 4.需求点信息数组
		DemandPointInfo[] demandPointInfos = new DemandPointInfo[orders.size()];
		for (int i = 0; i < orders.size(); i++) {
			PathPlanOrder ppo = orders.get(i);
			DemandPointInfo demandPointInfo = new DemandPointInfo();
			// 需求点(订单)的坐标
			demandPointInfo.setDemand(1);
			demandPointInfo.setDemandPoint(ppo.getP());
			// 研发提供
			demandPointInfo.setStartTime(ppo.getStartSendTime());
			demandPointInfo.setEndTime(ppo.getEndSendTime());
			demandPointInfo.setUnloadTime(config.getOrderUnloadMiniutes());
			demandPointInfos[i] = demandPointInfo;
		}
		TransportationAnalyst analyst = getTransportationAnalyst();
		return analyst.findVRPPath(vrpAnalystParameter, vehicleInfos, centerPointInfos, demandPointInfos);
	}
	
	
	public TransportationAnalystResult findBestPath(List<Point2D> orderPoints,int weightNameIndex) throws Exception{
		
		Point2Ds point2Ds = new Point2Ds();
		for(Point2D p :orderPoints){
			point2Ds.add(p);
		}
		return bestPathAnalyst(point2Ds, false, weightNameIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.supermap.egispservice.pathplan.service.INavigateAnalystEngineer#bestPathAnalyst(com.supermap.data.Point2Ds, boolean)
	 */
	@Override
	public TransportationAnalystResult bestPathAnalyst(Point2Ds orders, boolean returnDetail) throws Exception {
		return bestPathAnalyst(orders, returnDetail, 0);
	}
	
	
	private TransportationAnalystResult bestPathAnalyst(Point2Ds orders, boolean returnDetail,int weightNameIndex) throws Exception{
		TransportationAnalystParameter parameter = this.createTransportAnalysParam(orders, returnDetail,weightNameIndex);
		logger.info("开始分析最佳路线:");
		// 弧段数少不代表弧段长度短
		boolean hasLeastEdgeCount = false;
		long startTime = System.currentTimeMillis();
		TransportationAnalystResult result = this.transportationAnalyst.findPath(parameter, hasLeastEdgeCount);
		long endTime = System.currentTimeMillis();
		logger.info("分析完成,耗时[秒]:" + (endTime - startTime) / 1000.0);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.supermap.egispservice.pathplan.service.INavigateAnalystEngineer#navigateAnalyst(com.supermap.data.Point2Ds, com.supermap.data.Point2Ds, boolean,
	 * boolean)
	 */
	@Override
	public TransportationAnalystResult navigateAnalyst(Point2Ds orderPoints, Point2Ds netPoints, boolean prohibitViaduct, boolean returnDetail)
			throws Exception {
		TransportationAnalystResult result = null;

		// 支持高架桥切换需要准备两套相同的数据集,构建两个分析对象
		// if(!prohibitViaduct)
		// updateDirectionWhenChangeElevated(networkDataset, prohibitViaduct);
		logger.info("途径点:" + orderPoints.getCount() + " 配送中心:" + netPoints.getCount());
		TransportationAnalystParameter parameter = this.createTransportAnalysParam(orderPoints, returnDetail,0);
		logger.info("开始分析导航路线:");
		long startTime = System.currentTimeMillis();
		try {
			boolean hasLeastTotalCost = false;
			// tolerance太小容易导致某些点无法被捕捉，太大计算很慢
			result = this.transportationAnalyst.findMTSPPath(parameter, netPoints, hasLeastTotalCost);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		logger.info("分析完成,耗时[秒]:" + (endTime - startTime) / 1000.0);

		return result;
	}

	
	@Override
	public Map<String,Object> sortOrders(String orderIds,List<Point2D> orderPointsList, List<Point2D> netPoints, double carLoad, GroupType groupType) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<List<Point2D>> sortedAndGroupedOrders=new ArrayList<List<Point2D>>();
		HashMap<Double, Point2D> distance_orderPoints = new HashMap<Double, Point2D>();
		Map<Double,String> orderIdMaps = new HashMap<Double,String>();
		ArrayList<Double> distanceList = new ArrayList<Double>();
		Point2D centerPoint=netPoints.get(0);
		String[] orderIdA = orderIds.split(",");
		for (int i = 0; i < orderPointsList.size(); i++) {
			Point2D orderPoint = orderPointsList.get(i);
			double value;
			if (groupType.equals(GroupType.RadialGroup)) {
				double angle = Math.atan2(orderPoint.getX() - centerPoint.getX(), orderPoint.getY() - centerPoint.getY());
				value = angle;
			} else if (groupType.equals(GroupType.CircleGroup)) {
				double distance = Math.pow(orderPoint.getX() - centerPoint.getX(), 2) + Math.pow(orderPoint.getY() - centerPoint.getY(), 2);
				value = distance;
			} else {
				value = -1;
				logger.info("默认算法、暂不支持");
			}
			distance_orderPoints.put(value, orderPoint);
			orderIdMaps.put(value, orderIdA[i]);
			distanceList.add(value);
		}
		Collections.sort(distanceList);
		List<Point2D> sortedOrderPoints=new ArrayList<Point2D>();
		List<String> sortedOrderIds = new ArrayList<String>();
		for (int i = 0; i < distanceList.size(); i++) {
			
			Point2D point = distance_orderPoints.get(distanceList.get(i));
			sortedOrderIds.add(new String(orderIdMaps.get(distanceList.get(i))));
			sortedOrderPoints.add(point);
			// 达到limit就清零
			if (sortedOrderPoints.size() >= carLoad || i == distanceList.size() - 1) {
				sortedAndGroupedOrders.add(sortedOrderPoints);
				sortedOrderPoints = new ArrayList<Point2D>();
			}
		}
		resultMap.put("orderCoords", sortedAndGroupedOrders);
		resultMap.put("sortedOrderIds", sortedOrderIds);
		return resultMap;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.supermap.egispservice.pathplan.service.INavigateAnalystEngineer#sortOrders(com.supermap.data.Point2D, com.supermap.data.Point2Ds, int,
	 * com.supermap.egispservice.pathplan.service.NavigateAnalystEngineerImpl.GroupType)
	 */
	@Override
	public ArrayList<Point2Ds> sortOrders(Point2D centerPoint, Point2Ds orderPoints, int carLoad, GroupType groupType) {
		ArrayList<Point2Ds> orderPointsList = new ArrayList<Point2Ds>();

		HashMap<Double, Point2D> distance_orderPoints = new HashMap<Double, Point2D>();
		ArrayList<Double> distanceList = new ArrayList<Double>();
		for (int i = 0; i < orderPoints.getCount(); i++) {
			Point2D orderPoint = orderPoints.getItem(i);
			double value;
			if (groupType.equals(GroupType.RadialGroup)) {
				double angle = Math.atan2(orderPoint.getX() - centerPoint.getX(), orderPoint.getY() - centerPoint.getY());
				value = angle;
			} else if (groupType.equals(GroupType.CircleGroup)) {
				double distance = Math.pow(orderPoint.getX() - centerPoint.getX(), 2) + Math.pow(orderPoint.getY() - centerPoint.getY(), 2);
				value = distance;
			} else {
				value = -1;
				logger.info("默认算法、暂不支持");
			}
			distance_orderPoints.put(value, orderPoint);
			distanceList.add(value);
		}
		// logger.info("distanceList.size()" + distanceList.size());
		Collections.sort(distanceList);
		// logger.info("distanceList.size()" + distanceList.size());
		Point2Ds sortedOrderPoints = new Point2Ds();
		for (int i = 0; i < distanceList.size(); i++) {
			Point2D point = distance_orderPoints.get(distanceList.get(i));
			sortedOrderPoints.add(point);
			// logger.info(distanceList.get(i) + "," + point);
		}
		// logger.info("sortedOrderPoints.size()" + sortedOrderPoints.getCount());

		Point2Ds point2Ds = new Point2Ds();
		for (int i = 0; i < sortedOrderPoints.getCount(); i++) {
			point2Ds.add(sortedOrderPoints.getItem(i));
			// 达到limit就清零
			if (point2Ds.getCount() >= carLoad || i == sortedOrderPoints.getCount() - 1) {
				orderPointsList.add(point2Ds);
				point2Ds = new Point2Ds();
			}
		}

		return orderPointsList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.supermap.egispservice.pathplan.service.INavigateAnalystEngineer#calculateLength(com.supermap.analyst.networkanalyst.TransportationAnalystResult)
	 */
	@Override
	public double calculateLength(TransportationAnalystResult result) {
		double totalWeight = 0;
		double[] weights = result.getWeights();
		for (double weight : weights) {
			totalWeight += weight;
		}
		logger.info("totalWeight = " + totalWeight);

		// // 测试用
		// if (false) {
		// PathGuide[] pathGuides = result.getPathGuides();
		// double lengthA = 0;
		// double lengthB = 0;
		// double lengthC = 0;
		// double lengthD = 0;
		// for (PathGuide pathGuide : pathGuides) {
		// int pathGuideCount = pathGuide.getCount();
		// for (int i = 0; i < pathGuideCount; i++) {
		// PathGuideItem pathGuideItem = pathGuide.get(i);
		// double distance = pathGuideItem.getDistance();
		// double length = pathGuideItem.getLength();
		// double weight = pathGuideItem.getWeight();
		// GeoLine geoLine = pathGuideItem.getGuideLine();
		// double lineLength = 0;
		// if (geoLine != null) {
		// lineLength = geoLine.getLength();
		// }
		//
		// lengthA += distance;
		// lengthB += length;
		// lengthC += weight;
		// lengthD += lineLength;
		// }
		// }
		// logger.info("distance = " + lengthA);
		// logger.info("length = " + lengthB);
		// logger.info("weight = " + lengthC);
		// logger.info("geoLineLength = " + lengthD);
		// }

		return totalWeight;
	}

	/**
	 * 销毁objects java对象
	 */
	@Override
	public void closeObjectJava() {
		super.destoryObjectJava();
	}

	/**
	 * 初始化objects java
	 */
	@Override
	public void initObjectJava() {
		if(config.getSwap()==2){//调用oracle数据库
			super.initOracle();
		}else{//UDB
			super.init();
		}
	}

}
