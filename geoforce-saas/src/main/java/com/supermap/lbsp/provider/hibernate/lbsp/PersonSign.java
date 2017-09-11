package com.supermap.lbsp.provider.hibernate.lbsp;

import java.util.Date;


public class PersonSign implements java.io.Serializable {

	// Fields
	
	
	private String id;
	private String planId;
	private String personId;
	private String storeId;
	private Short results;
	private String stayDate;
	private Double startLng;
	private Double startLat;
	private Double startBdLng;
	private Double startBdLat;
	private Date startDate;
	private Float startDistance;
	private Double endBdLat;
	private Double endBdLng;
	private Double endLat;
	private Double endLng;
	private Date endDate;
	private Float endDistance;
	private Date operDate;

	// Constructors

	/** default constructor */
	public PersonSign() {
	}

	/** full constructor */
	public PersonSign(String id, String planId, String personId,
			String storeId, Short results, String stayDate, Double startLng,
			Double startLat, Double startBdLng, Double startBdLat,
			Date startDate, Float startDistance, Double endBdLat,
			Double endBdLng, Double endLat, Double endLng, Date endDate,
			Float endDistance, Date operDate) {
		this.id = id;
		this.planId = planId;
		this.personId = personId;
		this.storeId = storeId;
		this.results = results;
		this.stayDate = stayDate;
		this.startLng = startLng;
		this.startLat = startLat;
		this.startBdLng = startBdLng;
		this.startBdLat = startBdLat;
		this.startDate = startDate;
		this.startDistance = startDistance;
		this.endBdLat = endBdLat;
		this.endBdLng = endBdLng;
		this.endLat = endLat;
		this.endLng = endLng;
		this.endDate = endDate;
		this.endDistance = endDistance;
		this.operDate = operDate;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlanId() {
		return this.planId;
	}

	public void setPlanId(String planId) {
		this.planId = planId;
	}

	public String getPersonId() {
		return this.personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getStoreId() {
		return this.storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public Short getResults() {
		return this.results;
	}

	public void setResults(Short results) {
		this.results = results;
	}

	public String getStayDate() {
		return this.stayDate;
	}

	public void setStayDate(String stayDate) {
		this.stayDate = stayDate;
	}

	public Double getStartLng() {
		return this.startLng;
	}

	public void setStartLng(Double startLng) {
		this.startLng = startLng;
	}

	public Double getStartLat() {
		return this.startLat;
	}

	public void setStartLat(Double startLat) {
		this.startLat = startLat;
	}

	public Double getStartBdLng() {
		return this.startBdLng;
	}

	public void setStartBdLng(Double startBdLng) {
		this.startBdLng = startBdLng;
	}

	public Double getStartBdLat() {
		return this.startBdLat;
	}

	public void setStartBdLat(Double startBdLat) {
		this.startBdLat = startBdLat;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Float getStartDistance() {
		return this.startDistance;
	}

	public void setStartDistance(Float startDistance) {
		this.startDistance = startDistance;
	}

	public Double getEndBdLat() {
		return this.endBdLat;
	}

	public void setEndBdLat(Double endBdLat) {
		this.endBdLat = endBdLat;
	}

	public Double getEndBdLng() {
		return this.endBdLng;
	}

	public void setEndBdLng(Double endBdLng) {
		this.endBdLng = endBdLng;
	}

	public Double getEndLat() {
		return this.endLat;
	}

	public void setEndLat(Double endLat) {
		this.endLat = endLat;
	}

	public Double getEndLng() {
		return this.endLng;
	}

	public void setEndLng(Double endLng) {
		this.endLng = endLng;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Float getEndDistance() {
		return this.endDistance;
	}

	public void setEndDistance(Float endDistance) {
		this.endDistance = endDistance;
	}

	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

}