package com.supermap.egispservice.base.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 
 * @description 数据属性常量
 * @author CaoBin mailto:caobin@supermap.com
 * @company SuperMap Software Co., Ltd.
 * @createDate 2014-10-10
 * @version 1.0
 */
@Component
public class Config {
	/**
	 * SSO注册接口URL
	 */
	@Value("#{configParam['regist_url']}")
	private String registUrl;



	@Value("#{configParam['deploy.offline']}")
	private boolean deployOffline;
	
	
	
	
	public boolean isDeployOffline() {
		return deployOffline;
	}

	public void setDeployOffline(boolean deployOffline) {
		this.deployOffline = deployOffline;
	}

	public Config() {
		super();
	}

	public String getRegistUrl() {
		return registUrl;
	}

	public void setRegistUrl(String registUrl) {
		this.registUrl = registUrl;
	}
	
	/**
	 * web端网点聚集 数据界值
	 */
	@Value("#{configParam['point.WebconvergeNumber']}")
	private long webconvergeNumber;
	
	/**
	 * 移动端网点聚集 数据界值
	 */
	@Value("#{configParam['point.AppconvergeNumber']}")
	private long appconvergeNumber;




	public long getWebconvergeNumber() {
		return webconvergeNumber;
	}

	public void setWebconvergeNumber(long webconvergeNumber) {
		this.webconvergeNumber = webconvergeNumber;
	}

	public long getAppconvergeNumber() {
		return appconvergeNumber;
	}

	public void setAppconvergeNumber(long appconvergeNumber) {
		this.appconvergeNumber = appconvergeNumber;
	}
	
	@Value("#{configParam['IS_DEBUG']}")
	private boolean isDebug;
	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}
	
}
