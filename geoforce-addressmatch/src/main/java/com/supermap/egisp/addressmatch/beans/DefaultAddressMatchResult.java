package com.supermap.egisp.addressmatch.beans;

public class DefaultAddressMatchResult extends AddressMatchResult {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String from;
	private int resultType;
	private String resultInfo;
	private String province;
	private String city;
	private String county;
	private String admincode;
	private float score = 0;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getAdmincode() {
		return admincode;
	}
	public void setAdmincode(String admincode) {
		this.admincode = admincode;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	
	

}
