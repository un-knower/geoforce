package com.supermap.egispweb.util;

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
	@Value("#{configParam['portal_url']}")
	private String portaltUrl;
	
	@Value("#{configParam['order.template.path']}")
	private String orderTemplatePath;
	
	@Value("#{configParam['order.time.range']}")
	private int orderTimeRange = 7;

	@Value("#{configParam['deploy.offline']}")
	private boolean deployOffline;
	
	@Value("#{configParam['point.header.base']}")
	private String headerBase;
	
	@Value("#{configParam['point.header.baseEn']}")
	private String headerBaseEn;
	
	@Value("#{configParam['point.extendcol.max']}")
	private int maxExtcol;
	
	  public static final int LOGIN_SUCCESS = 1;
	  public static final int LOGIN_USERINFO_ERROR = 2;
	  public static final int LOGIN_LOGINED = 3;
	  public static final int LOGIN_RESET_PASS = 4;
	  public static final int LOGIN_NEED_LIC = 5;
	
	
	public Config() {
		super();
	}

	public String getPortaltUrl() {
		return portaltUrl;
	}

	public void setPortaltUrl(String portaltUrl) {
		this.portaltUrl = portaltUrl;
	}

	public String getOrderTemplatePath() {
		return orderTemplatePath;
	}

	public void setOrderTemplatePath(String orderTemplatePath) {
		this.orderTemplatePath = orderTemplatePath;
	}

	public int getOrderTimeRange() {
		return orderTimeRange;
	}

	public void setOrderTimeRange(int orderTimeRange) {
		this.orderTimeRange = orderTimeRange;
	}

	public boolean isDeployOffline() {
		return deployOffline;
	}

	public void setDeployOffline(boolean deployOffline) {
		this.deployOffline = deployOffline;
	}

	public String getHeaderBase() {
		return headerBase;
	}

	public void setHeaderBase(String headerBase) {
		this.headerBase = headerBase;
	}

	public int getMaxExtcol() {
		return maxExtcol;
	}

	public void setMaxExtcol(int maxExtcol) {
		this.maxExtcol = maxExtcol;
	}

	public String getHeaderBaseEn() {
		return headerBaseEn;
	}

	public void setHeaderBaseEn(String headerBaseEn) {
		this.headerBaseEn = headerBaseEn;
	}

	
	@Value("#{configParam['c.dituhui.com.pointmapURL']}")
	private String cdituhuiMapURL;


	public String getCdituhuiMapURL() {
		return cdituhuiMapURL;
	}

	public void setCdituhuiMapURL(String cdituhuiMapURL) {
		this.cdituhuiMapURL = cdituhuiMapURL;
	}
	
	
}
