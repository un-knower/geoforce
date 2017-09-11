package com.supermap.egisp.addressmatch.beans;

import java.util.List;

/**
 * 
 * <p>Title: POIAddressMatchByGeoParam</p>
 * Description:		带有范围的查询，如果points参数对象个数为1表示圆选范围，超过1个将被视为矩形。
 *
 * @author Huasong Huang
 * CreateTime: 2015-8-19 下午02:14:28
 */
public class POIAddressMatchByGeoParam extends AddressMatchParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int radius;
	private List<Point> points;
	private int pageNo;
	private int pageSize;
	private String filter;
	private String coorType;
	
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
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String getCoorType() {
		return coorType;
	}
	public void setCoorType(String coorType) {
		this.coorType = coorType;
	}
	
	

}
