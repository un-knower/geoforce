package com.supermap.egispservice.base.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
@Table(name = "sys_api_fendan")
public class APIFendanEntity extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String address;
	private String userid;
	private String eid;
	private String deptid;
	private String dcode;
	private String admincode;
	private String province;
	private String city;
	private String county;
	private String areaid;
	private String resulttype;
	private Date fendanTime;
	private BigDecimal smx;
	private BigDecimal smy;
	
	private String orderNum;//订单编号
	
	
	public APIFendanEntity() {
	}

	public APIFendanEntity(String address, String userid, String eid,
			String deptid, String dcode, String admincode, String province,
			String city, String county, String areaid,String resulttype,Date fendanTime,BigDecimal smx
			,BigDecimal smy) {
		this.address = address;
		this.userid = userid;
		this.eid = eid;
		this.deptid = deptid;
		this.dcode = dcode;
		this.admincode = admincode;
		this.province = province;
		this.city = city;
		this.county = county;
		this.areaid = areaid;
		this.resulttype=resulttype;
		this.fendanTime=fendanTime;
		this.smx=smx;
		this.smy=smy;
	}

	
	@Column(name = "smx", precision = 38, scale = 16)
	public BigDecimal getSmx() {
		return smx;
	}

	public void setSmx(BigDecimal smx) {
		this.smx = smx;
	}

	@Column(name = "smy", precision = 38, scale = 16)
	public BigDecimal getSmy() {
		return smy;
	}

	public void setSmy(BigDecimal smy) {
		this.smy = smy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="fendan_time")
	public Date getFendanTime() {
		return fendanTime;
	}

	public void setFendanTime(Date fendanTime) {
		this.fendanTime = fendanTime;
	}

	@Column(name="resulttype",length=2)
	public String getResulttype() {
		return resulttype;
	}

	public void setResulttype(String resulttype) {
		this.resulttype = resulttype;
	}

	@Column(name="address",length=128)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name="userid",length=32)
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Column(name="eid",length=32)
	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	@Column(name="deptid",length=32)
	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	@Column(name="dcode",length=32)
	public String getDcode() {
		return dcode;
	}

	public void setDcode(String dcode) {
		this.dcode = dcode;
	}

	@Column(name="admincode",length=16)
	public String getAdmincode() {
		return admincode;
	}

	public void setAdmincode(String admincode) {
		this.admincode = admincode;
	}

	@Column(name="province",length=8)
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

	@Column(name="areaid",length=32)
	public String getAreaid() {
		return areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

	@Column(name="order_num",length=50)
	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	
}
