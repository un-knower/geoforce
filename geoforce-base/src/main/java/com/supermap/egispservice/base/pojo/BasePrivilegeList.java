package com.supermap.egispservice.base.pojo;

import java.io.Serializable;

public class BasePrivilegeList  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int total = 0;
	private int currentCount = 0;
	private BasePrivilegeListItem[] items;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getCurrentCount() {
		return currentCount;
	}
	public void setCurrentCount(int currentCount) {
		this.currentCount = currentCount;
	}
	public BasePrivilegeListItem[] getItems() {
		return items;
	}
	public void setItems(BasePrivilegeListItem[] items) {
		this.items = items;
	}
	
	
}
