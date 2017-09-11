package com.supermap.egispservice.base.pojo;

import java.io.Serializable;

public class BaseStaffDetails extends BaseStaffListItem  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mobilePhone;
	private String phone;
	private String[] roleNames;
	private String[] roleIds;
	private String[] privilegeIds;
	private String[] privilegeCodes;
	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setRoleNames(String[] roleNames) {
		this.roleNames = roleNames;
	}
	public String[] getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}
	public String[] getPrivilegeCodes() {
		return privilegeCodes;
	}
	public void setPrivilegeCodes(String[] privilegeCodes) {
		this.privilegeCodes = privilegeCodes;
	}
	public String[] getRoleNames() {
		return roleNames;
	}
	public String[] getPrivilegeIds() {
		return privilegeIds;
	}
	public void setPrivilegeIds(String[] privilegeIds) {
		this.privilegeIds = privilegeIds;
	}
	
	
	
	
}
