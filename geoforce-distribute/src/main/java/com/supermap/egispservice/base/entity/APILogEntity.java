package com.supermap.egispservice.base.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
@Entity
@Table(name = "sys_api_log")
public class APILogEntity extends IdEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String uri;
	private String userid;
	private String param;
	private String result;
	private Date completeTime;
	private Date requestTime;
	private String consumeTime;
	private String deptid;
	private String eid;
	private String dcode;
	private int successcount;
	private int failcount;
	private int sumcount;
	
	
	public APILogEntity() {
	}

	public APILogEntity(String uri, String userid, String param, String result,
			Date completeTime, Date requestTime, String consumeTime,
			String deptid, String eid, String dcode, int successcount,
			int failcount, int sumcount) {
		this.uri = uri;
		this.userid = userid;
		this.param = param;
		this.result = result;
		this.completeTime = completeTime;
		this.requestTime = requestTime;
		this.consumeTime = consumeTime;
		this.deptid = deptid;
		this.eid = eid;
		this.dcode = dcode;
		this.successcount = successcount;
		this.failcount = failcount;
		this.sumcount = sumcount;
	}
	
	
	@Column(name="uri",length=100)
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Column(name="userid",length=50)
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	@Column(name="param",length=1000)
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	@Column(name="result",length=1000)
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="completetime")
	public Date getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(Date completeTime) {
		this.completeTime = completeTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="requesttime")
	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	@Column(name="consumetime",length=32)
	public String getConsumeTime() {
		return consumeTime;
	}

	public void setConsumeTime(String consumeTime) {
		this.consumeTime = consumeTime;
	}

	@Column(name="deptid",length=32)
	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	@Column(name="eid",length=32)
	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	@Column(name="dcode",length=50)
	public String getDcode() {
		return dcode;
	}

	public void setDcode(String dcode) {
		this.dcode = dcode;
	}

	@Column(name="successcount",length=10)
	public int getSuccesscount() {
		return successcount;
	}

	public void setSuccesscount(int successcount) {
		this.successcount = successcount;
	}

	@Column(name="failcount",length=10)
	public int getFailcount() {
		return failcount;
	}

	public void setFailcount(int failcount) {
		this.failcount = failcount;
	}

	@Column(name="sumcount",length=10)
	public int getSumcount() {
		return sumcount;
	}

	public void setSumcount(int sumcount) {
		this.sumcount = sumcount;
	}
	
}
