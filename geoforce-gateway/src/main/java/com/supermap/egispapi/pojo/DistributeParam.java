package com.supermap.egispapi.pojo;

import java.io.Serializable;
import java.util.List;

import com.supermap.egispservice.base.pojo.DistributeAddress;

public class DistributeParam  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 待解析的地址集合
	 */
	private List<DistributeAddress> addresses;
	
	/**
	 * 坐标类型
	 */
	private String type;
	
	/**
	 * 是否需要返回省市区
	 */
	private boolean needProv=false;
	
	/**
	 * 是否需要返回关联区划
	 */
	private boolean needRelArea=false;
	

	public boolean isNeedRelArea() {
		return needRelArea;
	}

	public void setNeedRelArea(boolean needRelArea) {
		this.needRelArea = needRelArea;
	}

	public boolean isNeedProv() {
		return needProv;
	}

	public void setNeedProv(boolean needProv) {
		this.needProv = needProv;
	}

	public List<DistributeAddress> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<DistributeAddress> addresses) {
		this.addresses = addresses;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
}
