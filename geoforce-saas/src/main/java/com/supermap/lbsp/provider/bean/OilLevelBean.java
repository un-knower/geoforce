package com.supermap.lbsp.provider.bean;

import java.util.List;




public class OilLevelBean {
	private String name;
	private String[] xData;//曲线图x轴数据 
	private Double[] yData;//曲线图y轴数据 
	private List<OilLevelInfoBean> list;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String[] getxData() {
		return xData;
	}
	public void setxData(String[] xData) {
		this.xData = xData;
	}
	public Double[] getyData() {
		return yData;
	}
	public void setyData(Double[] yData) {
		this.yData = yData;
	}
	public List<OilLevelInfoBean> getList() {
		return list;
	}
	public void setList(List<OilLevelInfoBean> list) {
		this.list = list;
	}
	
	

}
