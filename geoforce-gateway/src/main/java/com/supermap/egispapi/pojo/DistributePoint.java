package com.supermap.egispapi.pojo;


public class DistributePoint {

	private String id;
	private double x;
	private double y;
	
	public DistributePoint() {
	}
	
	public DistributePoint(String id,double x,double y){
		this.x = x;
		this.y = y;
		this.id=id;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
