package com.supermap.egispportal.access;

public class AccessResourceInfo {

	private String uri;
	private boolean needLogin = true;
	
	public AccessResourceInfo() {
		// TODO Auto-generated constructor stub
	}
	public AccessResourceInfo(String uri,boolean needLogin) {
		this.uri = uri;
		this.needLogin = needLogin;
	}
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public boolean isNeedLogin() {
		return needLogin;
	}
	public void setNeedLogin(boolean needLogin) {
		this.needLogin = needLogin;
	}
	
	
	
}
