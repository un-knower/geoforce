package com.supermap.egispservice.area;

import java.io.Serializable;

public class Point2D implements Serializable{

	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double x;
	private double y;
	public Point2D(){}

	public Point2D(double x, double y) {

		this.x = x;
		this.y = y;
	}
	
	public double getX(){ 
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
	
	
	
}
