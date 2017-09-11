package com.supermap.egispservice.base.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "sys_default_city", catalog = "egisp_dev")
public class SysDefaultCityEntity extends IdEntity implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String userid;
	String eid;
	String deptid;
	String clevel;
	String admincode;
	String province;
	String city;
	String county;
	Date createTime;
	Date updateTime;
	String defaultname;
	
	
	public SysDefaultCityEntity() {
	}


	public SysDefaultCityEntity(String id,String userid, String eid, String deptid,
			String clevel, String admincode, String province, String city,
			String county, Date createTime, Date updateTime,String defaultname) {
		this.id=id;
		this.userid = userid;
		this.eid = eid;
		this.deptid = deptid;
		this.clevel = clevel;
		this.admincode = admincode;
		this.province = province;
		this.city = city;
		this.county = county;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.defaultname=defaultname;
	}


	@Column(name = "userid", length = 32)
	public String getUserid() {
		return userid;
	}


	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Column(name = "eid", length = 32)
	public String getEid() {
		return eid;
	}


	public void setEid(String eid) {
		this.eid = eid;
	}


	@Column(name = "deptid", length = 32)
	public String getDeptid() {
		return deptid;
	}


	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	@Column(name = "clevel", length = 2)
	public String getClevel() {
		return clevel;
	}


	public void setClevel(String clevel) {
		this.clevel = clevel;
	}

	@Column(name = "admincode", length = 16)
	public String getAdmincode() {
		return admincode;
	}


	public void setAdmincode(String admincode) {
		this.admincode = admincode;
	}

	@Column(name = "province", length = 8)
	public String getProvince() {
		return province;
	}


	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "city", length = 32)
	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "county", length = 32)
	public String getCounty() {
		return county;
	}


	public void setCounty(String county) {
		this.county = county;
	}

	@Column(name = "defaultname", length = 50)
	public String getDefaultname() {
		return defaultname;
	}


	public void setDefaultname(String defaultname) {
		this.defaultname = defaultname;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")
	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time")
	public Date getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
