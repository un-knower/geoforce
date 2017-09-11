package com.supermap.egispservice.lbs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * DataWordbook entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "lbs_data_wordbook", catalog = "egisp_dev")
public class DataWordbook extends IdEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String type;
	private String code;
	private String name;
	private Short starts;

	// Constructors

	/** default constructor */
	public DataWordbook() {
	}

	/** minimal constructor */
	public DataWordbook(String id) {
		this.id = id;
	}

	/** full constructor */
	public DataWordbook(String id, String type, String code, String name,
			Short starts) {
		this.id = id;
		this.type = type;
		this.code = code;
		this.name = name;
		this.starts = starts;
	}


	@Column(name = "TYPE", length = 20)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "CODE", length = 6)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME", length = 48)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "STARTS")
	public Short getStarts() {
		return this.starts;
	}

	public void setStarts(Short starts) {
		this.starts = starts;
	}

}