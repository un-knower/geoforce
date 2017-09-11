package com.supermap.egispservice.pathplan.pojo;

import org.apache.commons.math3.ml.clustering.Clusterable;

public class Target implements Clusterable {
	private double x;
	private double y;
	private double weight;
	private int index;

	public double[] getPoint() {
		double[] value = { this.x, this.y};
		return value;
	}

	public String toString() {
		return "["+ this.index+"]";
//		return "["+this.index+"," + this.x + ", " + this.y + ", " + this.weight + "]";
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

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	
}