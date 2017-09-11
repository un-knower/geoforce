package com.supermap.egispservice.pathplan.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.supermap.data.Point2D;
import com.supermap.egispservice.area.service.IAreaService;
import com.supermap.egispservice.base.entity.GroupType;
import com.supermap.egispservice.base.entity.PathPlanCar;
import com.supermap.egispservice.base.entity.Point;
import com.supermap.egispservice.base.entity.RouteMapTaskCarEntity;
import com.supermap.egispservice.base.entity.RouteMapTaskNetEntity;
import com.supermap.egispservice.base.entity.RouteMapTaskOrderEntity;
import com.supermap.egispservice.base.entity.RouteTaskEntity;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.base.entity.WeightNameType;
import com.supermap.egispservice.base.service.ILogisticsOrderService;
import com.supermap.egispservice.pathplan.constant.Config;
import com.supermap.egispservice.pathplan.dao.RouteMapTaskCarDao;
import com.supermap.egispservice.pathplan.dao.RouteMapTaskNetDao;
import com.supermap.egispservice.pathplan.dao.RouteMapTaskOrderDao;
import com.supermap.egispservice.pathplan.dao.RouteTaskDao;
import com.supermap.egispservice.pathplan.pojo.PathPlanOrder;
import com.supermap.egispservice.pathplan.pojo.RouteTaskConstants;
import com.supermap.egispservice.pathplan.util.BeanTool;
import com.supermap.egispservice.pathplan.util.FileUtil;
import com.supermap.egispservice.pathplan.util.QuartzJobFactory;
import com.supermap.egispservice.pathplan.util.SuggestPathJobFactory;

/**
 * 
 * @description 路径分析服务
 * @author CaoBin mailto:caobin@supermap.com
 * @company SuperMap Software Co., Ltd.
 * @createDate 2014-10-14
 * @version 1.0
 */
@Transactional
@Service
public class RouteTaskServiceImpl implements IRouteTaskService {

	private final Logger logger = Logger.getLogger(RouteTaskServiceImpl.class);
	@Autowired
	private RouteTaskDao routeTaskDao;
	@Autowired
	private RouteMapTaskCarDao routeMapTaskCarDao;
	@Autowired
	private IPathPlanService pathPlanService;
	@Autowired
	private IPathPlanJobService pathPlanJobService;
	@Autowired
	private RouteMapTaskNetDao routeMapTaskNetDao;
	@Autowired
	private RouteMapTaskOrderDao routeMapTaskOrderDao;
	@Autowired
	private IAreaService areaService;
	@Autowired
	private Config config;
	@Autowired
	private ILogisticsOrderService logisticsOrderService;

