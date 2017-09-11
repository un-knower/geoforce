package com.supermap.egispservice.lbs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * InfoTerminalType entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "lbs_terminal_type", catalog = "egisp_dev")
public class TerminalType extends IdEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String factory;
	private String code;
	private Short type;

	// Constructors

	/** default constructor */
	public TerminalType() {
	}

	/** minimal constructor */
	public TerminalType(String id, String name, String code, Short type) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.type = type;
	}

	/** full constructor */
	public TerminalType(String id, String name, String factory,
			String code, Short type) {
		this.id = id;
		this.name = name;
		this.factory = factory;
		this.code = code;
		this.type = type;
	}


	@Column(name = "NAME", nullable = false, length = 24)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "FACTORY", length = 128)
	public String getFactory() {
		return this.factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	@Column(name = "CODE", nullable = false, length = 48)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "TYPE", nullable = false)
	public Short getType() {
		return this.type;
	}

	public void setType(Short type) {
		this.type = type;
	}

}