package com.supermap.egispservice.pathplan.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 * @description supermap objects java需要的数据属性常量
 * @author CaoBin mailto:caobin@supermap.com
 * @company SuperMap Software Co., Ltd.
 * @createDate 2014-10-10
 * @version 1.0
 */
@Component
public class Config {
	/**
	 * 连接方式 .1：udb  2:oracle
	 */
	@Value("#{configParam['swap']}")
	private int swap;

	/**
	 * 工作空间smwu
	 */
	@Value("#{configParam['workspacePath']}")
	private String workspacePath;
	/**
	 * 网络数据集
	 */
	@Value("#{configParam['networkName']}")
	private String networkName;
	/**
	 * 转向表
	 */
	@Value("#{configParam['turnTableName']}")
	private String turnTableName;
	/**
	 * weightName
	 */
	@Value("#{configParam['weightName']}")
	private String weightName;
	/**
	 * direction
	 */
	@Value("#{configParam['direction']}")
	private String direction;
	/**
	 * tolerance
	 */
	@Value("#{configParam['tolerance']}")
	private double tolerance;
	/**
	 * DISTENCE_METER_PER_DEGREE
	 */
	@Value("#{configParam['DISTENCE_METER_PER_DEGREE']}")
	private double distenceMeterPerDegree;

	/**
	 * fromToEndIsProhibited
	 */
	@Value("#{configParam['fromToEndIsProhibited']}")
	private String fromToEndIsProhibited;
	/**
	 * dataPath
	 */
	@Value("#{configParam['dataPath']}")
	private String dataPath;

	/**
	 * findMTSPPath使用，车辆负载订单数，值越大，线路越少，最低为1条先。越小，线路越多，及规划多辆车
	 */
	@Value("#{configParam['carLoad']}")
	private int carLoad;
	/**
	 * FindVRPPath使用，车辆负载订单数，值越大，线路越少，最低为1条先。越小，线路越多，及规划多辆车
	 */
	@Value("#{configParam['loadWeight']}")
	private double loadWeight;
	/**
	 * FindVRPPath使用，车辆的最大耗费值
	 */
	@Value("#{configParam['cost']}")
	private double cost;

	/**
	 * 数据源
	 */
	@Value("#{configParam['API_DATASOURCE_URL']}")
	private String apiDataSourceUrl;

	/**
	 * 数据库URL
	 */
	@Value("#{configParam['API_DBURL']}")
	private String apiDBUrl;

	/**
	 * 驱动
	 */
	@Value("#{configParam['API_DBDRIVER']}")
	private String apiDBDriver;

	/**
	 * 用户名
	 */
	@Value("#{configParam['API_USERNAME']}")
	private String apiUsername;

	/**
	 * 密码
	 */
	@Value("#{configParam['API_PASSWORD']}")
	private String apiPassword;

	/**
	 * 网络数据集
	 */
	@Value("#{configParam['API_NETWORKNAME']}")
	private String apiNetWorkName;

	/**
	 * 转向表
	 */
	@Value("#{configParam['API_TURNTABLENAME']}")
	private String apiTurnTableName;

	/**
	 * 转向表
	 */
	@Value("#{configParam['loadOrderNumber']}")
	private int loadOrderNumber;

	/**
	 * 距离下标
	 */
	@Value("#{configParam['distance.index']}")
	private int distanceIndex;
	/**
	 * 时间下标
	 */
	@Value("#{configParam['time.index']}")
	private int timeIndex;
	/**
	 * 卸货时间（分钟）
	 */
	@Value("#{configParam['order.unload.minutes']}")
	private int orderUnloadMiniutes;
	
	
	public Config() {
		super();
	}

	public String getWorkspacePath() {
		return workspacePath;
	}

	public void setWorkspacePath(String workspacePath) {
		this.workspacePath = workspacePath;
	}

	public String getNetworkName() {
		return networkName;
	}

	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}

	public String getTurnTableName() {
		return turnTableName;
	}

	public void setTurnTableName(String turnTableName) {
		this.turnTableName = turnTableName;
	}

	public String getWeightName() {
		return weightName;
	}

	public void setWeightName(String weightName) {
		this.weightName = weightName;
	}

	public double getTolerance() {
		return tolerance;
	}

	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	public String getFromToEndIsProhibited() {
		return fromToEndIsProhibited;
	}

	public void setFromToEndIsProhibited(String fromToEndIsProhibited) {
		this.fromToEndIsProhibited = fromToEndIsProhibited;
	}

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	public int getCarLoad() {
		return carLoad;
	}

	public void setCarLoad(int carLoad) {
		this.carLoad = carLoad;
	}

	public double getLoadWeight() {
		return loadWeight;
	}

	public void setLoadWeight(double loadWeight) {
		this.loadWeight = loadWeight;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}


	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getApiDataSourceUrl() {
		return apiDataSourceUrl;
	}

	public void setApiDataSourceUrl(String apiDataSourceUrl) {
		this.apiDataSourceUrl = apiDataSourceUrl;
	}

	public String getApiDBUrl() {
		return apiDBUrl;
	}

	public void setApiDBUrl(String apiDBUrl) {
		this.apiDBUrl = apiDBUrl;
	}

	public String getApiDBDriver() {
		return apiDBDriver;
	}

	public void setApiDBDriver(String apiDBDriver) {
		this.apiDBDriver = apiDBDriver;
	}

	public String getApiUsername() {
		return apiUsername;
	}

	public void setApiUsername(String apiUsername) {
		this.apiUsername = apiUsername;
	}

	public String getApiPassword() {
		return apiPassword;
	}

	public void setApiPassword(String apiPassword) {
		this.apiPassword = apiPassword;
	}

	public String getApiNetWorkName() {
		return apiNetWorkName;
	}

	public void setApiNetWorkName(String apiNetWorkName) {
		this.apiNetWorkName = apiNetWorkName;
	}

	public String getApiTurnTableName() {
		return apiTurnTableName;
	}

	public void setApiTurnTableName(String apiTurnTableName) {
		this.apiTurnTableName = apiTurnTableName;
	}

	public int getSwap() {
		return swap;
	}

	public void setSwap(int swap) {
		this.swap = swap;
	}

	public int getLoadOrderNumber() {
		return loadOrderNumber;
	}

	public void setLoadOrderNumber(int loadOrderNumber) {
		this.loadOrderNumber = loadOrderNumber;
	}

	public double getDistenceMeterPerDegree() {
		return distenceMeterPerDegree;
	}

	public void setDistenceMeterPerDegree(double distenceMeterPerDegree) {
		this.distenceMeterPerDegree = distenceMeterPerDegree;
	}

	public int getDistanceIndex() {
		return distanceIndex;
	}

	public void setDistanceIndex(int distanceIndex) {
		this.distanceIndex = distanceIndex;
	}

	public int getTimeIndex() {
		return timeIndex;
	}

	public void setTimeIndex(int timeIndex) {
		this.timeIndex = timeIndex;
	}

	public int getOrderUnloadMiniutes() {
		return orderUnloadMiniutes;
	}

	public void setOrderUnloadMiniutes(int orderUnloadMiniutes) {
		this.orderUnloadMiniutes = orderUnloadMiniutes;
	}

	
}