	/**
	 * 保存任务，并在指定时间执行
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	@Transactional
	public Map<String, Object> saveJobAndRun(String carsProperties,String netcoord, String ordersCoords, String areaName, String areaId, String ordersIds, int ordersCount, String netid,
			Date jobRunTime, Date jobstarttime, Date jobendtime, UserEntity user,String taskName,int typeAnalysis,int typeTrac,int typePath )   {
		Map<String, Object> m = new HashMap<String, Object>();
		
		//判断用户设置类型
		GroupType groupType=GroupType.NoneGroup;
		if(typePath==0){
			groupType=GroupType.NoneGroup;
		}else{
			groupType=GroupType.RadialGroup;
		}
		WeightNameType weightNameType=WeightNameType.MileAndTruck;
		if(typeAnalysis==0 && typeTrac==0){
			weightNameType=WeightNameType.MileAndTruck;
		}else if(typeAnalysis==0 && typeTrac==1){
			weightNameType=WeightNameType.MileBlockTruck;
		}else if(typeAnalysis==1 && typeTrac==0){
			weightNameType=WeightNameType.TimeAndTruck;
		}else if(typeAnalysis==1 && typeTrac==1){
			weightNameType=WeightNameType.TimeBlockTruck;
		}
		
		//设置车辆信息
		List<PathPlanCar> pathPlanCars=new ArrayList<PathPlanCar>();
		if(carsProperties.indexOf(";")>0){
			String [] carsArray=carsProperties.split(";");
			for (String car : carsArray) {
				pathPlanCars.add(createCarByProperties(car));
			}
		}else{
			pathPlanCars.add(createCarByProperties(carsProperties));
		}
		
		//TODO 订单总体积与总重量，默认按0.3长*0.3宽*0.3高（米）的体积算，因为其他页面还没有具体数据，需要批量导入支持。
		//总重量按(3KG=6斤/单)*数量 需要其他接口支持实时数据
		double orderTotalVolume=0.3*0.3*0.3*ordersCount;
		double orderTotalLoadWeight=3*ordersCount;
		//获取总体积与总重量
		//车辆
		double carTotalVolume=0;
		double carTotalLoadWeight=0;
		double loadOrderNumPerCar=Math.ceil(Double.valueOf(ordersCount)/pathPlanCars.size());
		for (PathPlanCar pathPlanCar : pathPlanCars) {
			carTotalVolume+=pathPlanCar.getVolume();
			carTotalLoadWeight+=pathPlanCar.getLoadWeight();
			//设置每个车辆应装的订单数量
			pathPlanCar.setLoadOrderNumber(loadOrderNumPerCar);
		}
		
		
		if(orderTotalVolume>carTotalVolume ||orderTotalLoadWeight>carTotalLoadWeight){
			m.put("flag", "error");
			m.put("info", "操作失败！订单总体积大于车辆总体积或订单总重量大于车辆总载重。请调整相关订单或车辆数后重试.");
			return m;
		}
		
		
		// 1.保存任务
		RouteTaskEntity taskEntity = new RouteTaskEntity();
		taskEntity.setPlanTypeId(Byte.valueOf("1"));// 默认最优路径
		taskEntity.setAreaId(areaId);
		taskEntity.setAreaName(areaName);
		taskEntity.setOrderCount(ordersCount);
		taskEntity.setTaskStatusId(Byte.valueOf("6"));// 定时设置
		taskEntity.setPlanTime(new Date());
		taskEntity.setDeliveryStartTime(new Date());
		taskEntity.setDeliveryEndTime(new Date());
		taskEntity.setCreateTime(new Date());
		taskEntity.setDeleteFlag(Byte.valueOf("0"));// 没有使用，默认0.
		taskEntity.setUserId(user);
		taskEntity.setEnterpriseId(user.getEid());
		taskEntity.setDepartmentId(user.getDeptId());
		taskEntity.setTaskName(taskName);
		taskEntity.setTypeAnalysis(String.valueOf(typeAnalysis));//分析模式
		taskEntity.setTypePath(String.valueOf(typePath));//放射线路
		taskEntity.setTypeTrac(String.valueOf(typeTrac));//货车模式
		routeTaskDao.save(taskEntity);
		
		//保存车辆关系到数据库
		for (PathPlanCar pathPlanCar : pathPlanCars) {
			RouteMapTaskCarEntity entity=new RouteMapTaskCarEntity();
			entity.setCarId(pathPlanCar.getCarId());
			entity.setCarName(pathPlanCar.getCarPlate());
			entity.setTaskId(taskEntity.getId());
			routeMapTaskCarDao.save(entity);
		}

		// 2.保存网点
		RouteMapTaskNetEntity taskNetEntity = new RouteMapTaskNetEntity();
		taskNetEntity.setNetId(netid);
		taskNetEntity.setTaskId(taskEntity.getId());
		routeMapTaskNetDao.save(taskNetEntity);
		// 3.保存订单
		if (ordersCount == 1) {
			RouteMapTaskOrderEntity taskOrderEntity = new RouteMapTaskOrderEntity();
			taskOrderEntity.setOrderId(ordersIds);
			taskOrderEntity.setTaskId(taskEntity.getId());
			routeMapTaskOrderDao.save(taskOrderEntity);
		} else {
			
			String[] ordersIdsArray = ordersIds.split(",");
			List<RouteMapTaskOrderEntity> list=new ArrayList<RouteMapTaskOrderEntity>(); 
			for (int i = 0; i < ordersIdsArray.length; i++) {
				RouteMapTaskOrderEntity taskOrderEntity = new RouteMapTaskOrderEntity();
				taskOrderEntity.setOrderId(ordersIdsArray[i]);
				taskOrderEntity.setTaskId(taskEntity.getId());
				list.add(taskOrderEntity);
			}
			
			
//			List<PathPlanOrder> orders = FileUtil.getPutianData(config.getPutianDataPath(),true);
//			for (int i = 0; i < orders.size(); i++) {
//				RouteMapTaskOrderEntity taskOrderEntity = new RouteMapTaskOrderEntity();
//				taskOrderEntity.setOrderId(orders.get(i).getOrderId());
//				taskOrderEntity.setTaskId(taskEntity.getId());
//				list.add(taskOrderEntity);
//			}
			routeMapTaskOrderDao.save(list);
		}
		
		try {
			// 4.保存网点与订单数据，车辆数据
			createFiles(taskEntity.getId());
			writeNetsAndOrdersFiles(taskEntity.getId(), netid, ordersIds, netcoord, ordersCoords,pathPlanCars);
			// 5.添加任务
			pathPlanService.planByVRPPath(taskEntity.getId(), false, false, groupType, weightNameType,QuartzJobFactory.class);
			m.put("flag", "ok");
		} catch (Exception e) {
			m.put("flag", "error");
			e.printStackTrace();
		}
		
		return m;
	}
	

	/**
	 * 
	 * <p>Title ：saveJobAndRun</p>
	 * Description：		保存线路规划工作，并运行
	 * @param netId		网点ID
	 * @param netCoord	网点坐标
	 * @param orderIds	订单Id列表，使用逗号分隔
	 * @param orderCoords	订单坐标，使用分号分隔，每对坐标第一个为经度，第二个为纬度，
	 * 						示例：121.43870568995496,31.21282983470203,121.47985517750072,31.296681193569263
	 * @param orderTimes	订单是配送时间列表，所有时间使用逗号相隔，第一个为开始时间，第二个为结束时间，
	 * 						示例：8:00,9:00,8:00,9:00
	 * @param user			使用者信息
	 * @param taskName		任务名称
	 * @param carLoad		车辆负载量
	 * @param fixedConsumeMiniute	固定消耗时间
	 * @param pathType		路线类型,分为常规模式和放射模式
	 * @param anlystType	分析类型，时间最短和路程最短
	 * @param carType		车辆通行类型
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-12-16 下午03:22:42
	 */
	@Transactional
	public Map<String, Object> saveJobAndRun(String netId, String netCoord, String orderIds, String orderCoords,
			String orderTimes, String areaId, String areaName, UserEntity user, String taskName, int carLoad,
			double fixedConsumeMiniute, int pathType, int anlystType, int carType,String batchTimeStart,String batchTimeEnd){
		
		Map<String, Object> m = new HashMap<String, Object>();
		//判断用户设置类型
		GroupType groupType=GroupType.NoneGroup;
		if (pathType == 0) {
			groupType = GroupType.NoneGroup;
		} else {
			groupType = GroupType.RadialGroup;
		}
		
		WeightNameType weightNameType = WeightNameType.MileAndTruck;
		if (anlystType == 0 && carType == 0) {
			weightNameType = WeightNameType.MileAndTruck;
		} else if (anlystType == 0 && carType == 1) {
			weightNameType = WeightNameType.MileBlockTruck;
		} else if (anlystType == 1 && carType == 0) {
			weightNameType = WeightNameType.TimeAndTruck;
		} else if (anlystType == 1 && carType == 1) {
			weightNameType = WeightNameType.TimeBlockTruck;
		}
		// 剔除不满足时间要求的订单
		String[] batchTimeStartItems = batchTimeStart.split(":");
		int startH = Integer.parseInt(batchTimeStartItems[0]);
		int startM = Integer.parseInt(batchTimeStartItems[1]);
		int startMM = startH * 60 + startM;

		String[] batchTimeEndItems = batchTimeEnd.split(":");
		int endH = Integer.parseInt(batchTimeEndItems[0]);
		int endM = Integer.parseInt(batchTimeEndItems[1]);
		int endMM = endH * 60 + endM;
		
		
		
		String[] orderIdArr = orderIds.split(",");
		String[] orderCoordArr = orderCoords.split(",");
		String[] orderTimeArr = orderTimes.split(",");
		
		
		
		StringBuilder orderIdBuilder = new StringBuilder();
		StringBuilder orderCoorBuilder = new StringBuilder();
		StringBuilder orderTimeBuilder = new StringBuilder();
		for(int i = 0;i<orderIdArr.length;i++){
			String[] startItems = orderTimeArr[i * 2].split(":");
			int orderStartH = Integer.parseInt(startItems[0]);
			int orderStartM = Integer.parseInt(startItems[1]);
			int orderStartMM = orderStartH * 60 + orderStartM;
			String endItems[] = orderTimeArr[i * 2 + 1].split(":");
			
			int orderEndH = Integer.parseInt(endItems[0]);
			int orderEndM = Integer.parseInt(endItems[1]);
			int orderEndMM = orderEndH * 60 + orderEndM;
			if(orderStartMM >= startMM && orderEndMM <= endMM && orderStartMM < orderEndMM){
				orderIdBuilder.append(orderIdArr[i]).append(",");
				orderCoorBuilder.append(orderCoordArr[i * 2]).append(",").append(orderCoordArr[i * 2 + 1]).append(",");
				orderTimeBuilder.append(orderTimeArr[i * 2]).append(",").append(orderTimeArr[i * 2 + 1]).append(",");
			}else{
				logger.info("## 剔除[start:" + orderTimeArr[i * 2] + ",end:" + orderTimeArr[i * 2 + 1] + "],[id:"
						+ orderIdArr[i] + "]");
			}
		}
		orderIds = orderIdBuilder.toString().substring(0,orderIdBuilder.length() - 1);
		orderCoords = orderCoorBuilder.toString().substring(0,orderCoorBuilder.length() - 1);
		orderTimes = orderTimeBuilder.toString().substring(0,orderTimeBuilder.length() - 1);
		
		
		// 1.保存任务
		RouteTaskEntity taskEntity = new RouteTaskEntity();
		taskEntity.setPlanTypeId(RouteTaskConstants.PLAN_TYPE_BEST_PATH);// 默认最优路径
		taskEntity.setAreaId(areaId);
		taskEntity.setAreaName(areaName);
		taskEntity.setOrderCount(orderIds.split(",").length);
		taskEntity.setTaskStatusId(RouteTaskConstants.TASK_STATUS_TIMING_SETTING);// 定时设置
		taskEntity.setPlanTime(new Date());
		taskEntity.setDeliveryStartTime(new Date());
		taskEntity.setDeliveryEndTime(new Date());
		taskEntity.setCreateTime(new Date());
		taskEntity.setDeleteFlag(RouteTaskConstants.DELETE_FLAG);// 没有使用，默认0.
		taskEntity.setUserId(user);
		taskEntity.setEnterpriseId(user.getEid());
		taskEntity.setDepartmentId(user.getDeptId());
		taskEntity.setTaskName(taskName);
		taskEntity.setTypeAnalysis(String.valueOf(anlystType));//分析模式
		taskEntity.setTypePath(String.valueOf(pathType));//放射线路
		taskEntity.setTypeTrac(String.valueOf(carType));//货车模式
		routeTaskDao.save(taskEntity);
		
		try {
			// 4.保存网点与订单数据，车辆数据
			createFiles(taskEntity.getId());
			// 将参数写入到文件中，行顺序为：网点id，网点坐标，订单id，订单坐标，车辆信息，订单时间，车辆负载，固定消耗时间,批次开始时间，批次结束时间
			writePathPlanParamToFile(taskEntity.getId(), netId, netCoord, orderIds, orderCoords, "", orderTimes,
					carLoad + "", fixedConsumeMiniute + "", batchTimeStart, batchTimeEnd);
			pathPlanService.planByVRPPath(taskEntity.getId(), false, false, groupType, weightNameType,SuggestPathJobFactory.class);
			m.put("flag", "ok");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			m.put("flag", "error");
		}
		
		return m;
	}
	

