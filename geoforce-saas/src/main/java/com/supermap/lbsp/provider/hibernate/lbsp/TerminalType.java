package com.supermap.lbsp.provider.hibernate.lbsp;


/**
 * InfoTerminalType entity. @author MyEclipse Persistence Tools
 */

public class TerminalType implements java.io.Serializable {

	// Fields

	private String id;
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

	
	public String getFactory() {
		return this.factory;
	}

	public void setFactory(String factory) {
		this.factory = factory;
	}

	
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	public Short getType() {
		return this.type;
	}

	public void setType(Short type) {
		this.type = type;
	}

}