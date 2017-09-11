package com.supermap.egispservice.base.entity;

// Generated 2014-9-4 11:41:47 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EgisRssOrderStatus generated by hbm2java
 */
@Entity
@Table(name = "EGIS_RSS_ORDER_STATUS", catalog = "egisp_dev")
public class RssOrderStatusEntity implements java.io.Serializable {

	private byte id;
	private String value;

	public RssOrderStatusEntity() {
	}

	public RssOrderStatusEntity(byte id) {
		this.id = id;
	}

	public RssOrderStatusEntity(byte id, String value) {
		this.id = id;
		this.value = value;
	}

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	public byte getId() {
		return this.id;
	}

	public void setId(byte id) {
		this.id = id;
	}

	@Column(name = "value", length = 32)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
