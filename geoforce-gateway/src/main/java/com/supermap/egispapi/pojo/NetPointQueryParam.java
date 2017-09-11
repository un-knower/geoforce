package com.supermap.egispapi.pojo;

import java.io.Serializable;

public class NetPointQueryParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String name;
	private String id;
	private String userId;
	private int pageNo = 1;
	private int pageSize = 10;
	@Override
	public String toString() {
		return "name=" + name + ", id=" + id + ", userId="
				+ userId + ", pageNo=" + pageNo + ", pageSize=" + pageSize
				+ ", areaId=" + areaId + "";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	private String areaId;//区划id
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	

}
