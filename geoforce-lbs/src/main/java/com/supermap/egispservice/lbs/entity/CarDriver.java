package com.supermap.egispservice.lbs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * InfoCarDriver entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "lbs_car_driver", catalog = "egisp_dev")
public class CarDriver extends IdEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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


	@Column(name = "CAR_ID", nullable = false, length = 48)
	public String getCarId() {
		return this.carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	@Column(name = "DRIVER_ID", nullable = false, length = 48)
	public String getDriverId() {
		return this.driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

}