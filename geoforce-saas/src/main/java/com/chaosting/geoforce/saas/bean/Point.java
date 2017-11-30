package com.chaosting.geoforce.saas.bean;

import java.io.Serializable;

public class Point implements Serializable {

	private static final long serialVersionUID = -3226279458295557783L;

	private Double x;
	
	private Double y;

	public Double getX() {
		return x;
	}

	public void setX(Double x) {
		this.x = x;
	}

	public Double getY() {
		return y;
	}

	public void setY(Double y) {
		this.y = y;
	}
}
