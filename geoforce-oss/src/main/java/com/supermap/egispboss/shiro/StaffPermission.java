package com.supermap.egispboss.shiro;

import org.apache.shiro.authz.Permission;

public class StaffPermission implements Permission{

	private String code;
	private String url;
	
	public StaffPermission() {
	}
	
	public StaffPermission(String code,String url){
		this.code = code;
		this.url = url;
	}
	
	@Override
	public boolean implies(Permission permission) {
		if(!(permission instanceof StaffPermission)){
			return false;
		}
		StaffPermission sp = (StaffPermission)permission;
		String submitUrl = sp.getUrl();
		if(this.url.startsWith(submitUrl)){
			return true;
		}
	
		return false;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	

}