	private PathPlanCar createCarByProperties(String carProperties){
		String[] properties=carProperties.split(",");
		PathPlanCar car=new PathPlanCar();
		car.setCarLength(Double.valueOf(properties[0]));
		car.setCarWidth(Double.valueOf(properties[1]));
		car.setCarHeight(Double.valueOf(properties[2]));
		car.setVolume(Double.valueOf(properties[3]));
		car.setLoadWeight(Double.valueOf(properties[4]));
		car.setCarId(properties[5]);
		car.setCarPlate(properties[6]);
		return car;
	}
	/**
	 * 根据任务ID号创建文件夹，文件夹下存放path.json，nets_orders.data
	 * path.json 存储路径规划结果
	 * nets_orders.data 第一行内容：网点ID，第二话内容：网点坐标；第三行内容：订单id;第四行内容：订单坐标
	 */
	private void createFiles(String folderName) throws Exception {

		String pathName = config.getDataPath() + folderName;
		File file = new File(pathName);
		if (file.exists() && file.isDirectory()) {
			FileUtil.delFolder(pathName);
		}
		file.mkdir();
		Files.touch(new File(pathName + File.separator + "path.json"));
		Files.touch(new File(pathName + File.separator + "nets_orders.data"));
		logger.info("创建文件path.json，nets_orders.data成功");
	}

