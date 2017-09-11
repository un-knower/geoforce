package com.supermap.lbsp.provider.bean;

import java.util.Date;

public class PersonAlarm {
	// Fields

	private String id;
	private String personId;
	private Date alarmDate;
	private String personName;
	private String deptId;
	private String deptCode;
	private Integer type;//报警类型（对应报警类型表code）
	private String fenceId;//围栏id
	private Short status;//状态（1已处理0未处理）
	private Double lng;
	private Double lat;
	private Double orgLng;
	private Double orgLat;
	private String address;
	private String userId;
	private String opinion;
	private Date dealDate;
	private Date lastDate;// 每次报警最后时间
	private Long diffTime;// 报警持续时间 单位分钟  lastDate-alarmDate lastTimeStr以后不用
	private String others;//预留字段 其它内容
	private String picPath;
	private Short gpsType;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public Date getAlarmDate() {
		return alarmDate;
	}
	public void setAlarmDate(Date alarmDate) {
		this.alarmDate = alarmDate;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getFenceId() {
		return fenceId;
	}
	public void setFenceId(String fenceId) {
		this.fenceId = fenceId;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public Double getLng() {
		return lng;
	}
	public void setLng(Double lng) {
		this.lng = lng;
	}
	public Double getLat() {
		return lat;
	}
	public void setLat(Double lat) {
		this.lat = lat;
	}
	public Double getOrgLng() {
		return orgLng;
	}
	public void setOrgLng(Double orgLng) {
		this.orgLng = orgLng;
	}
	public Double getOrgLat() {
		return orgLat;
	}
	public void setOrgLat(Double orgLat) {
		this.orgLat = orgLat;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	public Date getDealDate() {
		return dealDate;
	}
	public void setDealDate(Date dealDate) {
		this.dealDate = dealDate;
	}
	public Date getLastDate() {
		return lastDate;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	public Long getDiffTime() {
		return diffTime;
	}
	public void setDiffTime(Long diffTime) {
		this.diffTime = diffTime;
	}
	public String getOthers() {
		return others;
	}
	public void setOthers(String others) {
		this.others = others;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public Short getGpsType() {
		return gpsType;
	}
	public void setGpsType(Short gpsType) {
		this.gpsType = gpsType;
	}
	
}
