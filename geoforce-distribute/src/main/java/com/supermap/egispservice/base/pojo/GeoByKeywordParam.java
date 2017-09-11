package com.supermap.egispservice.base.pojo;

import java.io.Serializable;

/**
 * 
 * <p>Title: GeoByKeywordParam</p>
 * Description:关键词查询的参数结构
 *
 * @author Huasong Huang
 * CreateTime: 2015-5-4 上午10:31:08
 */
public class GeoByKeywordParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String filter;
	private int pageNo;
	private int pageSize;
	private Geometry4KeywordParam geometry;
	
	
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
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
	public Geometry4KeywordParam getGeometry() {
		return geometry;
	}
	public void setGeometry(Geometry4KeywordParam geometry) {
		this.geometry = geometry;
	}
	
	
	
}
