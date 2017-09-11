package com.supermap.egispservice.base.pojo;

import java.io.Serializable;

import com.supermap.egispservice.base.constants.EbossStatusConstants;



public class BaseOrderInfoListItem  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String username;
	private String status;
	private float totalAmount;
	private float consultPrice;
	private String email;
	private String telephone;
	private String submit_time;
	private int orderType = EbossStatusConstants.ORDER_TYPE_CUSTOM;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public float getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(float totalAmount) {
		this.totalAmount = totalAmount;
	}
	public float getConsultPrice() {
		return consultPrice;
	}
	public void setConsultPrice(float consultPrice) {
		this.consultPrice = consultPrice;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	public int getOrderType() {
		return orderType;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getSubmit_time() {
		return submit_time;
	}
	public void setSubmit_time(String submitTime) {
		submit_time = submitTime;
	}
	
}
