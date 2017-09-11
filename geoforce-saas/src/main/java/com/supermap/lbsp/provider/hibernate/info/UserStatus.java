package com.supermap.lbsp.provider.hibernate.info;



/**
 * EgisUserStatus entity. @author MyEclipse Persistence Tools
 */

public class UserStatus implements java.io.Serializable {

	// Fields

	private Short id;
	private String value;

	// Constructors

	/** default constructor */
	public UserStatus() {
	}

	/** minimal constructor */
	public UserStatus(Short id) {
		this.id = id;
	}

	/** full constructor */
	public UserStatus(Short id, String value) {
		this.id = id;
		this.value = value;
	}

	
	public Short getId() {
		return this.id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}