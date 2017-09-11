package com.supermap.egispservice.lbs.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * InfoCar entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "lbs_car", catalog = "egisp_dev")
public class Car extends IdEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String eid;
	private String depId;
	private String license;
	private String color;
	private String brand;
	private Short status;//1在线 2离线 3未上线（无记录）
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

	
	@Column(name = "EID", nullable = true, length = 48)
	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}
	
	@Column(name = "DEP_ID", nullable = false, length = 32)
	public String getDepId() {
		return this.depId;
	}

	public void setDepId(String depId) {
		this.depId = depId;
	}

	@Column(name = "LICENSE", nullable = false, length = 16)
	public String getLicense() {
		return this.license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	@Column(name = "COLOR", length = 8)
	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Column(name = "BRAND", length = 20)
	public String getBrand() {
		return this.brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@Column(name = "STATUS")
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	@Column(name = "TYPE", nullable = false, length = 48)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "PETROL", length = 16)
	public String getPetrol() {
		return this.petrol;
	}

	public void setPetrol(String petrol) {
		this.petrol = petrol;
	}

	@Column(name = "OTHERS", length = 256)
	public String getOthers() {
		return this.others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OPER_DATE", nullable = false)
	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

	@Column(name = "CREATE_USERID", nullable = false, length = 32)
	public String getCreateUserid() {
		return this.createUserid;
	}

	public void setCreateUserid(String createUserid) {
		this.createUserid = createUserid;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STOP_DATE")
	public Date getStopDate() {
		return this.stopDate;
	}

	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}
	
	
	
	private Terminal terminal;//终端

	@OneToOne(cascade={CascadeType.REMOVE},fetch=FetchType.LAZY,optional = true,mappedBy = "car")
	public Terminal getTerminal() {
		return terminal;
	}

	public void setTerminal(Terminal terminal) {
		this.terminal = terminal;
	}
	

}