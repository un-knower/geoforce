package com.supermap.egispweb.httpclient;

import java.math.BigDecimal;
import java.util.List;

public class DituhuiPointResult {
	
	private List<PointAttributes> attributes;//字段
	private String title;//网点名称
	private String cid;//大众版id
	private double bdsmx;//百度经度
	private double bdsmy;//百度纬度
	private String bdxy;//百度经纬度
	
	private BigDecimal smx;//超图墨卡托x
	private BigDecimal smy;//超图墨卡托y
	private String address;//地址
	
	public List<PointAttributes> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<PointAttributes> attributes) {
		this.attributes = attributes;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public double getBdsmx() {
		return bdsmx;
	}
	public void setBdsmx(double bdsmx) {
		this.bdsmx = bdsmx;
	}
	public double getBdsmy() {
		return bdsmy;
	}
	public void setBdsmy(double bdsmy) {
		this.bdsmy = bdsmy;
	}
	public String getBdxy() {
		return bdxy;
	}
	public void setBdxy(String bdxy) {
		this.bdxy = bdxy;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	

}
