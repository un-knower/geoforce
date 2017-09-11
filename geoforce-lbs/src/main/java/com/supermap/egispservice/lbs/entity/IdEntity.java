package com.supermap.egispservice.lbs.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/**
 * 统一定义id的entity基类. 
 * 
 */
@MappedSuperclass
public abstract class IdEntity {

	protected String id;

	@Id
	@GenericGenerator(name = "idGenerator", strategy = "uuid.hex")
    @GeneratedValue(generator = "idGenerator")
	@Column(name = "ID", unique = true, nullable = false, length = 35)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
