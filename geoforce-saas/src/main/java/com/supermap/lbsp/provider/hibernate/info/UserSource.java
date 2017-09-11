package com.supermap.lbsp.provider.hibernate.info;



/**
 * EgisRssUserSource entity. @author MyEclipse Persistence Tools
 */

public class UserSource implements java.io.Serializable {

	// Fields

	private Short id;
	private String value;

	// Constructors

	/** default constructor */
	public UserSource() {
	}

	/** minimal constructor */
	public UserSource(Short id) {
		this.id = id;
	}

	/** full constructor */
	public UserSource(Short id, String value) {
		this.id = id;
		this.value = value;
	}

	// Property accessors
	
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