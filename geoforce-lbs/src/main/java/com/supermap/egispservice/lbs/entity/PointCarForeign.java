package com.supermap.egispservice.lbs.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "BIZ_MAP_POINT_CAR",catalog = "egisp_dev")
public class PointCarForeign  extends IdEntity implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pointId;
	private String carId;
	
	
	@Column(name = "point_id", length = 32)
	public String getPointId() {
		return pointId;
	}
	public void setPointId(String pointId) {
		this.pointId = pointId;
	}
	
	@Column(name = "car_id", length = 32)
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	
	

}
