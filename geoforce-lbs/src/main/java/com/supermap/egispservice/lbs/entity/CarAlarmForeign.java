package com.supermap.egispservice.lbs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;




/**
 * CfgCarRegion entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "lbs_car_alarm_foreige", catalog = "egisp_dev")
public class CarAlarmForeign extends IdEntity  implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String carId;
	private String foreignId;
	private String type;
	

	// Constructors

	/** default constructor */
	public CarAlarmForeign() {
	}

	/** minimal constructor */
	public CarAlarmForeign(String id) {
		this.id = id;
	}

	/** full constructor */
	public CarAlarmForeign(String id, String carId, String foreignId,String type) {
		this.id = id;
		this.carId = carId;
		this.foreignId = foreignId;
		this.type= type;
	}


	@Column(name = "CAR_ID", length = 32)
	public String getCarId() {
		return this.carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}
	@Column(name = "FOREIGN_ID", length = 48)
	public String getForeignId() {
		return foreignId;
	}

	public void setForeignId(String foreignId) {
		this.foreignId = foreignId;
	}
	@Column(name = "Type", length = 32)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	

}