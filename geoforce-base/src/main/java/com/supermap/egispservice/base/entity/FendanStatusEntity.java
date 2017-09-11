package com.supermap.egispservice.base.entity;

// Generated 2014-9-4 11:41:47 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DimFendanStatus generated by hbm2java
 */
@Entity
@Table(name = "DIM_FENDAN_STATUS", catalog = "egisp_dev")
public class FendanStatusEntity implements java.io.Serializable {

	private byte id;
	private String value;

	public FendanStatusEntity() {
	}

	public FendanStatusEntity(byte id) {
		this.id = id;
	}

	public FendanStatusEntity(byte id, String value) {
		this.id = id;
		this.value = value;
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	public byte getId() {
		return this.id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	@Column(name = "value", length = 8)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