	/**
	 * nets_orders.data 第一行内容：网点ID，第二行内容：网点坐标；第三行内容：订单id;第四行内容：订单坐标
	 */
	private void writeNetsAndOrdersFiles(String folderName, String netid, String ordersIds, String netcoord, String ordersCoords,List<PathPlanCar> pathPlanCars) throws Exception {
		String carString="";
		for (int i = 0; i < pathPlanCars.size(); i++) {
			PathPlanCar car=pathPlanCars.get(i);
			//拼接字符串
			carString+=car.toString();
			if(i!=(pathPlanCars.size()-1)){
				carString+=";";
			}
		}
		writePathPlanParamToFile(folderName, netid, netcoord, ordersIds, ordersCoords, carString);
		logger.info("向nets_orders.data写入数据成功");
	}
	
	/**
	 * 
	 * <p>Title ：writePathPlanParamToFile</p>
	 * Description：		将用于路线规划的参数写入到文件中
	 * @param folderName
	 * @param params
	 * @throws IOException
	 * Author：Huasong Huang
	 * CreateTime：2015-12-17 下午02:13:37
	 */
	private void writePathPlanParamToFile(String folderName,String...params) throws IOException{
		String pathName = config.getDataPath() + folderName;
		File netsAndOrdersDataFile = new File(pathName + File.separator + "nets_orders.data");
		BufferedWriter bw = Files.newWriter(netsAndOrdersDataFile, Charsets.UTF_8);
		for(String param : params){
			bw.write(param);
			bw.newLine();
		}
		bw.close();
	}

