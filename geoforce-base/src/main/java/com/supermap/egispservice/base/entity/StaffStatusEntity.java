package com.supermap.egispservice.base.entity;

// Generated 2014-9-4 11:41:47 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EgisRssStaffStatus generated by hbm2java
 */
@Entity
@Table(name = "EGIS_RSS_STAFF_STATUS", catalog = "egisp_dev")
public class StaffStatusEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private byte id;
	private String value;

	public StaffStatusEntity() {
	}

	public StaffStatusEntity(byte id) {
		this.id = id;
	}

	public StaffStatusEntity(byte id, String value) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StaffStatusEntity other = (StaffStatusEntity) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
}
