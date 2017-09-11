package com.supermap.lbsp.provider.hibernate.lbsp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


public class PersonStoreType implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	
	private String id;
	private String name;
	private Short type;

	// Constructors

	/** default constructor */
	public PersonStoreType() {
	}

	/** minimal constructor */
	public PersonStoreType(String id, String name) {
		this.id = id;
		this.name = name;
	}

	/** full constructor */
	public PersonStoreType(String id, String name, Short type) {
		this.id = id;
		this.name = name;
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

	
	public Short getType() {
		return this.type;
	}

	public void setType(Short type) {
		this.type = type;
	}

}