package com.supermap.egispapi.pojo;

import java.io.Serializable;
import java.util.List;

public class DistributeXYParam  implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 待解析的地址集合
	 */
	private List<DistributePoint> points;
	
	/**
	 * 坐标类型
	 */
	private String type; 
	
	
	/**
	 * 是否需要返回省市区
	 */
	private boolean needProv=false;

	/**
	 * 是否需要返回区划状态
	 */
//	private boolean needAreaStatus=false;
//	
//
//	public boolean isNeedAreaStatus() {
//		return needAreaStatus;
//	}
//
//	public void setNeedAreaStatus(boolean needAreaStatus) {
//		this.needAreaStatus = needAreaStatus;
//	}
	

	public boolean isNeedProv() {
		return needProv;
	}

	public void setNeedProv(boolean needProv) {
		this.needProv = needProv;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public List<DistributePoint> getPoints() {
		return points;
	}

	public void setPoints(List<DistributePoint> points) {
		this.points = points;
	}

	
	
}
