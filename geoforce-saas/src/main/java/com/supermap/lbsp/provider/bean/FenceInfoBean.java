package com.supermap.lbsp.provider.bean;



import java.sql.Date;
import java.util.List;

import javax.xml.crypto.Data;

import com.supermap.lbsp.provider.hibernate.lbsp.AlarmType;

public class FenceInfoBean {
	private String id;
	private String name;
	private String alarmType;
	private Short status;
	private Date setdate;
	private String userId;
	private String region;
	private String remark;
	private String deptId;
	/**区域类型 1围栏2线3点 default= 1*/
	private Short regionType;
	private String typeName;
	private String statusName;
	private String setDateStr;//操作时间str
	private String userName;//处理人名称
	private String time;//有效时间
	private String openData;
	
	private String regionId;
	private String regionSetId;
	
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getRegionSetId() {
		return regionSetId;
	}
	public void setRegionSetId(String regionSetId) {
		this.regionSetId = regionSetId;
	}
	public String getOpenData() {
		return openData;
	}
	public void setOpenData(String openData) {
		this.openData = openData;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(String alarmType) {
		this.alarmType = alarmType;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public Date getSetdate() {
		return setdate;
	}
	public void setSetdate(Date setdate) {
		this.setdate = setdate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public Short getRegionType() {
		return regionType;
	}
	public void setRegionType(Short regionType) {
		this.regionType = regionType;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getSetDateStr() {
		return setDateStr;
	}
	public void setSetDateStr(String setDateStr) {
		this.setDateStr = setDateStr;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
