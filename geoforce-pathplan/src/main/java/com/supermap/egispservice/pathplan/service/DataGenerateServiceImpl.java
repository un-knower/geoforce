package com.supermap.egispservice.pathplan.service;

import java.io.BufferedWriter;
import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.supermap.analyst.networkanalyst.DirectionType;
import com.supermap.analyst.networkanalyst.PathGuide;
import com.supermap.analyst.networkanalyst.PathGuideItem;
import com.supermap.analyst.networkanalyst.SideType;
import com.supermap.analyst.networkanalyst.TransportationAnalystResult;
import com.supermap.cloud.base.util.sm.coordinate.CoordinateTranslator;
import com.supermap.data.GeoLineM;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.PointM;
import com.supermap.data.PointMs;
import com.supermap.egispservice.base.entity.GroupType;
import com.supermap.egispservice.base.entity.PathPlanCar;
import com.supermap.egispservice.base.entity.Point;
import com.supermap.egispservice.base.entity.RouteTaskEntity;
import com.supermap.egispservice.base.entity.WeightNameType;
import com.supermap.egispservice.pathplan.constant.Config;
import com.supermap.egispservice.pathplan.pojo.PathPlanOrder;
import com.supermap.egispservice.pathplan.pojo.RouteTaskConstants;
import com.supermap.egispservice.pathplan.pojo.Target;
import com.supermap.egispservice.pathplan.util.FileUtil;

@Component
public class DataGenerateServiceImpl implements IDataGenerateService {
	@Autowired
	private Config config;
	@Autowired
	private INavigateAnalystEngineerService navigateAnalystEngineerService;
	private final Logger logger = Logger.getLogger(DataGenerateServiceImpl.class);

	@Autowired
	private IRouteTaskService routeTaskService;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.supermap.egispservice.pathplan.service.IDataGenerateService#generateDataByVRPPath(java.util.List, java.util.List, java.lang.String, boolean)
	 */
	@Override
	public void generateDataByVRPPath(List<Point> netPoints, List<Point> orderPoints,List<PathPlanCar> pathPlanCars, String taskId, boolean withDetail,GroupType groupType,WeightNameType weightNameType) throws Exception {
		// 1.创建文件
		// 2.转换订单、网点
		// 订单
		List<Point2D> orderPointsList = new ArrayList<Point2D>();
		for (Point point : orderPoints) {
			Point2D point2d = new Point2D(point.getLon(), point.getLat());
			orderPointsList.add(point2d);
		}
		// 网点
		List<Point2D> netPoints2 = new ArrayList<Point2D>();
		for (Point point : netPoints) {
			Point2D point2d = new Point2D(point.getLon(), point.getLat());
			netPoints2.add(point2d);
		}
		// 3.将导航结果生成json对象
		Map<String, Object> m =null;
		if(groupType==GroupType.NoneGroup){
			m = generatePathDataByVRPPath(orderPointsList, netPoints2,pathPlanCars, withDetail, weightNameType);
		}else if(groupType==GroupType.RadialGroup ){
			// 获取订单信息
			Map<String,Object> mapObjects = routeTaskService.getNetsAndOrders(taskId);
			String orderIds = (String) mapObjects.get("ordersIds");
			m = generatePathDataByVRPPathForRadialGroup(orderIds,orderPointsList, netPoints2, pathPlanCars, withDetail, weightNameType);
			List<String> sortedOrderIds = (List<String>) m.get("sortedOrderIds");
			String newOrderCoord=(String)m.get("newOrderCoord");
			reWriteOrderCoordAndOrderIdsToFile(taskId, "nets_orders.data", newOrderCoord,sortedOrderIds);
		}
		JSONObject path = (JSONObject)m.get("path");
		String stopIndexes=(String)m.get("stopIndexes");
		String pathGuides=(String)m.get("pathGuides");
		// 4.写入Path 结果
		writeToFile(taskId, "path.json", path.toString(),stopIndexes,pathGuides,null);
	}
	
