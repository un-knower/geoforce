package com.supermap.lbsp.provider.bean;

import com.supermap.lbsp.provider.hibernate.lbsp.Region;
import com.supermap.lbsp.provider.hibernate.lbsp.RegionSet;

public class RegionSetRegionBean implements java.io.Serializable{
	/**
	 * 
	 */
	private Region region;
	private RegionSet regionSet;
	private String userName;
	private String typeName;
	private String id;
	private Integer typeCode;
	private String lngLan;
	private  String regionName;
	
	
	
	
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	
	
	public String getLngLan() {
		return lngLan;
	}
	public void setLngLan(String lngLan) {
		this.lngLan = lngLan;
	}
	public Integer getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(Integer typeCode) {
		this.typeCode = typeCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	public RegionSet getRegionSet() {
		return regionSet;
	}
	public void setRegionSet(RegionSet regionSet) {
		this.regionSet = regionSet;
	}
	
}
