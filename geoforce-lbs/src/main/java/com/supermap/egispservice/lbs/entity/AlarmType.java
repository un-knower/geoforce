package com.supermap.egispservice.lbs.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * CfgAlermType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "lbs_alarm_type",catalog = "egisp_dev")
public class AlarmType  extends IdEntity  implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields
	private String name;
	private Short type;
	private String code;

	// Constructors

	/** default constructor */
	public AlarmType() {
	}

	/** minimal constructor */
	public AlarmType(String id) {
		this.id = id;
	}

	/** full constructor */
	public AlarmType(String id, String name, Short type, String code) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.code = code;
	}


	@Column(name = "NAME", length = 48)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "TYPE")
	public Short getType() {
		return this.type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	@Column(name = "CODE", length = 8)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}