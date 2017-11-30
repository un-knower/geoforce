package com.chaosting.geoforce.saas.bean;

import java.io.Serializable;
import java.util.List;

public class DistrictResult implements Serializable {

	private static final long serialVersionUID = -7382253403135943191L;

	private String admincode;

	private String name;

	private String fullName;
	
	private Integer level;
	
	private List<Point> points;

	public DistrictResult(String admincode, String name, String fullName, Integer level, List<Point> points) {
		this.admincode = admincode;
		this.name = name;
		this.fullName = fullName;
		this.level = level;
		this.points = points;
	}

	public String getAdmincode() {
		return admincode;
	}

	public void setAdmincode(String admincode) {
		this.admincode = admincode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}

}
