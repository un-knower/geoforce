package com.supermap.egispservice.lbs.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 终端信息
 * @author Administrator
 *
 */
@Entity
@Table(name = "lbs_terminal", catalog = "egisp_dev")
public class Terminal  extends IdEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String eid;
	private String typeId;
	private String carId;
	private String name;
	private String code;
	private String mobile;
	private String deptId;
	private Date operDate;

	// Constructorso

	/** default constructor */
	public Terminal() {
	}

	/** full constructor */
	public Terminal(String id,String eid, String typeId, String carId, String name,
			String code, String mobile, String deptId, Date operDate) {
		this.id = id;
		this.eid = eid;
		this.typeId = typeId;
		this.carId = carId;
		this.name = name;
		this.code = code;
		this.mobile = mobile;
		this.deptId = deptId;
		this.operDate = operDate;
	}

	

	@Column(name = "EID", nullable = true, length = 48)
	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}
	
	@Column(name = "TYPE_ID", nullable = false, length = 48)
	public String getTypeId() {
		return this.typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	@Column(name = "CAR_ID", nullable = false, length = 48)
	public String getCarId() {
		return this.carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	@Column(name = "NAME", nullable = false, length = 24)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "CODE", nullable = false, length = 32)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "MOBILE", nullable = false, length = 16)
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "DEPT_ID", nullable = false, length = 32)
	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OPER_DATE", nullable = false, length = 10)
	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

	
	
	private Car car;

	@OneToOne(cascade=CascadeType.REFRESH,optional=false)
	@JoinColumn(name="CAR_ID",referencedColumnName="ID",insertable=false,updatable=false)
	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}
	
	
	private TerminalType type;

	@ManyToOne
	@JoinColumn(name="TYPE_ID",insertable=false,updatable=false)
	public TerminalType getType() {
		return type;
	}

	public void setType(TerminalType type) {
		this.type = type;
	}
	
	
	

}