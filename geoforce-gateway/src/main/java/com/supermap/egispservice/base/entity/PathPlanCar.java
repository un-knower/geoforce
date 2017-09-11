package com.supermap.egispservice.base.entity;

import java.io.Serializable;

/**
 * 
 * @description 安排路径规划的车辆
 * @author CaoBin mailto:caobin@supermap.com
 * @company SuperMap Software Co., Ltd.
 * @createDate 2014-11-6
 * @version 1.0
 */
public class PathPlanCar implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	private String carId;
	
	private double cost;
	/**
	 * 承载订单数量
	 */
	private double loadOrderNumber;
	private double carLength;
	private double carWidth;
	private double carHeight;
	/**
	 * 体积
	 */
	private double volume;
	/**
	 * 载重
	 */
	private double loadWeight;
	/**
	 * 车主
	 */
	private String carOwner;
	/**
	 * 联系电话
	 */
	private String phone;
	/**
	 * 车牌
	 */
	private String carPlate;
	/**
	 * 车型
	 */
	private String carType;
	
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public double getLoadOrderNumber() {
		return loadOrderNumber;
	}
	public void setLoadOrderNumber(double loadOrderNumber) {
		this.loadOrderNumber = loadOrderNumber;
	}
	public double getCarLength() {
		return carLength;
	}
	public void setCarLength(double carLength) {
		this.carLength = carLength;
	}
	public double getCarWidth() {
		return carWidth;
	}
	public void setCarWidth(double carWidth) {
		this.carWidth = carWidth;
	}
	public double getCarHeight() {
		return carHeight;
	}
	public void setCarHeight(double carHeight) {
		this.carHeight = carHeight;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}
	public double getLoadWeight() {
		return loadWeight;
	}
	public void setLoadWeight(double loadWeight) {
		this.loadWeight = loadWeight;
	}
	public String getCarOwner() {
		return carOwner;
	}
	public void setCarOwner(String carOwner) {
		this.carOwner = carOwner;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCarPlate() {
		return carPlate;
	}
	public void setCarPlate(String carPlate) {
		this.carPlate = carPlate;
	}
	public String getCarType() {
		return carType;
	}
	public void setCarType(String carType) {
		this.carType = carType;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	@Override
	public String toString() {
		return "cost=" + cost + ", loadOrderNumber=" + loadOrderNumber + ", carLength=" + carLength + ", carWidth=" + carWidth + ", carHeight="
				+ carHeight + ", volume=" + volume + ", loadWeight=" + loadWeight + ", carOwner=" + carOwner + ", phone=" + phone + ", carPlate=" + carPlate
				+ ", carType=" + carType + ", carId=" + carId ;
	}
	
}
