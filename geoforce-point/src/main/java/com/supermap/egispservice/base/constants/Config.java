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
	
	public Config() {
		super();
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

}
