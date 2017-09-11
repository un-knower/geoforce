package com.supermap.egisp.addressmatch.beans;

import java.io.Serializable;

/**
 * 
 * <p>Title: AddressMatchParam</p>
 * Description:		地址匹配的参数
 *
 * @author Huasong Huang
 * CreateTime: 2015-8-17 下午03:45:12
 */
public class AddressMatchParam implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String address;
	private String admincode;
	private int qs;
	private int level;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAdmincode() {
		return admincode;
	}
	public void setAdmincode(String admincode) {
		this.admincode = admincode;
	}
	public int getQs() {
		return qs;
	}
	public void setQs(int qs) {
		this.qs = qs;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	
}
