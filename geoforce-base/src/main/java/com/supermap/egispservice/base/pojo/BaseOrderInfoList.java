package com.supermap.egispservice.base.pojo;

import java.io.Serializable;
import java.util.List;

public class BaseOrderInfoList  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int total = 0;
	private int currentCount = 0;
	private BaseOrderInfoListItem[] items;
	
	private BaseOrderInfo[] infos;
	
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
	public BaseOrderInfoListItem[] getItems() {
		return items;
	}
	public void setItems(BaseOrderInfoListItem[] items) {
		this.items = items;
	}
	public void setItems(List<BaseOrderInfoListItem> items) {
		if(null != items){
			this.items = new BaseOrderInfoListItem[items.size()];
			for(int i=0;i<items.size();i++){
				this.items[i] = items.get(i);
			}
		}
	}
	public BaseOrderInfo[] getInfos() {
		return infos;
	}
	public void setInfos(BaseOrderInfo[] infos) {
		this.infos = infos;
	}
	public void setInfos(List<BaseOrderInfo> infos) {
		if(null != infos){
			this.infos = new BaseOrderInfo[infos.size()];
			for(int i=0;i<infos.size();i++){
				this.infos[i] = infos.get(i);
			}
		}
	}
	
	
	
	
}
