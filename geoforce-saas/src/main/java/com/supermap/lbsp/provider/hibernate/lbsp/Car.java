package com.supermap.lbsp.provider.hibernate.lbsp;

import java.util.Date;


/**
 * InfoCar entity. @author MyEclipse Persistence Tools
 */

public class Car implements java.io.Serializable {

	// Fields

	private String id;
	private String depId;
	private String eid;
	private String license;
	private String color;
	private String brand;
	private Short status;
	private String type;
	private String petrol;
	private String others;
	private Date operDate;
	private String createUserid;
	private Date stopDate;

	// Constructors

	/** default constructor */
	public Car() {
	}

	/** minimal constructor */
	public Car(String id,String eid, String depId, String license, String type,
			Date operDate, String createUserid) {
		this.id = id;
		this.eid = eid;
		this.depId = depId;
		this.license = license;
		this.type = type;
		this.operDate = operDate;
		this.createUserid = createUserid;
	}

	/** full constructor */
	public Car(String id,String eid, String depId, String license, String color,
			String brand, Short status, String type, String petrol,
			String others, Date operDate, String createUserid, Date stopDate) {
		this.id = id;
		this.eid = eid;
		this.depId = depId;
		this.license = license;
		this.color = color;
		this.brand = brand;
		this.status = status;
		this.type = type;
		this.petrol = petrol;
		this.others = others;
		this.operDate = operDate;
		this.createUserid = createUserid;
		this.stopDate = stopDate;
	}

	// Property accessors
	
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	
	public String getDepId() {
		return this.depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}

	
	public String getLicense() {
		return this.license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	
	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	
	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	public String getPetrol() {
		return this.petrol;
	}

	public void setPetrol(String petrol) {
		this.petrol = petrol;
	}

	
	public String getOthers() {
		return this.others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	
	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

	
	public String getCreateUserid() {
		return this.createUserid;
	}

	public void setCreateUserid(String createUserid) {
		this.createUserid = createUserid;
	}

	
	public Date getStopDate() {
		return this.stopDate;
	}

	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}

}