package com.supermap.egispservice.base.pojo;

public class AddresInfoDetails{

	private String id;
	private String address;
	private boolean isAdminLess = false;
	private String from;
	private int resultType = 1;
	private String resultInfo;
	private String province;
	private String city;
	private String admincode;
	private String keyword;
	private double smx;
	private double smy;
	private boolean isNeedAddressMatch = true;
	
	
	public AddresInfoDetails(){}
	
	public AddresInfoDetails(String id,String address){
		this.id = id;
		this.address = address;
	}
	
	public boolean isAdminLess() {
		return isAdminLess;
	}
	public void setAdminLess(boolean isAdminLess) {
		this.isAdminLess = isAdminLess;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public int getResultType() {
		return resultType;
	}
	public void setResultType(int resultType) {
		this.resultType = resultType;
	}
	public String getResultInfo() {
		return resultInfo;
	}
	public void setResultInfo(String resultInfo) {
		this.resultInfo = resultInfo;
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
	public String getAdmincode() {
		return admincode;
	}
	public void setAdmincode(String admincode) {
		this.admincode = admincode;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public double getSmx() {
		return smx;
	}
	public void setSmx(double smx) {
		this.smx = smx;
	}
	public double getSmy() {
		return smy;
	}
	public void setSmy(double smy) {
		this.smy = smy;
	}
	public boolean isNeedAddressMatch() {
		return isNeedAddressMatch;
	}
	public void setNeedAddressMatch(boolean isNeedAddressMatch) {
		this.isNeedAddressMatch = isNeedAddressMatch;
	}
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
	
	
	
}
