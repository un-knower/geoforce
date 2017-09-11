package com.supermap.egispservice.base.pojo;

import java.io.Serializable;

public class BaseOrderItem  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String moduleId;
	private String pModuleId;
	private String moduleName;
	private int limit;
	private float consultPrice;
	private String startUseTime;
	private String deadline;
	private String id;
	private int type;
	
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public float getConsultPrice() {
		return consultPrice;
	}
	public void setConsultPrice(float consultPrice) {
		this.consultPrice = consultPrice;
	}
	public String getStartUseTime() {
		return startUseTime;
	}
	public void setStartUseTime(String startUseTime) {
		this.startUseTime = startUseTime;
	}
	public String getDeadline() {
		return deadline;
	}
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getpModuleId() {
		return pModuleId;
	}
	public void setpModuleId(String pModuleId) {
		this.pModuleId = pModuleId;
	}
	
	
}
