package com.supermap.egispservice.base.pojo;

public class ServiceModuleByType {

	private String type;
	private String queryType;
	private BaseServiceModuleListItem[] modules;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	public BaseServiceModuleListItem[] getModules() {
		return modules;
	}
	public void setModules(BaseServiceModuleListItem[] modules) {
		this.modules = modules;
	}
	
}
