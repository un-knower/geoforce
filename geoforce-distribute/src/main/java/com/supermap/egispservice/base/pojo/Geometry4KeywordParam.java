package com.supermap.egispservice.base.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * <p>Title: Geometry4KeywordParam</p>
 * Description:	关键词查询中的空间对象参数
 *
 * @author Huasong Huang
 * CreateTime: 2015-5-4 上午10:29:42
 */
public class Geometry4KeywordParam implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int radius;
	private List<Point> points;
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
	public List<Point> getPoints() {
		return points;
	}
	public void setPoints(List<Point> points) {
		this.points = points;
	}
	
	
	
}
