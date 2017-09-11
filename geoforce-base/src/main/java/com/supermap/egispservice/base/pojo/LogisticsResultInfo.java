package com.supermap.egispservice.base.pojo;

import java.io.Serializable;

/**
 * 
 * <p>Title: LogisticsResultInfo</p>
 * Description: 分单结果信息
 *
 * @author Huasong Huang
 * CreateTime: 2014-9-23 下午03:08:36
 */
public class LogisticsResultInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	private String orderNum;
	private String admincode;
	private String province;
	private String city;
	private String county;
	private String address;
	private String areaNum;
	private String areaId;
	private String areaName;
	private String netId;
	private String netName;
	private String dutyName;
	private String dutyPhone;
	private double smx;
	private double smy;
	private String batch;
	private String fendanStatus;
	private String orderStatus;

	
	public double getSmx() {
		return smx;
	}
	public void setSmx(double smx) {
		this.smx = smx;
	}
	public double getSmy() {
		return smy;
	}
	public void setSmy(double smy) {
		this.smy = smy;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getAdmincode() {
		return admincode;
	}
	public void setAdmincode(String admincode) {
		this.admincode = admincode;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAreaNum() {
		return areaNum;
	}
	public void setAreaNum(String areaNum) {
		this.areaNum = areaNum;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public String getDutyName() {
		return dutyName;
	}
	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}
	public String getDutyPhone() {
		return dutyPhone;
	}
	public void setDutyPhone(String dutyPhone) {
		this.dutyPhone = dutyPhone;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getFendanStatus() {
		return fendanStatus;
	}
	public void setFendanStatus(String fendanStatus) {
		this.fendanStatus = fendanStatus;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	
	
	
	
	

}
