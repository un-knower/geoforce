package com.supermap.egispservice.base.pojo;

import java.io.Serializable;
import java.util.List;


public class BaseOrderInfo  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userid;
	private String userName;
	private String orderId;
	private float totalPrice;
	private float consultPrice;
	private String submitTime;
	private String remarks;
	private String status;
	private String funcNames;
	private String telephone;
	private String email;
	private int orderType;
	private BaseOrderItem orderItems[] = null;
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
	public float getConsultPrice() {
		return consultPrice;
	}
	public void setConsultPrice(float consultPrice) {
		this.consultPrice = consultPrice;
	}
	public String getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public BaseOrderItem[] getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(BaseOrderItem[] orderItems) {
		this.orderItems = orderItems;
	}
	
	public void setOrderItems(List<BaseOrderItem> orderItems) {
		this.orderItems = new BaseOrderItem[orderItems.size()];
		for(int i = 0;i<this.orderItems.length;i++){
			this.orderItems[i] = orderItems.get(i);
		}
	}
	
	public String getFuncNames() {
		return funcNames;
	}
	public void setFuncNames(String funcNames) {
		this.funcNames = funcNames;
	}
	public int getOrderType() {
		return orderType;
	}
	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

}
