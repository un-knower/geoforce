package com.supermap.egispservice.base.pojo;

import java.io.Serializable;
import java.util.List;

public class BaseServiceModuleList  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int total;
	private int currentCount;
	private BaseServiceModuleListItem[] items;
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
	public BaseServiceModuleListItem[] getItems() {
		return items;
	}
	public void setItems(BaseServiceModuleListItem[] items) {
		this.items = items;
	}
	
	public void setItems(List<BaseServiceModuleListItem> items){
		if(null != items){
			this.items = new BaseServiceModuleListItem[items.size()];
			for(int i = 0;i<items.size();i++){
				this.items[i] = items.get(i);
			}
		}
	}
	
}
