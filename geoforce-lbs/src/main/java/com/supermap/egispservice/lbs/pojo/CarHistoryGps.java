package com.supermap.egispservice.lbs.pojo;

import java.io.Serializable;
import java.util.List;


/**
 * 
* ClassName：CarHistoryGps   
* 类描述：   车辆历史轨迹bean
* 操作人：wangshuang   
* 操作时间：2014-9-24 下午02:18:40     
* @version 1.0
 */
public class CarHistoryGps implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String carId;
	private String license;
	private List<CarGps> list;
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public List<CarGps> getList() {
		return list;
	}
	public void setList(List<CarGps> list) {
		this.list = list;
	}
	
}
