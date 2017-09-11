package com.supermap.lbsp.provider.hibernate.lbsp;

public class CarAlarmForeign implements java.io.Serializable {
	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String id;
	private String carId;
	private String foreignId;
	private String type;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	public String getForeignId() {
		return foreignId;
	}
	public void setForeignId(String foreignId) {
		this.foreignId = foreignId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
