package com.supermap.egispservice.reverse.utils;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.supermap.utils.AppPropertiesUtil;

@Component
public class Config {

	/**
	 * 百度反向地址解析接口地址
	 */
	@Value("#{configParam['baidu.geocoder.url']}")
	private String baiduGeocoderUrl;

	/**
	 * 百度ak
	 */
	@Value("#{configParam['baidu.ak']}")
	private String baiduAK;
	
	
	private static Properties prop = AppPropertiesUtil.readPropertiesFile("config.properties", Config.class);
	
	public static final int QUERY_RANGE = Integer.parseInt(prop.getProperty("query.range", "200"));
	
	public static int QUERY_RANGE_MAX = Integer.parseInt(prop.getProperty("query.range.max", "500"));
	
	public static final String INDEX_DIR = prop.getProperty("lucene.index.dir");
	
	public static final String INDEX_NAME = prop.getProperty("lucene.index.name");
	
	// 公里
	public static final int RANGE_BUFF_TH = 6;
	// 几百米
	public static final int RANGE_BUFF_HU = 7;
	// 几十米
	public static final int RANGE_BUFF_TE = 8;
	// 几米
	public static final int RANGE_BUFF_MI = 9;
	// 公里级别误差，五百米阀值
	public static final int RANGE_BUFF_TH_THRESHOD = 500;
	// 百米级别误差，一百米阀值
	public static final int RANGE_BUFF_HU_THRESHOD = 100;
	// 十米级别误差，三十米阀值
	public static final int RANGE_BUFF_TE_THRESHOD = 30;
	
	// 摩卡托
	public static final String TYPE_SMC = "SMC";
	// 经纬度
	public static final String TYPE_SMLL = "SMLL";
	
	
	

	public String getBaiduGeocoderUrl() {
		return baiduGeocoderUrl;
	}

	public void setBaiduGeocoderUrl(String baiduGeocoderUrl) {
		this.baiduGeocoderUrl = baiduGeocoderUrl;
	}

	public String getBaiduAK() {
		return baiduAK;
	}

	public void setBaiduAK(String baiduAK) {
		this.baiduAK = baiduAK;
	}
}