	/**
	 * 获取任务相关的网点与订单数据
	 * netid:网点id
	 * netcoord：网点坐标
	 * ordersIds：订单id
	 * ordersCoords：订单坐标
	 * pathPlanCars:本次任务安排车辆
	 * @throws IOException
	 */
	@Override
	public Map<String, Object> getNetsAndOrders(String taskId) throws IOException {
		File file = new File(config.getDataPath() + File.separator + taskId + File.separator + "nets_orders.data");
		List<String> lines = Files.readLines(file, Charsets.UTF_8);
		Map<String, Object> m = new HashMap<String, Object>();
		for (int i = 0; i < lines.size(); i++) {
			if (i == 0) {
				m.put(RouteTaskConstants.PARAM_FIELD_NET_ID, lines.get(i));
			} else if (i == 1) {
				m.put(RouteTaskConstants.PARAM_FIELD_NET_COORD, lines.get(i));
			} else if (i == 2) {
				m.put(RouteTaskConstants.PARAM_FIELD_ORDER_IDS, lines.get(i));
			} else if (i == 3) {
				m.put(RouteTaskConstants.PARAM_FIELD_ORDER_COORDS, lines.get(i));
			} else if (i == 4) {
				m.put(RouteTaskConstants.PARAM_FIELD_CARS, lines.get(i));
			}else if(i == 5){
				m.put(RouteTaskConstants.PARAM_FIELD_ORDER_TIME_RANGE, lines.get(i));
			}else if(i==6){
				m.put(RouteTaskConstants.PARAM_FIELD_ORDER_CAR_LOAD, lines.get(i));
			}else if(i == 7){
				m.put(RouteTaskConstants.PARAM_FIELD_ORDER_FIXED_MIN, lines.get(i));
			}else if(i == 8){
				m.put(RouteTaskConstants.PARAM_FIELD_ORDER_BATCH_TIME_START, lines.get(i));
			}else if(i == 9){
				m.put(RouteTaskConstants.PARAM_FIELD_ORDER_BATCH_TIME_END, lines.get(i));
			}
		}
		
		return m;
	}
	
