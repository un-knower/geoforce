package com.supermap.egispservice.base.pojo;

import java.io.Serializable;


public class BaseRoleListItem  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String status;
	private BasePrivilegeListItem[] privileges;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BasePrivilegeListItem[] getPrivileges() {
		return privileges;
	}
	public void setPrivileges(BasePrivilegeListItem[] privileges) {
		this.privileges = privileges;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
