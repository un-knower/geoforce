package com.supermap.egispservice.base.pojo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Administrator
 *APP需要返回的数据结构
 */
public class AppPointPojo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String styleid;
	//private String groupid;
	private BigDecimal smx;
	private BigDecimal smy;
	private String name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStyleid() {
		return styleid;
	}
	public void setStyleid(String styleid) {
		this.styleid = styleid;
	}
	public BigDecimal getSmx() {
		return smx;
	}
	public void setSmx(BigDecimal smx) {
		this.smx = smx;
	}
	public BigDecimal getSmy() {
		return smy;
	}
	public void setSmy(BigDecimal smy) {
		this.smy = smy;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