	/**
	 * 
	 * <p>Title ：getNetsAndOrdersObj</p>
	 * Description：	获取订单和网点信息
	 * @param taskId
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-12-17 上午10:05:56
	 */
	public Map<String,Object> getNetsAndOrdersObj(String taskId){
		Map<String,Object> netOrdersMap = new HashMap<String,Object>();
		try {
			Map<String,Object> netsOrders = getNetsAndOrders(taskId);
			
			// 网点ID
			String netIds = (String) netsOrders.get(RouteTaskConstants.PARAM_FIELD_NET_ID);
			List<String> netIdList = new ArrayList<String>();
			String[] idItems = netIds.split(",");
			for(String idItem : idItems){
				netIdList.add(idItem);
			}
			netOrdersMap.put(RouteTaskConstants.PARAM_FIELD_NET_ID, netIdList);
			// 网点坐标
			String netCoordStr = (String) netsOrders.get(RouteTaskConstants.PARAM_FIELD_NET_COORD);
			String[] netCoordArray =netCoordStr.split(",");
			List<Point2D> netCoordList = new ArrayList<Point2D>();
			for(int i=0;i<netCoordArray.length;i+=2){
				Point2D p = new Point2D();
				p.setX(Double.valueOf(netCoordArray[i]));
				p.setY(Double.valueOf(netCoordArray[i + 1]));
				netCoordList.add(p);
			}
			netOrdersMap.put(RouteTaskConstants.PARAM_FIELD_NET_COORD, netCoordList);
			// 订单ID
			String orderIdStr = (String) netsOrders.get(RouteTaskConstants.PARAM_FIELD_ORDER_IDS);
			String orderIdItems[] = orderIdStr.split(",");
			List<String> orderIdList = new ArrayList<String>();
			for(String orderIdItem : orderIdItems){
				orderIdList.add(orderIdItem);
			}
			netOrdersMap.put(RouteTaskConstants.PARAM_FIELD_ORDER_IDS, orderIdList);
			// 订单坐标
			String orderCoordsStr = (String) netsOrders.get(RouteTaskConstants.PARAM_FIELD_ORDER_COORDS);
			String[] orderCoordItems = orderCoordsStr.split(",");
			List<Point2D> orderCoordList = new ArrayList<Point2D>();
			for (int i = 0; i < orderCoordItems.length; i += 2) {
				Point2D p = new Point2D();
				p.setX(Double.valueOf(orderCoordItems[i]));
				p.setY(Double.valueOf(orderCoordItems[i + 1]));
				orderCoordList.add(p);
			}
			netOrdersMap.put(RouteTaskConstants.PARAM_FIELD_ORDER_COORDS, orderCoordList);
			// 订单时间
			String orderTimeStr = (String) netsOrders.get(RouteTaskConstants.PARAM_FIELD_ORDER_TIME_RANGE);
			String orderTimeItems[] = orderTimeStr.split(",");
			List<String> orderTimeList = new ArrayList<String>();
			for (int i = 0; i < orderTimeItems.length; i += 2) {
				orderTimeList.add(orderTimeItems[i]);
				orderTimeList.add(orderTimeItems[i+1]);
			}
			netOrdersMap.put(RouteTaskConstants.PARAM_FIELD_ORDER_TIME_RANGE, orderTimeList);
			netOrdersMap.put(RouteTaskConstants.PARAM_FIELD_CARS, netsOrders.get(RouteTaskConstants.PARAM_FIELD_CARS));
			// 车辆负载
			String carLoadStr = (String) netsOrders.get(RouteTaskConstants.PARAM_FIELD_ORDER_CAR_LOAD);
			String fixedMinStr = (String) netsOrders.get(RouteTaskConstants.PARAM_FIELD_ORDER_FIXED_MIN); 
			netOrdersMap.put(RouteTaskConstants.PARAM_FIELD_ORDER_CAR_LOAD, (int)Double.parseDouble(carLoadStr));
			netOrdersMap.put(RouteTaskConstants.PARAM_FIELD_ORDER_FIXED_MIN, (int)Double.parseDouble(fixedMinStr));
			// 批次起始时间
			String batchTimeStartStr = (String) netsOrders.get(RouteTaskConstants.PARAM_FIELD_ORDER_BATCH_TIME_START);
			String[] timeStartItems = batchTimeStartStr.split(":");
			int startH = Integer.parseInt(timeStartItems[0]);
			int startM = Integer.parseInt(timeStartItems[1]);
			Calendar startCal = Calendar.getInstance();
			startCal.set(Calendar.HOUR_OF_DAY, startH);
			startCal.set(Calendar.MINUTE, startM);
			netOrdersMap.put(RouteTaskConstants.PARAM_FIELD_ORDER_BATCH_TIME_START, startCal.getTimeInMillis());
			
			String batchTimeEndStr =  (String) netsOrders.get(RouteTaskConstants.PARAM_FIELD_ORDER_BATCH_TIME_END);
			String[] timeEndItems = batchTimeEndStr.split(":");
			int endH = Integer.parseInt(timeEndItems[0]);
			int endM = Integer.parseInt(timeEndItems[1]);
			Calendar endCal = Calendar.getInstance();
			endCal.set(Calendar.HOUR_OF_DAY, endH);
			endCal.set(Calendar.MINUTE, endM);
			netOrdersMap.put(RouteTaskConstants.PARAM_FIELD_ORDER_BATCH_TIME_END,endCal.getTimeInMillis());
			
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return netOrdersMap;
	}

	/**
	 * 得到线路规划结果（路径，区域，网点，订单）
	 */
	@Override
	public Map<String, Object> getResult(String taskId,String areaId,String deptId) throws Exception {
		Map<String, Object> m = getNetsAndOrders(taskId);
		Map<String, Object> pathFileContent=getResultPath(taskId);
		m.put("path", pathFileContent.get("path")); 
		m.put("stopIndexes", pathFileContent.get("stopIndexes")); 
		m.put("pathGuides", pathFileContent.get("pathGuides")); 
		m.put("area", areaService.queryByIdOrNumber(areaId, "", deptId, true));
		if(pathFileContent.containsKey("pathWeights")){
			m.put("pathWeights", pathFileContent.get("pathWeights"));
		}
		if(pathFileContent.containsKey("lengths")){
			m.put("lengths",pathFileContent.get("lengths"));
		}
		if(pathFileContent.containsKey("totalLength")){
			m.put("totalLength",pathFileContent.get("totalLength"));
		}
		if(pathFileContent.containsKey("stopTimes")){
			m.put("stopTimes", pathFileContent.get("stopTimes"));
		}
//		String orderIds = (String) m.get(RouteTaskConstants.PARAM_FIELD_ORDER_IDS);
//		String[] orderIdItems = orderIds.split(",");
//		List<String> addrList = new ArrayList<String>();
//		List<Map<String,Object>> orderList = this.logisticsOrderService.queryByIds(orderIdItems);
//		for(Map<String,Object> orderInfo: orderList){
//			addrList.add((String)orderInfo.get("address"));
//		}
//		m.put("address", addrList);
		return m;
	}

	/**
	 * 获取任务对应的线路规划结果
	 * 
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> getResultPath(String taskId) throws Exception {
		Map<String, Object> m=new HashMap<String, Object>();
		File file = new File(config.getDataPath() + File.separator + taskId + File.separator + "path.json");
		List<String> lines = Files.readLines(file, Charsets.UTF_8);
		for (int i = 0; i < lines.size(); i++) {
			if (i == 0) {
				m.put("path", lines.get(i));
			} else if (i == 1) {
				m.put("stopIndexes", lines.get(i));
			} else if (i == 2) {
				m.put("pathGuides", lines.get(i));
			}else if(i == 3){
				m.put("pathWeights", lines.get(i));
			}else if(i == 4){
				m.put("lengths", lines.get(i));
			}else if(i == 5){
				m.put("totalLength", lines.get(i));
			}else if(i == 7){
				m.put("stopTimes", lines.get(i));
			}
		}
		return m;
	}

	@Override
	public Map<String, Object> getTasks(final String userid, final String eid, final String deptId, final String taskId, final Byte taskStatusId,
			final String areaName, int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize, sortType);
		Specification<RouteTaskEntity> spec = new Specification<RouteTaskEntity>() {
			@Override
			public Predicate toPredicate(Root<RouteTaskEntity> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
				Path<String> userid2 = root.get("userId").get("id");
				Path<String> eid2 = root.get("enterpriseId").get("id");
				Path<String> deptId2 = root.get("departmentId").get("id");
				Path<String> taskId2 = root.get("taskName");//2015-1-5 11:04:21 修改为查询任务名称
				Path<Byte> taskStatusId2 = root.get("taskStatusId");
				Path<String> areaName2 = root.get("areaName");
				if (StringUtils.isNoneEmpty(areaName)) {
					Predicate p = builder.like(areaName2, "%" + areaName + "%");
					predicateList.add(p);
				}
				Predicate useridPredicate = builder.equal(userid2, userid);
				predicateList.add(useridPredicate);

				Predicate eidPredicate = builder.equal(eid2, eid);
				predicateList.add(eidPredicate);

				Predicate deptIdPredicate = builder.equal(deptId2, deptId);
				predicateList.add(deptIdPredicate);

				if (StringUtils.isNoneEmpty(taskId)) {
					Predicate taskIdPredicate = builder.like(taskId2, "%" + taskId + "%"); 
					predicateList.add(taskIdPredicate);
				}

				if (taskStatusId != 0) {
					Predicate taskStatusIdPredicate = builder.equal(taskStatusId2, taskStatusId);
					predicateList.add(taskStatusIdPredicate);
				}

				Predicate[] predicates = new Predicate[predicateList.size()];
				predicateList.toArray(predicates);
				query.where(predicates);
				return null;
			}

		};
		Page<RouteTaskEntity> page = routeTaskDao.findAll(spec, pageRequest);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("total", page.getTotalPages());
		m.put("page", pageNumber);
		m.put("records", page.getTotalElements());
		m.put("rows", page.getContent());
		return m;
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize, String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.supermap.egispservice.pathplan.service.IRouteTaskService#save(com.supermap.egispservice.pathplan.entity.RouteTaskEntity)
	 */
	@Override
	public void save(RouteTaskEntity entity) {
		routeTaskDao.save(entity);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.supermap.egispservice.pathplan.service.IRouteTaskService#findAll()
	 */
	@Override
	public List<RouteTaskEntity> findAll() {
		return Lists.newArrayList(routeTaskDao.findAll());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.supermap.egispservice.pathplan.service.IRouteTaskService#findById(java.lang.String)
	 */
	@Override
	public RouteTaskEntity findById(String id) {
		return routeTaskDao.findOne(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.supermap.egispservice.pathplan.service.IRouteTaskService#update(com.supermap.egispservice.pathplan.entity.RouteTaskEntity)
	 */
	@Override
	public void update(RouteTaskEntity entity) {
		RouteTaskEntity entity2 = findById(entity.getId());
		BeanUtils.copyProperties(entity, entity2, BeanTool.getNullPropertyNames(entity));
		routeTaskDao.save(entity2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.supermap.egispservice.pathplan.service.IRouteTaskService#deleteById(java.lang.String)
	 */
	@Override
	public boolean deleteById(String id) throws Exception {
		boolean flag=false;
//		if (id.indexOf(",") > 0) {
//			List<String> idList = new ArrayList<String>();
//			String[] ids = id.split(",");
//			for (String idString : ids) {
//				idList.add(idString);
//				pathPlanJobService.deleteJob(idString);
//				String pathName = config.getDataPath() + idString;
//				File file = new File(pathName);
//				if (file.exists() && file.isDirectory()) {
//					FileUtil.delFolder(pathName);
//				}
//			}
//			routeTaskDao.deleteByIds(idList);
//			flag=true;
//		} else {
		// 记得恢复
//			RouteTaskEntity taskEntity = routeTaskDao.findOne(id);
//			if(taskEntity.getTaskStatusId()==7){
//				flag=false;
//			}else{
				pathPlanJobService.deleteJob(id);
				routeTaskDao.delete(id);
				String pathName = config.getDataPath() + id;
				File file = new File(pathName);
				if (file.exists() && file.isDirectory()) {
					FileUtil.delFolder(pathName);
				}
				flag=true;
//			}
//		}
		return flag;
	}
}
