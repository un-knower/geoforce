package com.supermap.lbsp.provider.hibernate.lbsp;



/**
 * InfoCarDriver entity. @author MyEclipse Persistence Tools
 */

public class CarDriver implements java.io.Serializable {

	// Fields

	private String id;
	private String carId;
	private String driverId;

	// Constructors

	/** default constructor */
	public CarDriver() {
	}

	/** full constructor */
	public CarDriver(String id, String carId, String driverId) {
		this.id = id;
		this.carId = carId;
		this.driverId = driverId;
	}

	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public String getCarId() {
		return this.carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	
	public String getDriverId() {
		return this.driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

}