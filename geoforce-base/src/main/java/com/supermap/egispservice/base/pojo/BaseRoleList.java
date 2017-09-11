package com.supermap.egispservice.base.pojo;

import java.io.Serializable;

public class BaseRoleList  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int total;
	private int currentCount;
	private BaseRoleListItem items[];
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
	public BaseRoleListItem[] getItems() {
		return items;
	}
	public void setItems(BaseRoleListItem[] items) {
		this.items = items;
	}
	
}
