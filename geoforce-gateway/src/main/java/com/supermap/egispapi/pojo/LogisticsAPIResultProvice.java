package com.supermap.egispapi.pojo;

import java.io.Serializable;

import com.supermap.egispservice.base.pojo.LogisticsAPIResult;

/**
 * 需要返回省市区的分单结果
 */
public class LogisticsAPIResultProvice extends  LogisticsAPIResult implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	/**
	 * 省
	 */
	private String provice;
	
	/**
	 * 市
	 */
	private String city;
	
	/**
	 * 区
	 */
	private String county;

	public String getProvice() {
		return provice;
	}

	public void setProvice(String provice) {
		this.provice = provice;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}
	
	
	
}
