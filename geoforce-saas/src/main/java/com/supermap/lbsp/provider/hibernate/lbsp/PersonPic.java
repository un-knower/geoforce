package com.supermap.lbsp.provider.hibernate.lbsp;


import java.util.Date;



public class PersonPic implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	
	private String id;
	private Short type;
	private String address;
	private Double bdLat;
	private Double bdLng;
	private Double ctLat;
	private Double ctLng;
	private String foreignId;
	private Date date;
	private String personId;
	private String url;
	private String remark;//照片说明

	// Constructors

	/** default constructor */
	public PersonPic() {
	}

	/** full constructor */
	public PersonPic(String id, Short type, String address,String url, String remark,  
			Double bdLat, Double bdLng, Double ctLat, Double ctLng,
			String foreignId, Date date, String personId) {
		this.id = id;
		this.type = type;
		this.address = address;
		this.bdLat = bdLat;
		this.bdLng = bdLng;
		this.ctLat = ctLat;
		this.ctLng = ctLng;
		this.foreignId = foreignId;
		this.date = date;
		this.personId = personId;
		this.url = url;
		this.remark=remark;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Short getType() {
		return this.type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getBdLat() {
		return this.bdLat;
	}

	public void setBdLat(Double bdLat) {
		this.bdLat = bdLat;
	}

	public Double getBdLng() {
		return this.bdLng;
	}

	public void setBdLng(Double bdLng) {
		this.bdLng = bdLng;
	}

	public Double getCtLat() {
		return this.ctLat;
	}

	public void setCtLat(Double ctLat) {
		this.ctLat = ctLat;
	}

	public Double getCtLng() {
		return this.ctLng;
	}

	public void setCtLng(Double ctLng) {
		this.ctLng = ctLng;
	}

	public String getForeignId() {
		return this.foreignId;
	}

	public void setForeignId(String foreignId) {
		this.foreignId = foreignId;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPersonId() {
		return this.personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}