	/**
	 * 
	 * <p>Title ：generateVRPPathWithTimeFixed</p>
	 * Description：			生成带有固定时间的线路规划
	 * @param netPoints		网点
	 * @param orderPoints	订单坐标
	 * @param orderIds		订单Id列表
	 * @param timeRanges	时间范围列表
	 * @param carLoad		车辆负载
	 * @param fixConsumeMin	固定消耗时间（分钟）
	 * @param taskId		任务Id
	 * @param start			批次开始时间
	 * @param end 			批次截止时间
	 * Author：Huasong Huang
	 * CreateTime：2015-12-4 上午10:01:15
	 * @throws Exception 
	 */
	public int generateVRPPathWithTimeFixed(List<Point2D> netPoints, List<Point2D> orderPoints, List<String> orderIds,
			List<String> timeRanges, int carLoad, int fixConsumeMin, String taskId, long start, long end)
			throws Exception {

		// 获取订单信息
		List<PathPlanOrder> orders = buildPathPlanOrders(orderIds, orderPoints, timeRanges, fixConsumeMin);

		// 将订单进行分组
		List<List<PathPlanOrder>> groupOrders = groupOrders(orders);

		// 生成线路
		int pathCount = generatePath(taskId, netPoints, groupOrders, 2, start, end, carLoad, fixConsumeMin);
		return pathCount;

	}
	
	
	/**
	 * 
	 * <p>Title ：buildPathPlanOrders</p>
	 * Description：		构建路线规划的订单
	 * @param orderIds
	 * @param orderPoints
	 * @param timeRanges
	 * @param fixConsumeMin
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-12-17 下午03:43:10
	 */
	private List<PathPlanOrder> buildPathPlanOrders(List<String> orderIds,List<Point2D> orderPoints,List<String> timeRanges,int fixConsumeMin){
		List<PathPlanOrder> pathPlanOrders = new LinkedList<PathPlanOrder>();
		Calendar startCal = Calendar.getInstance();
		startCal.set(Calendar.SECOND, 0);
		Calendar endCal = Calendar.getInstance();
		endCal.set(Calendar.SECOND, 0);
		for (int i = 0; i < orderIds.size(); i++) {
			PathPlanOrder ppo = new PathPlanOrder();
			ppo.setId(i+"");
			ppo.setOrderId(orderIds.get(i));
			Point2D p = orderPoints.get(i);
			ppo.setP(p);
			com.supermap.cloud.base.util.sm.coordinate.CoordinateTranslator.Point smm = new com.supermap.cloud.base.util.sm.coordinate.CoordinateTranslator.Point();
			smm.setX(p.getX());
			smm.setY(p.getY());
			CoordinateTranslator.lngLatToMercator(smm);
			Point2D smmPoint2d = new Point2D();
			smmPoint2d.setX(smm.getX());
			smmPoint2d.setY(smm.getY());
			ppo.setPp(smmPoint2d);

			// 设置开始配送时间
			String start = timeRanges.get(i * 2);
			String end = timeRanges.get(i * 2 +1);
			String[] startItems = start.split(":");
			int startHour = Integer.parseInt(startItems[0]);
			int startMin = Integer.parseInt(startItems[1]);
			
			startCal.set(Calendar.HOUR_OF_DAY, startHour);
			startCal.set(Calendar.MINUTE, startMin);
			ppo.setStartSendTime(startCal.getTime());
			long startMill = startCal.getTimeInMillis();
			// 设置截止配送时间
			String endItems[] = end.split(":");
			int endHour = Integer.parseInt(endItems[0]);
			int endMin = Integer.parseInt(endItems[1]);
			endCal.set(Calendar.HOUR_OF_DAY, endHour);
			endCal.set(Calendar.MINUTE, endMin);
			ppo.setEndSendTime(endCal.getTime());
			// 设置分组
//			String orderGroup = startHour + "_" + startMin + "_" + endHour + "_" + endMin;
			ppo.setGroup("1");
			
			
			pathPlanOrders.add(ppo);
			long endMill = endCal.getTimeInMillis();
			int carCost = (int) ((endMill - startMill)/60000 - fixConsumeMin);
			ppo.setCarCost(carCost);
		}
		return pathPlanOrders;
	}
//	
//	private void calculatePathWeight(List<Point2D> orderPointsList,List<List<PathPlanOrder>> orderGroups){
//		int indexPrefix = 0;
//		for(List<PathPlanOrder> group : orderGroups){
//			TransportationAnalystResult result = this.navigateAnalystEngineerService.findVRPPathWithFixed(orderPointsList, group, 1, 2);
//			int[][] indexs = result.getStopIndexes();
//			double[][] weights = result.getStopWeights();
//			double[] weight = result.getWeights();
//			result.dispose();
//			StringBuilder indexBuilder = new StringBuilder("[");
//			for(int i=0;i<indexs[0].length;i++){
//				indexBuilder.append(group.get(indexs[0][i]).getId());
//				if(i < indexs[0].length - 1){
//					indexBuilder.append(",");
//				}
//			}
//			indexBuilder.append("]");
//			StringBuilder weightBuilder = new StringBuilder("[");
//			for(int i=0;i<weights[0].length;i++){
//				weightBuilder.append(weights[0][i]);
//				if(i < weights[0].length - 1){
//					weightBuilder.append(",");
//				}
//			}
//			weightBuilder.append("]");
//			System.out.println(weight[0]);
//			System.out.println(indexBuilder.toString());
////			System.out.println(weightBuilder.toString());
//			
//			
//		}
		
//	}
	
	
	/**
	 * 
	 * <p>Title ：groupOrders</p>
	 * Description：		将订单按照车辆耗费进行分组
	 * @param orders
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-12-11 上午11:31:01
	 */
	private List<List<PathPlanOrder>> groupOrders(List<PathPlanOrder> orders){
		Map<String,List<PathPlanOrder>> groupResult = new HashMap<String,List<PathPlanOrder>>();
		for(PathPlanOrder ppo : orders){
			String group = ppo.getGroup();
			if(groupResult.containsKey(group)){
				groupResult.get(group).add(ppo);
			}else{
				List<PathPlanOrder> list = new ArrayList<PathPlanOrder>();
				list.add(ppo);
				groupResult.put(group, list);
			}
		}
		List<List<PathPlanOrder>> groupList = new ArrayList<List<PathPlanOrder>>();
		Set<String> keys = groupResult.keySet();
		Iterator<String> iterator = keys.iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			groupList.add(groupResult.get(key));
		}
		return groupList;
	}
	
	public static List<List<Target>> dbSCANCluster(List<Target> targets, double eps, int minPts) {
		DistanceMeasure measure = new EuclideanDistance();
		DBSCANClusterer<Clusterable> dbSCANClusterer = new DBSCANClusterer<Clusterable>(eps, minPts, measure);
		return groupOrders(dbSCANClusterer, targets);
	}
	
	/**
	 * 
	 * <p>Title ：cluster</p>
	 * Description：		对订单进行分组
	 * @param clusterer
	 * @param targets
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-12-9 下午03:04:49
	 */
	public static List<List<Target>> groupOrders(Clusterer<Clusterable> clusterer, List<Target> targets) {
		List<List<Target>> clusterTargets = new ArrayList<List<Target>>();
		List<Clusterable> clusters = new ArrayList<Clusterable>();
		for (Target target : targets) {
			clusters.add(target);
		}
		List<CentroidCluster<Clusterable>> centroidClusterList = (List<CentroidCluster<Clusterable>>) clusterer.cluster(clusters);
		for (Cluster<Clusterable> item : centroidClusterList) {
			List<Clusterable> points = item.getPoints();
			List<Target> targets2 = new ArrayList<Target>();
			for (Clusterable point : points) {
				Target target = (Target) point;
				targets2.add(target);
			}
			clusterTargets.add(targets2);
		}
		return clusterTargets;
	}
	
	
	public static void printResult(HashMap<Target, List<Target>> result) {
		for (Target item : result.keySet()) {
			System.out.println(item);
			List<Target> centroidTargets = result.get(item);
			System.out.print("[");
			int count = 0;
			for (Target target : centroidTargets) {
				if (count > 0) {
					System.out.print(", ");
				}
				System.out.print(target);
				count++;
			}
			System.out.println("]");
			System.out.println("----------------------------");
		}
	}
	
	private void generateJsonResult(List<Point2D> orderPointsList, List<List<PathPlanOrder>> pathList, String taskId) throws Exception {
		List<JSONObject> jsonPointList = new ArrayList<JSONObject>();
		int[] parts = new int[pathList.size()];
		StringBuilder stopIndexBuilder = new StringBuilder("[");
		StringBuilder pathGuid = new StringBuilder("[");
		StringBuilder weightBuilder = new StringBuilder("[");
		for(int i=0;i<pathList.size();i++){
			List<PathPlanOrder> path = pathList.get(i);
			Point2Ds point2Ds = new Point2Ds();
			stopIndexBuilder.append("[").append(0).append(",");
			point2Ds.add(orderPointsList.get(0));
			for(int j=0;j<path.size();j++){
				point2Ds.add(path.get(j).getP());
				stopIndexBuilder.append(path.get(j).getId()).append(",");
			}
			stopIndexBuilder.append(0).append("]");
			
			point2Ds.add(orderPointsList.get(0));
			TransportationAnalystResult result = this.navigateAnalystEngineerService.bestPathAnalyst(point2Ds, true);
			double weight[] = result.getWeights();
			// 提取线路
			fetchResult(result, jsonPointList, parts, i);
			// 提取站点索引
//			String stopIndexes=fetchStopIndexes(result,preCount,1);
//			preCount += path.size();
			
			// 提取导航信息
			String pathGuidInfo = showResult(result);
			pathGuid.append(pathGuidInfo.substring(1, pathGuidInfo.length() - 1));
			// 提取线路耗费
//			String weights = fetchPathWeight(result);
			weightBuilder.append(weight[0]);
			if(i < pathList.size() - 1){
				stopIndexBuilder.append(",");
				pathGuid.append(",");
				weightBuilder.append(",");
			}
			result.dispose();
		}
		stopIndexBuilder.append("]");
		pathGuid.append("]");
		weightBuilder.append("]");
		JSONObject path=convertLineToJson(jsonPointList, parts);
		writeToFile(taskId, "path.json", path.toString(), stopIndexBuilder.toString(), pathGuid.toString(), weightBuilder.toString());
		
	}

	/**
	 * 
	 * <p>Title ：fetchOrderIds</p>
	 * Description：		提取订单ID列表
	 * @param orderList
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-12-7 下午04:55:31
	 */
	private List<String> fetchOrderIds(List<PathPlanOrder> orderList){
		List<String> orderIds = new ArrayList<String>();
		for(PathPlanOrder ppo : orderList){
			orderIds.add(ppo.getOrderId());
		}
		return orderIds;
	}
	
	
	/**
	 * 
	 * <p>Title ：fetOrderCoords</p>
	 * Description：		提取坐标
	 * @param pathList
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-12-7 下午04:58:50
	 */
	private String fetOrderCoords(List<PathPlanOrder> orderList) {
		StringBuilder sb = new StringBuilder();
		for (PathPlanOrder ppo : orderList) {
			sb.append(ppo.getP().getX()).append(",").append(ppo.getP().getY()).append(",");
		}
		return sb.toString().substring(0, sb.length() - 1);
	}
	

	private String fetchPathWeight(TransportationAnalystResult result) {
		double[][] weights = result.getStopWeights();
		StringBuilder sb = new StringBuilder("[");
		double weight[] = weights[0];
		for(int i=0;i<weight.length;i++){
			sb.append(weight[i]);
			if(i < weight.length - 1){
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 
	 * <p>Title ：generatePath</p>
	 * Description：		根据提供的订单列表，规划最优线路，结果放置在pathList中。
	 * @param taskId			任务ID
	 * @param orderPointsList	网点列表
	 * @param groupOrders		分组列表
	 * @param weightNameIndex	权重字段的下标，0：length；1：length3；2：pathtime;3:pathtime2
	 * @param start				开始配送基准时间
	 * @param end				回到起点的时间
	 * @throws Exception
	 * @return					线路的数量
	 * Author：Huasong Huang
	 * CreateTime：2015-12-18 上午09:29:12
	 */
	@SuppressWarnings("unchecked")
	private int generatePath(String taskId, List<Point2D> netPoints, List<List<PathPlanOrder>> groupOrders,
			int weightNameIndex,long start,long end,int carLoad,double fixConsuTime) throws Exception {

		List<JSONObject> pathLists = new LinkedList<JSONObject>();
		List<List<Integer>> stopIndexLists = new ArrayList<List<Integer>>();
		JSONArray weightArray = new JSONArray();
		JSONArray guideArrays = new JSONArray();
		List<Integer> partList = new ArrayList<Integer>();
		List<Double> distances = new ArrayList<Double>();
		List<List<Double>> stopWeights = new ArrayList<List<Double>>();
		double totalDistance = 0;
		JSONArray timeArray = null;
		long consuMin = (end - start) / 60000;
		long realCanRun = (long) (consuMin - fixConsuTime);
		logger.info("## the minutes of the car can run ["+realCanRun);
		
		for (List<PathPlanOrder> orderList : groupOrders) {
			// 将结果加入到PathList中
			TransportationAnalystResult finalResult = null;
//			finalResult = this.navigateAnalystEngineerService.findVRPPathWithFixed(netPoints, orderList, carLoad, realCanRun,
//					weightNameIndex);
			// 待研发提供新jar包时候加入
			finalResult = this.navigateAnalystEngineerService.findVRPPathWithTimeRange(netPoints, orderList, carLoad,
					realCanRun, new Date(start), new Date(end));
			Map<String, Object> resultMap = fetchPathDetails(finalResult, orderList, start);
			finalResult.dispose();
			// 线路
			List<JSONObject> pathList = (List<JSONObject>) resultMap.get(RouteTaskConstants.RESULT_NAME_PATH);
			pathLists.addAll(pathList);
			// parts
			int[] parts = (int[]) resultMap.get(RouteTaskConstants.RESULT_NAME_PARTS);
			for (int part : parts) {
				partList.add(part);
			}
			// stopIndex
			List<List<Integer>> stopIndexList = (List<List<Integer>>) resultMap.get(RouteTaskConstants.RESULT_NAME_INDEX);
			stopIndexLists.addAll(stopIndexList);
			// pathGuid
			String guidStr = (String) resultMap.get(RouteTaskConstants.RESULT_NAME_GUIDE);
			JSONArray guideArray = JSONArray.fromObject(guidStr);
			guideArrays.addAll(guideArray);
			// weight
			JSONArray weights = (JSONArray) resultMap.get(RouteTaskConstants.RESULT_NAME_WEIGHT);
			weightArray.addAll(weights);
			
			// distance
			List<Double> dis = (List<Double>) resultMap.get(RouteTaskConstants.RESULT_NAME_DISTANCE);
			distances.addAll(dis);
			
			// totalDistance 
			double tdis = (Double) resultMap.get(RouteTaskConstants.RESULT_NAME_TOTAL_DISTANCE);
			totalDistance += tdis;
			//stopWeights
			List<List<Double>> stopSLs = (List<List<Double>>) resultMap.get(RouteTaskConstants.RESULT_NAME_STOP_WEIGHTS);
			stopWeights.addAll(stopSLs);
			// stopTimes
			timeArray = (JSONArray) resultMap.get(RouteTaskConstants.RESULT_NAME_STOP_TIMES);
			
		}
		for (int i = 0; i < guideArrays.size(); i++) {
			JSONObject obj = guideArrays.getJSONObject(i);
			obj.accumulate("index", i);
			guideArrays.set(i, obj);

		}

		JSONObject path = convertLineToJson(pathLists, partList);
		JSONArray sotpIndexArray = JSONArray.fromObject(stopIndexLists);
		JSONArray diss = JSONArray.fromObject(distances);
		JSONArray stopW = JSONArray.fromObject(stopWeights);
//		writeToFile(taskId, RouteTaskConstants.FILE_RESULT_NAME, path.toString(), sotpIndexArray.toString(), guideArrays.toString(),
//				weightArray.toString());
		writePathResultToFile(taskId, RouteTaskConstants.FILE_RESULT_NAME, path.toString(), sotpIndexArray.toString(),
				guideArrays.toString(), weightArray.toString(), diss.toString(), totalDistance + "",stopW.toString(),timeArray.toString());
		return stopIndexLists.size();

	}
	
	
	
	/**
	 * 
	 * <p>Title ：fetchPathDetails</p>
	 * Description：		提取线路详细信息
	 * @param result
	 * @param orders
	 * @param start
	 * @return
	 * @throws Exception
	 * Author：Huasong Huang
	 * CreateTime：2015-12-18 下午02:04:13
	 */
	public Map<String,Object>  fetchPathDetails(TransportationAnalystResult result,List<PathPlanOrder> orders,long start) throws Exception{
		int[][] stopIndexs = result.getStopIndexes();
		double[] weights = result.getWeights();
		double[][] stopWeights = result.getStopWeights();
		Date[][] stopDates = result.getTimes();
		
		// 存放距离的列表
		List<Double> distanceList = new ArrayList<Double>();
		String pathGuid = fetchPathGuidInfo(result,distanceList);
		List<JSONObject> jsonPointList = new LinkedList<JSONObject>();
		int[] parts = new int[weights.length];
		fetchResultForVRPPath(result, jsonPointList, parts);
//		JSONObject path=convertLineToJson(jsonPointList, parts);
		List<List<Integer>> stopIndexLists = new ArrayList<List<Integer>>();
		for(int[] stopIndex : stopIndexs){
			List<Integer> stopIndexList = new ArrayList<Integer>();
			stopIndexList.add(0);
			for (int i = 1; i < stopIndex.length - 1; i++) {
				PathPlanOrder ppo = orders.get(stopIndex[i]);
				stopIndexList.add(Integer.parseInt(ppo.getId()));
			}
			stopIndexList.add(0);
			stopIndexLists.add(stopIndexList);
		}
		List<Double> distances = distanceList.subList(0, distanceList.size() - 1);
		double tDistance = distanceList.get(distanceList.size() - 1);
		// stopWeigths 
		List<List<Double>> stopWeightsList = new ArrayList<List<Double>>();
		for(double[] stopWeight : stopWeights){
			List<Double> stopWL = new ArrayList<Double>();
			for(int i=0;i<stopWeight.length;i++){
				stopWL.add(stopWeight[i]);
			}
			stopWeightsList.add(stopWL);
		}
		double[] pathMini = new double[distances.size()];
		// stop times
		List<List<String>> stopTimes = buildStopTimes(stopDates,pathMini);
		JSONArray timeArryas = JSONArray.fromObject(stopTimes);
		
		JSONArray weightArray = JSONArray.fromObject(pathMini);
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put(RouteTaskConstants.RESULT_NAME_PATH, jsonPointList);
		resultMap.put(RouteTaskConstants.RESULT_NAME_PARTS, parts);
		resultMap.put(RouteTaskConstants.RESULT_NAME_INDEX, stopIndexLists);
		resultMap.put(RouteTaskConstants.RESULT_NAME_WEIGHT, weightArray);
		resultMap.put(RouteTaskConstants.RESULT_NAME_GUIDE, pathGuid);
		resultMap.put(RouteTaskConstants.RESULT_NAME_DISTANCE, distances);
		resultMap.put(RouteTaskConstants.RESULT_NAME_TOTAL_DISTANCE, tDistance);
		resultMap.put(RouteTaskConstants.RESULT_NAME_STOP_WEIGHTS, stopWeightsList);
		resultMap.put(RouteTaskConstants.RESULT_NAME_STOP_TIMES, timeArryas);
		
		return resultMap;
	}

	/**
	 * 
	 * <p>Title ：buildStopTimes</p>
	 * Description：		构建站点时间
	 * @param stopWeightsList
	 * @param start
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-12-24 下午07:22:06
	 */
	private List<List<String>> buildStopTimes(Date[][] dates,double[] pathMins){
		List<List<String>> stopTimes = new ArrayList<List<String>>();
		int k = 0;
		for(Date[] date : dates){
			List<String> stopTime = new ArrayList<String>();
			double pathMin = 0;
			Date start = null;
			Date end = null;
			for (int i = 0; i < date.length; i++) {
				long cD = 0;
				if(i == 0){
					start = date[i];
					cD = start.getTime();
				}else if(i == date.length - 1){
					end = date[i];
					cD = end.getTime();
				}else{
					cD = date[i].getTime() - config.getOrderUnloadMiniutes() * 60000;
				}
				Date d = new Date(cD);
				stopTime.add(sdf.format(d));
			}
			stopTimes.add(stopTime);
			long diff = end.getTime() - start.getTime();
			pathMin = diff / 60000;
			pathMins[k++] = pathMin;
		}
		return stopTimes;
	}
	
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.supermap.egispservice.pathplan.service.IDataGenerateService#generateDataByMTSPPath(com.supermap.data.Point2D, com.supermap.data.Point2Ds,
	 * java.lang.String, boolean, boolean)
	 */
	@Override
	public void generateDataByMTSPPath(Point2D netPoint, Point2Ds orderPoints, String taskId, boolean withDetail, boolean prohibitViaduct) throws Exception {
		// 1.创建文件
		createFiles(taskId);
		// 2.排序订单
		GroupType groupType = GroupType.RadialGroup;
		int carLoad = config.getCarLoad();
		// 一个网点中心，根据orderPoints和carLoad安排多辆车(carLoad=300000 越大，就只有一辆车)
		ArrayList<Point2Ds> orderPointsList = navigateAnalystEngineerService.sortOrders(netPoint, orderPoints, carLoad, groupType);
		logger.info("根据角度排序车辆");
		// 3.将导航结果生成json对象
		Point2Ds netPoints = new Point2Ds();
		netPoints.add(netPoint);
		Map<String, Object> m = generatePathDataByMTSPPath(orderPointsList, netPoints, withDetail, prohibitViaduct);
		JSONObject path = (JSONObject)m.get("path");
		String stopIndexes=(String)m.get("stopIndexes");
		String pathGuides=(String)m.get("pathGuides");
		// 4.写入Path 结果
		writeToFile(taskId, "path.json", path.toString(),stopIndexes,pathGuides,null);
	}

	/**
	 * 使用findMTSPPath方法实现路径分析
	 * 
	 * @param orderPointsList
	 * @param netPoints
	 * @param withDetail
	 * @param prohibitViaduct
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> generatePathDataByMTSPPath(ArrayList<Point2Ds> orderPointsList, Point2Ds netPoints, boolean withDetail, boolean prohibitViaduct)
			throws Exception {
		Map<String, Object> m=new HashMap<String, Object>();
		List<JSONObject> jsonPointList = new ArrayList<JSONObject>();
		int[] parts = new int[orderPointsList.size()];
		int preCount=0;//上一次的步长
		for (int i = 0; i < orderPointsList.size(); i++) {
			Point2Ds groupOrderPoints = orderPointsList.get(i);
			
			// 分析
			logger.info("导航分析中......MTSPPath常规模式");
			TransportationAnalystResult result = navigateAnalystEngineerService.navigateAnalyst(groupOrderPoints, netPoints, prohibitViaduct, withDetail);
			if (result != null) {
				fetchResultForMTSPPath(result, jsonPointList, parts, i);
				JSONObject path=convertLineToJson(jsonPointList, parts);
				String stopIndexes=fetchStopIndexes(result,preCount,0);
				preCount=groupOrderPoints.getCount();
				String pathGuides=showResult(result);
				m.put("path", path);
				m.put("stopIndexes", stopIndexes);
				m.put("pathGuides", pathGuides);
				result.dispose();
			} else {
				logger.error("导航分析结果为null");
			}
		}
		return m;
	}

	/**
	 * 放射状的道路分析
	 * 1.排序后的订单的坐标顺序写入.data文件，覆盖原来的记录
	 * 2.组装每个stopindexs 和 pathguides并写入文件
	 */
	public Map<String, Object> generatePathDataByVRPPathForRadialGroup(String orderIds,List<Point2D> orderPointsList, List<Point2D> netPoints,List<PathPlanCar> pathPlanCars, boolean withDetail,WeightNameType weightNameType ) throws Exception {
		Map<String, Object> m=new HashMap<String, Object>();
		//订单排序
		Map<String,Object>  sortedOrderInfos = navigateAnalystEngineerService.sortOrders(orderIds,orderPointsList, netPoints ,pathPlanCars.get(0).getLoadOrderNumber(), GroupType.RadialGroup) ;
		List<List<Point2D>> orderPoint2DsList = (List<List<Point2D>>) sortedOrderInfos.get("orderCoords");
		List<String> sortedIds = (List<String>) sortedOrderInfos.get("sortedOrderIds");
		
		StringBuffer newOrderCoord=new StringBuffer();
		for (int i = 0; i < orderPoint2DsList.size(); i++) {
			List<Point2D> points=orderPoint2DsList.get(i);
			for (int j = 0; j < points.size(); j++) {
				Point2D p=points.get(j);
				newOrderCoord.append(p.getX()).append(",").append(p.getY()).append(",");
			}
		}
		
		List<JSONObject> jsonPointList = new ArrayList<JSONObject>();
		int[] parts = new int[orderPoint2DsList.size()];// 在fetchResultForVRPPath方法中初始化
		StringBuffer totalStopIndexes=new StringBuffer("[");
		StringBuffer totalPathGuides=new StringBuffer("[");
		logger.info("导航分析中......MTSPPath放射模式");
		int preCount=0;//上一次的步长
		List<PathPlanCar> onlyOnePathPlanCar=new ArrayList<PathPlanCar>();
		onlyOnePathPlanCar.add(pathPlanCars.get(0));
		StringBuilder stopWeightsBuilder = new StringBuilder();
		stopWeightsBuilder.append("[");
		for (int i = 0; i < orderPoint2DsList.size(); i++) {
			List<Point2D> points=orderPoint2DsList.get(i);
			TransportationAnalystResult result = navigateAnalystEngineerService.findVRPPath(points, netPoints,onlyOnePathPlanCar, withDetail,weightNameType);
			if (result != null) {
				fetchResult(result, jsonPointList, parts, i);
				String stopIndexes=fetchStopIndexes(result,preCount,0);
				
				String stopWeights = fetchStopWeights(result);
				stopWeightsBuilder.append(stopWeights);
				if(i < orderPoint2DsList.size() - 1){
					stopWeightsBuilder.append(",");
				}
				preCount += points.size();
				if(i!=0){
					totalStopIndexes.append(",");
					totalPathGuides.append(",");
				}
				totalStopIndexes.append(stopIndexes.substring(1, stopIndexes.length()-1));
				String pathGuides=showResult(result);
				totalPathGuides.append(pathGuides.substring(1, pathGuides.length()-1));
				
				
				result.dispose();
			} else {
				logger.warn("导航分析result is null");
			}
		}
		stopWeightsBuilder.append("]");
		totalStopIndexes.append("]");
		totalPathGuides.append("]");
		JSONObject path=convertLineToJson(jsonPointList, parts);
		m.put("path", path);
		m.put("stopIndexes", totalStopIndexes.toString());
		m.put("pathGuides", totalPathGuides.toString());
		m.put("newOrderCoord", newOrderCoord.substring(0, newOrderCoord.length() - 1));//新的订单序列
		m.put("sortedOrderIds", sortedIds);
		m.put("stopWeights", stopWeightsBuilder.toString());
		return m;
	}
	
	/**
	 * 使用findVRPPath方法实现路径分析
	 * 
	 * @param orderPointsList
	 * @param netPoints
	 * @param withDetail
	 * @param prohibitViaduct
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> generatePathDataByVRPPath(List<Point2D> orderPointsList, List<Point2D> netPoints,List<PathPlanCar> pathPlanCars, boolean withDetail,WeightNameType weightNameType ) throws Exception {
		//ArrayList<Point2Ds> orderPointsList = navigateAnalystEngineerService.sortOrders(netPoints, orderPointsList, config.getCarLoad(), GroupType.RadialGroup);
		
		Map<String, Object> m=new HashMap<String, Object>();
		List<JSONObject> jsonPointList = new ArrayList<JSONObject>();
		int[] parts = null;// 在fetchResultForVRPPath方法中初始化
		// 分析
		logger.info("导航分析中(findVRPPath)......");
		TransportationAnalystResult result = navigateAnalystEngineerService.findVRPPath(orderPointsList, netPoints,pathPlanCars, withDetail, weightNameType);
		if (result != null) {
			parts = new int[result.getRoutes().length];
			fetchResultForVRPPath(result, jsonPointList, parts);
			JSONObject path=convertLineToJson(jsonPointList, parts);
			String stopIndexes=fetchStopIndexes(result,0,0);
			String pathGuides=showResult(result);
			m.put("path", path);
			m.put("stopIndexes", stopIndexes);
			m.put("pathGuides", pathGuides);
			result.dispose();
		} else {
			logger.error("导航分析结果为null");
		}
		return m;
	}

	public void createFiles(String folderName) throws Exception {

		String pathName = config.getDataPath() + folderName;
		File file = new File(pathName);
		if (file.exists() && file.isDirectory()) {
			FileUtil.delFolder(pathName);
		}
		file.mkdir();// 根据任务ID号创建文件夹，文件夹下存放path.json，sh_net.json，sh_order.json
		Files.touch(new File(pathName + File.separator + "path.json"));
		Files.touch(new File(pathName + File.separator + "sh_net.json"));
		Files.touch(new File(pathName + File.separator + "sh_order.json"));
		logger.info("创建文件path.json，sh_net.json，sh_order.json成功");
	}

	/**
	 * 将排序后的订单坐标从新写入到.data文件
	 */
	public void reWriteOrderCoordAndOrderIdsToFile(String folderName, String fileName, String newOrderCoord,List<String> orderIds) throws Exception {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<orderIds.size();i++){
			sb.append(orderIds.get(i));
			if(i < orderIds.size() - 1){
				sb.append(",");
			}
		}
		File file = new File(config.getDataPath() + File.separator + folderName + File.separator + fileName);
		List<String> lines=Files.readLines(file, Charsets.UTF_8);
		BufferedWriter bw = Files.newWriter(file, Charsets.UTF_8);
		for (int i = 0; i < lines.size(); i++) {
			if(i == 2){
				bw.write(sb.toString());
				bw.newLine();
			}else if(i==3){
				bw.write(newOrderCoord);
				bw.newLine();
			}else{
				bw.write(lines.get(i));
				if(i!=lines.size()-1){
					bw.newLine();
				}
			}
		}
		bw.close();
	}
	/**
	 * 第一行，路径结果
	 * 第二行，途径点顺序
	 * 第三行，中文转向信息
	 * @param folderName
	 * @param fileName
	 * @param content
	 * @param stopIndexes
	 * @param pathGuides
	 * @throws Exception
	 */
	public void writeToFile(String folderName, String fileName, String content,String stopIndexes,String pathGuides,String stopWeights) throws Exception {
		File file = new File(config.getDataPath() + File.separator + folderName + File.separator + fileName);
		BufferedWriter bw = Files.newWriter(file, Charsets.UTF_8);
		bw.write(content);
		bw.newLine();
		bw.write(stopIndexes);
		bw.newLine();
		bw.write(pathGuides);
		if(!StringUtils.isEmpty(stopWeights)){
			bw.newLine();
			bw.write(stopWeights);
		}
		bw.close();
	}
	
	/**
	 * 
	 * <p>Title ：writeToFile</p>
	 * Description：		写入到文件
	 * @param folderName
	 * @param fileName
	 * @param contents
	 * @throws Exception
	 * Author：Huasong Huang
	 * CreateTime：2015-12-18 下午02:13:27
	 */
	public void writePathResultToFile(String folderName, String fileName, String ...contents) throws Exception {
		File file = new File(config.getDataPath() + File.separator + folderName + File.separator + fileName);
		BufferedWriter bw = Files.newWriter(file, Charsets.UTF_8);
		for(String content : contents){
			bw.write(content);
			bw.newLine();
		}
		bw.close();
	}
	
	

	private static JSONObject convertLineToJson(List<JSONObject> pointList, int[] parts) {
		if (pointList == null || pointList.size() == 0)
			throw new RuntimeException("pointList is null/empty");

		JSONObject pointJson = new JSONObject();
		pointJson.accumulate("point2Ds", pointList);
		pointJson.accumulate("parts", parts);
		JSONObject resultJson = new JSONObject();
		resultJson.accumulate("result", pointJson);
		return resultJson;
	}
	
	private static JSONObject convertLineToJson(List<JSONObject> pointList, List<Integer> parts) {
		if (pointList == null || pointList.size() == 0)
			throw new RuntimeException("pointList is null/empty");

		JSONObject pointJson = new JSONObject();
		pointJson.accumulate("point2Ds", pointList);
		pointJson.accumulate("parts", parts);
		JSONObject resultJson = new JSONObject();
		resultJson.accumulate("result", pointJson);
		return resultJson;
	}

	/**
	 * 从TransportationAnalystResult提取道路轨迹
	 * 
	 * @param result
	 * @param jsonPointList
	 * @param parts
	 * @param partIndex
	 */
	private void fetchResultForMTSPPath(TransportationAnalystResult result, List<JSONObject> jsonPointList, int[] parts, int partIndex) {

		GeoLineM[] linems = result.getRoutes();
		if (linems == null) {
			logger.info("result.getRoutes() == null");
		} else if (linems.length == 0) {
			logger.info("result.getRoutes().length == 0");
		} else {
			logger.info("linems.length = " + linems.length);
			for (int i = 0; i < linems.length; i++) {
				GeoLineM line = linems[i];

				int linePartCount = line.getPartCount();
				logger.info("linePartCount = " + linePartCount);
				// 假设linePartCount都是1
				PointMs part = line.getPart(0);
				int pointCount = part.getCount();
				logger.info("pointCount = " + pointCount);
				int reduceNum=0;
				for (int j = 0; j < pointCount; j++) {
					if(j!=0 && (j!=(pointCount-1)) && ((j%2)==0)){
						reduceNum++;
						continue;
					}
					PointM pointM = part.getItem(j);
					JSONObject jsonPoint = new JSONObject();
					// 将经纬度转为墨卡托点
					jsonPoint.accumulate("x", CoordinateTranslator.lngToMercator(pointM.getX()));
					jsonPoint.accumulate("y", CoordinateTranslator.latToMercator(pointM.getY()));
					jsonPointList.add(jsonPoint);
				}
				parts[partIndex] += (pointCount-reduceNum);
			}
		}
	}

	/**
	 * 获取中文转向信息
	 * @param result
	 */
	public String showResult(TransportationAnalystResult result ) {
		
		return fetchPathGuidInfo(result, null);
	
	}
	
	/**
	 * 
	 * <p>Title ：fetchPathGuidInfo</p>
	 * Description：		获取路线导航信息
	 * @param result
	 * @param distanceList	用于存储各个路线的里程，列表最后一个为总里程
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-12-18 上午10:44:17
	 */
	public String fetchPathGuidInfo(TransportationAnalystResult result,List<Double> distanceList){
		PathGuide[] pathGuides = result.getPathGuides();
		int k = 0;
		StringBuilder show = new StringBuilder();
		double totalLength = 0;
		String countLength = "";
		show.append("[");
		
		for (PathGuide pathGuide : pathGuides) {
			show.append("{index:").append(k).append(",text:\"");
			double length = 0;
			for (int j = 1; j < pathGuide.getCount(); j++) {
				PathGuideItem item = pathGuide.get(j);
				// 导引子项为站点的添加方式
				if (item.isStop()) {
					String side = "无";
					if (item.getSideType() == SideType.LEFT)
						side = "左侧";
					if (item.getSideType() == SideType.RIGHT)
						side = "右侧";
					if (item.getSideType() == SideType.MIDDLE)
						side = "道路上";
//					String dis = NumberFormat.getInstance().format(item.getDistance());
					String dis = Math.round( item.getDistance())+"米";
					if (item.getIndex() == -1 && item.getID() == -1) {
						continue;
					}
					if (j != pathGuide.getCount() - 1) {
						show.append("到达[").append(item.getIndex()-1).append("号订单点],在道路").append(side).append(dis).append("<br>");
					} else {
						show.append("到达终点,在道路").append(side).append(dis).append("<br>\",length:");
					}
				}
				
				// 导引子项为弧段的添加方式
				if (item.isEdge()) {
					String direct = "直行";
					if (item.getDirectionType() == DirectionType.EAST)
						direct = "东";
					if (item.getDirectionType() == DirectionType.WEST)
						direct = "西";
					if (item.getDirectionType() == DirectionType.SOUTH)
						direct = "南";
					if (item.getDirectionType() == DirectionType.NORTH)
						direct = "北";
					String weight = NumberFormat.getInstance().format(item.getWeight());
					String roadName = item.getName();
					double itemDistence=item.getLength()* config.getDistenceMeterPerDegree();
					if (weight.equals("0") && roadName.equals("")) {
						show.append("朝").append(direct).append("行走").append(Math.round(itemDistence)).append("米<br>");
					} else {
						String roadString = roadName.equals("") ? "匿名路段" : roadName;
						show.append("沿着").append(roadString).append(",朝").append(direct).append("行走")
						.append(Math.round(itemDistence)).append("米<br>");
//						show.append("沿着[").append(roadString).append("(EdgId:").append(item.getID()).append(")],朝").append(direct).append("行走")
//						.append(Math.round( item.getLength()*100000)).append("米<br>");
					}
					
					length += item.getLength();
				}
			}
			if(null != distanceList){
				distanceList.add(new Double(length));
			}
			totalLength += length;
			countLength += length + "_";
			show.append(length);
			if(k++==pathGuides.length-1){
				show.append("}");
			}else{
				show.append("},");
			}
		}
		countLength += totalLength;
		//show.append(countLength);
		show.append("]");
		if(null != distanceList){
			distanceList.add(new Double(totalLength));
		}
		logger.info("totalLength:"+totalLength);
		return show.toString();
	}
	
	/**
	 * 
	 * <p>Title ：getStopWeights</p>
	 * Description：		获取物流配送结果中站点之间的花费，findVRPPath
	 * @param result
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-12-3 下午02:50:54
	 */
	public List<List<Double>> getStopWeights(TransportationAnalystResult result){
		double[][] stopWeigths = result.getStopWeights();
		List<List<Double>> stopWeights = new ArrayList<List<Double>>();
		for (int i = 0; i < stopWeigths.length; i++) {
			List<Double> stopWeightsItem = new ArrayList<Double>();
			double[] pathGuide = stopWeigths[i];
			for(int j=0;j<pathGuide.length;j++){
				stopWeightsItem.add(pathGuide[j]);
			}
			stopWeights.add(stopWeightsItem);
		}
		return stopWeights;
	}
	/**
	 * 返回stopindexes 途径节点的顺序
	 * type:步长。通过遍历多个结果集的路径，需要合并为一个stopindex，则里面的值需要根据步长累加。
	 * @param result
	 * @return
	 */
	private String fetchStopIndexes(TransportationAnalystResult result,int step,int prefix){
		
		StringBuffer sb=new StringBuffer();
		sb.append("[");
		int [][] stopIndexs=result.getStopIndexes();
		int k=0;
		int lineNumber=result.getRoutes().length;
		if(lineNumber!=stopIndexs.length){
			k=lineNumber;
		}
		for (int i = k; i < stopIndexs.length; i++) {
			sb.append("[");
			int [] pathStopIndexs=stopIndexs[i];
			for (int j = prefix; j < pathStopIndexs.length; j++) {
				if(j==pathStopIndexs.length-1){
					sb.append(pathStopIndexs[j]+step);
				}else{
					sb.append(pathStopIndexs[j]+step).append(",");
				}
			}
			if(i==stopIndexs.length-1){
				sb.append("]");
			}else{
				sb.append("],");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * 返回stopinWeights 途径节点的权重（路径长度或耗时）
	 * @param result
	 * @return
	 */
	private String fetchStopWeights(TransportationAnalystResult result){
		List<List<Double>> stopWeights= getStopWeights(result);
		return JSONArray.fromObject(stopWeights).toString();
	}
	/**
	 * 从TransportationAnalystResult提取道路轨迹
	 */
	private void fetchResultForVRPPath(TransportationAnalystResult result, List<JSONObject> jsonPointList, int[] parts) {
		GeoLineM[] linems = result.getRoutes();
		DecimalFormat df = new DecimalFormat("#.00");
		if (linems == null) {
			logger.info("result.getRoutes() == null");
		} else if (linems.length == 0) {
			logger.info("result.getRoutes().length == 0");
		} else {
			logger.info("linems.length = " + linems.length);
			for (int i = 0; i < linems.length; i++) {
				GeoLineM line = linems[i];

				int linePartCount = line.getPartCount();
				logger.info("linePartCount = " + linePartCount);
				// 假设linePartCount都是1
				PointMs part = line.getPart(0);
				int pointCount = part.getCount();
				logger.info("pointCount = " + pointCount);
				parts[i] += pointCount;
				for (int j = 0; j < pointCount; j++) {
					PointM pointM = part.getItem(j);
					JSONObject jsonPoint = new JSONObject();
					// 将经纬度转为墨卡托点
					jsonPoint.accumulate("x", df.format(CoordinateTranslator.lngToMercator(pointM.getX())));
					jsonPoint.accumulate("y", df.format(CoordinateTranslator.latToMercator(pointM.getY())));
					jsonPointList.add(jsonPoint);
				}
			}
		}
	}
	
	// 从TransportationAnalystResult提取道路轨迹
	public void fetchResult(TransportationAnalystResult result, List<JSONObject> jsonPointList, int[] parts, int partIndex){
		GeoLineM[] linems = result.getRoutes();
		if (linems == null) {
			System.out.println("result.getRoutes() == null");
		} else if (linems.length == 0) {
			System.out.println("result.getRoutes().length == 0");
		} else {
			System.out.println("linems.length = " + linems.length);
			for (int i = 0; i < linems.length; i++) {
				GeoLineM line = linems[i];
				
				int linePartCount = line.getPartCount();
				System.out.println("linePartCount = " + linePartCount);
				// 假设linePartCount都是1
				PointMs part = line.getPart(0);
				int pointCount = part.getCount();
				System.out.println("pointCount = " + pointCount);
				parts[partIndex] += pointCount;
				for (int j = 0; j < pointCount; j++) {
					PointM pointM = part.getItem(j);
					JSONObject jsonPoint = new JSONObject();
					// 将经纬度转为墨卡托点
					jsonPoint.accumulate("x", CoordinateTranslator.lngToMercator(pointM.getX()));
					jsonPoint.accumulate("y", CoordinateTranslator.latToMercator(pointM.getY()));
					jsonPointList.add(jsonPoint);
				}
			}
		}

	}

	@Override
	public void generateDataByVRPathWithTimeRange(List<Point> netPoints, List<Point> orderPoints,
			List<PathPlanCar> pathPlanCars, String taskId, boolean withDetail, GroupType groupType,
			WeightNameType weightNameType) throws Exception {
		
		LinkedList<PathPlanOrder> readyToPlanOrders = new LinkedList<PathPlanOrder>();
		// 获取网点及订单信息
		Map<String,Object> mapObjects = routeTaskService.getNetsAndOrders(taskId);
		String orderIds[] = ((String) mapObjects.get("ordersIds")).split(",");
		RouteTaskEntity taskEntity = routeTaskService.findById(taskId);
		
		for (int i=0;i<orderPoints.size();i++) {
			PathPlanOrder planOrder = new PathPlanOrder();
			Point2D point2d = new Point2D(orderPoints.get(i).getLon(), orderPoints.get(i).getLat());
			planOrder.setId(orderIds[i]);
			planOrder.setP(point2d);
			
		}
		// 网点
		List<Point2D> netPoints2 = new ArrayList<Point2D>();
		for (Point point : netPoints) {
			Point2D point2d = new Point2D(point.getLon(), point.getLat());
			netPoints2.add(point2d);
		}

	}
}
