package com.supermap.egispservice.base.pojo;

import java.io.Serializable;

public class DeptUserZtree implements Serializable{

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public boolean getIsParent() {
		return isParent;
	}
	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}
	public boolean getIsUser() {
		return isUser;
	}
	public void setIsUser(boolean isUser) {
		this.isUser = isUser;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
    private String pId;
    private String name;
    private String code;
    private boolean isParent;
    private boolean isUser;
    
    @Override
    public String toString() {
    	return this.getId()+","+this.getName()+","+this.getpId()+","+this.getIsUser()+","+this.getIsParent();
    }
	

}
