package com.supermap.egispservice.base.pojo;

import java.io.Serializable;

public class LogisticsAPIResult implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String areaNumber;
	private String areaName;
	private double x;
	private double y;
	private String resultType;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAreaNumber() {
		return areaNumber;
	}
	public void setAreaNumber(String areaNumber) {
		this.areaNumber = areaNumber;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public String getResultType() {
		return resultType;
	}
	public void setResultType(String resultType) {
		this.resultType = resultType;
	}
	
	/**
	 * 区划状态
	 */
	private int status=0;//0 正常，1停用，2 超区
	
	
	public int getStatus() {
		return this.status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * 关联区划编码
	 */
	private String relationAreanum;
	
	/**
	 * 关联区划名称
	 */
	private String relationAreaname;
	
	public String getRelationAreanum() {
		return relationAreanum;
	}
	public void setRelationAreanum(String relationAreanum) {
		this.relationAreanum = relationAreanum;
	}
	public String getRelationAreaname() {
		return relationAreaname;
	}
	public void setRelationAreaname(String relationAreaname) {
		this.relationAreaname = relationAreaname;
	}
	
	
}
