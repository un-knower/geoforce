package com.supermap.egispservice.base.pojo;

import java.io.Serializable;

public class DistributeAddress implements Serializable{

	
	private String address;
	private String id;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
