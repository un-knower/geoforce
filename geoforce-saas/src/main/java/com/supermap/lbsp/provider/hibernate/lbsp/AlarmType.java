package com.supermap.lbsp.provider.hibernate.lbsp;


/**
 * CfgAlermType entity. @author MyEclipse Persistence Tools
 */

public class AlarmType implements java.io.Serializable {

	// Fields

	private String id;
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

	// Property accessors
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public Short getType() {
		return this.type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}