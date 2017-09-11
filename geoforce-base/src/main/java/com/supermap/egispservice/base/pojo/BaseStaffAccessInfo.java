package com.supermap.egispservice.base.pojo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


public class BaseStaffAccessInfo  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String username;
	private List<String> roleNames;
	private Map<String,String> permissions;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<String> getRoleNames() {
		return roleNames;
	}
	public void setRoleNames(List<String> roleNames) {
		this.roleNames = roleNames;
	}
	public Map<String, String> getPermissions() {
		return permissions;
	}
	public void setPermissions(Map<String, String> permissions) {
		this.permissions = permissions;
	}
	
	
	
	

}
