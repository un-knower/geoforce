package com.supermap.egispservice.base.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "INFO_ADDRESS", catalog = "egisp_dev")
public class AddressEntity extends IdEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String address;
	private String userId;
	private String eid;
	private String dcode;
	private String areaId;
	private String areaName;
	private String areaNum;
	private String province;
	private String city;
	private String county;
	private int status = 2;
	private Date addTime;
	private double smx;
	private double smy;
	
	
	@Column(name="address",length=128)
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	@Column(name="province",length=32)
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	@Column(name="city",length=32)
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@Column(name="county",length=32)
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	@Column(name="status")
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "add_time")
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	@Column(name="smx")
	public double getSmx() {
		return smx;
	}
	public void setSmx(double smx) {
		this.smx = smx;
	}
	@Column(name="smy")
	public double getSmy() {
		return smy;
	}
	public void setSmy(double smy) {
		this.smy = smy;
	}
	@Column(name="area_id")
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	@Column(name="area_name")
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	@Column(name="area_num")
	public String getAreaNum() {
		return areaNum;
	}
	public void setAreaNum(String areaNum) {
		this.areaNum = areaNum;
	}
	
	
	
	
}
