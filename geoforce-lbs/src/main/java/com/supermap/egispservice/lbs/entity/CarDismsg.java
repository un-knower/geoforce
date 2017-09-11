package com.supermap.egispservice.lbs.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


/**
 * RepCarDismsg entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "lbs_car_dismsg", catalog = "egisp_dev")
public class CarDismsg extends IdEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String deptId;
	private String title;
	private String content;
	private Date sendDate;
	private Short status;
	//private String carId;
	private String mobile;
	private Short type;
	private String license;
	private String userId;
	// Constructors

	/** default constructor */
	public CarDismsg() {
	}

	/** minimal constructor */
	public CarDismsg(String id) {
		this.id = id;
	}

	/** full constructor */
	public CarDismsg(String id,String deptId, String title, String content, Date sendDate,
			Short status, String carId, String mobile,Short type,String userId) {
		this.id = id;
		this.deptId = deptId;
		this.title = title;
		this.content = content;
		this.sendDate = sendDate;
		this.status = status;
		//this.carId = carId;
		this.mobile = mobile;
		this.type = type;
		this.userId = userId;
	}


	@Column(name = "DEPT_ID",nullable = true, length = 48)
	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	
	@Column(name = "TITLE", length = 48)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "CONTENT", length = 256)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SEND_DATE")
	public Date getSendDate() {
		return this.sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	@Column(name = "STATUS")
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}
	
	@Column(name = "TYPE")
	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	/*@Column(name = "CAR_ID", length = 32)
	public String getCarId() {
		return this.carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}*/

	@Column(name = "MOBILE", length = 16)
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@Column(name = "USER_ID",nullable = true, length = 48)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Transient
	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}
	
	
	private Car car;

	@ManyToOne(cascade=CascadeType.REFRESH,fetch=FetchType.LAZY,optional=false)
	@JoinColumn(name="CAR_ID")
	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}
	
	
}