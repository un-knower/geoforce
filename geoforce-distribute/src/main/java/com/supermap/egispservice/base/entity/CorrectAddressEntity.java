package com.supermap.egispservice.base.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "CORRECT_ADDRESS")
public class CorrectAddressEntity extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String address;
	private String batch;
	private String areaNum;
	private String areaName;
	private String desc;
	private Date addTime;
	private Date correctTime;
	private int status = 0;
	private double x;
	private double y;
	private String userId;
	private String eid;
	private String dcode;
	
	@Column(name="address",length=100)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Column(name="batch",length=32)
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	@Column(name="area_num",length=32)
	public String getAreaNum() {
		return areaNum;
	}
	public void setAreaNum(String areaNum) {
		this.areaNum = areaNum;
	}
	@Column(name="area_name",length=32)
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	@Column(name="desc_info",length=512)
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "add_time")
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="correct_time")
	public Date getCorrectTime() {
		return correctTime;
	}
	public void setCorrectTime(Date correctTime) {
		this.correctTime = correctTime;
	}
	@Column(name="status",nullable=false)
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Column(name="x")
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	@Column(name="y")
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	@Column(name="user_id",length=32)
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Column(name="eid",length=32)
	public String getEid() {
		return eid;
	}
	public void setEid(String eid) {
		this.eid = eid;
	}
	@Column(name="dcode",length=32)
	public String getDcode() {
		return dcode;
	}
	public void setDcode(String dcode) {
		this.dcode = dcode;
	}
	
	
	
	
	

}
