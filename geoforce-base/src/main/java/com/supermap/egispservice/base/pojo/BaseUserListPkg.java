package com.supermap.egispservice.base.pojo;

import java.io.Serializable;

public class BaseUserListPkg  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int totalCount;
	private int currentCount;
	private BaseUserListInfo[] userInfos;
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getCurrentCount() {
		return currentCount;
	}
	public void setCurrentCount(int currentCount) {
		this.currentCount = currentCount;
	}
	public BaseUserListInfo[] getUserInfos() {
		return userInfos;
	}
	public void setUserInfos(BaseUserListInfo[] userInfos) {
		this.userInfos = userInfos;
	}
	
	
	
}
