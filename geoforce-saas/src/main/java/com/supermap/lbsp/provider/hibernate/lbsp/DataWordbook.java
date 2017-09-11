package com.supermap.lbsp.provider.hibernate.lbsp;



/**
 * DataWordbook entity. @author MyEclipse Persistence Tools
 */

public class DataWordbook implements java.io.Serializable {

	// Fields

	private String id;
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

	// Property accessors
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public Short getStarts() {
		return this.starts;
	}

	public void setStarts(Short starts) {
		this.starts = starts;
	}

}