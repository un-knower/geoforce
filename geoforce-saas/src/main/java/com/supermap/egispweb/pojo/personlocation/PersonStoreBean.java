package com.supermap.egispweb.pojo.personlocation;

import java.util.Date;
import java.util.List;

import com.supermap.lbsp.provider.hibernate.lbsp.PersonPic;

/**
 * 
* ClassName：PersonStoreBean   
* 类描述：   巡店人员定位 门店信息bean
* 操作人：wangshuang   
* 操作时间：2014-12-1 下午02:57:27     
* @version 1.0
 */
public class PersonStoreBean {
	private String id;
	private String eid;
	private String deptId;
	private String name;
	private String shopkeeperName;
	private String shopkeeperPhone;
	private double bdLat;
	private double bdLng;
	private double ctLat;
	private double ctLng;
	private String typeId;
	private Short source;
	private String address;
	private Date operDate;
	private String userId;
	private List<PersonPic> pics;
	private String eidName;
	private String deptName;
	private String typeName;
	private String ico;
	public String getIco() {
		return ico;
	}
	public void setIco(String ico) {
		this.ico = ico;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEid() {
		return eid;
	}
	public void setEid(String eid) {
		this.eid = eid;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShopkeeperName() {
		return shopkeeperName;
	}
	public void setShopkeeperName(String shopkeeperName) {
		this.shopkeeperName = shopkeeperName;
	}
	public String getShopkeeperPhone() {
		return shopkeeperPhone;
	}
	public void setShopkeeperPhone(String shopkeeperPhone) {
		this.shopkeeperPhone = shopkeeperPhone;
	}
	public double getBdLat() {
		return bdLat;
	}
	public void setBdLat(double bdLat) {
		this.bdLat = bdLat;
	}
	public double getBdLng() {
		return bdLng;
	}
	public void setBdLng(double bdLng) {
		this.bdLng = bdLng;
	}
	public double getCtLat() {
		return ctLat;
	}
	public void setCtLat(double ctLat) {
		this.ctLat = ctLat;
	}
	public double getCtLng() {
		return ctLng;
	}
	public void setCtLng(double ctLng) {
		this.ctLng = ctLng;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public Short getSource() {
		return source;
	}
	public void setSource(Short source) {
		this.source = source;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<PersonPic> getPics() {
		return pics;
	}
	public void setPics(List<PersonPic> pics) {
		this.pics = pics;
	}
	public String getEidName() {
		return eidName;
	}
	public void setEidName(String eidName) {
		this.eidName = eidName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
