package com.supermap.egispservice.pathanalysis.constant;

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

	public Config() {
		super();
	}

	public String getWeightName() {
		return weightName;
	}

	public void setWeightName(String weightName) {
		this.weightName = weightName;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public double getTolerance() {
		return tolerance;
	}

	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
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

	public double getDistenceMeterPerDegree() {
		return distenceMeterPerDegree;
	}

	public void setDistenceMeterPerDegree(double distenceMeterPerDegree) {
		this.distenceMeterPerDegree = distenceMeterPerDegree;
	}
	
	
	/**
	 * 连接方式 .1：udb  2:oracle
	 */
	@Value("#{configParam['swap']}")
	private int swap;

	public int getSwap() {
		return swap;
	}

	public void setSwap(int swap) {
		this.swap = swap;
	}
	
	
	@Value("#{configParam['apiworkspacePath']}")
	private String apiworkspacePath;

	public String getApiworkspacePath() {
		return apiworkspacePath;
	}

	public void setApiworkspacePath(String roadworkspacePath) {
		this.apiworkspacePath = roadworkspacePath;
	}
	
}
