package com.supermap.egispapi.pojo;

import java.io.Serializable;

public class AdmincodeQueryParam implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String admincode;//行政区代码
	private String levelstr;//行政区级别
	public String getAdmincode() {
		return admincode;
	}
	public void setAdmincode(String admincode) {
		this.admincode = admincode;
	}
	public String getLevelstr() {
		return levelstr;
	}
	public void setLevelstr(String levelstr) {
		this.levelstr = levelstr;
	}
	
	

}
