package com.supermap.egispservice.base.pojo;

import java.io.Serializable;

public class BaseUserListInfo  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String username;
	private String realName;
	private String status;
	private String email;
	private String mobilephone;
	private String telephone;
	private String qq;
	private String firstLogin;
	
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
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getFirstLogin() {
		return firstLogin;
	}
	public void setFirstLogin(String firstLogin) {
		this.firstLogin = firstLogin;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	private String companyName;//公司名称
	private String admincode;//公司所属省份
	private String adminname;//省份名称
	private String combusiness;//所属行业
	private String businessremark;//行业备注

	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getAdmincode() {
		return admincode;
	}
	public void setAdmincode(String admincode) {
		this.admincode = admincode;
	}
	public String getAdminname() {
		return adminname;
	}
	public void setAdminname(String adminname) {
		this.adminname = adminname;
	}
	public String getCombusiness() {
		return combusiness;
	}
	public void setCombusiness(String combusiness) {
		this.combusiness = combusiness;
	}
	public String getBusinessremark() {
		return businessremark;
	}
	public void setBusinessremark(String businessremark) {
		this.businessremark = businessremark;
	}

	private String companyid;//企业id

	public String getCompanyid() {
		return companyid;
	}
	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}
	
	
}
