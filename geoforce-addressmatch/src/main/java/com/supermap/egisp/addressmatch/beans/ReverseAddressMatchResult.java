package com.supermap.egisp.addressmatch.beans;

/**
 * 
 * <p>Title: ReverseAddressMatchResult</p>
 * Description:		反向地址解析结果
 *
 * @author Huasong Huang
 * CreateTime: 2015-8-19 上午10:05:35
 */
public class ReverseAddressMatchResult extends AddressMatchResult{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double distande;
	private String poiId;
	private String address;
	private String province;
	private String city;
	private String county;
	private String town;
	private String name;
	public double getDistande() {
		return distande;
	}
	public void setDistande(double distande) {
		this.distande = distande;
	}
	public String getPoiId() {
		return poiId;
	}
	public void setPoiId(String poiId) {
		this.poiId = poiId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
	
	
	
}
