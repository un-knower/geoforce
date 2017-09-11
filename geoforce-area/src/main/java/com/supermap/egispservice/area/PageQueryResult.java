package com.supermap.egispservice.area;

import java.io.Serializable;
import java.util.List;

public class PageQueryResult implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 总的页数
	private int total;
	// 第几页
	private int page;
	// 总的记录数
	private int records;
	// 记录对象集合
	private List<AreaEntity> rows;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRecords() {
		return records;
	}
	public void setRecords(int records) {
		this.records = records;
	}
	public List<AreaEntity> getRows() {
		return rows;
	}
	public void setRows(List<AreaEntity> rows) {
		this.rows = rows;
	}
	

}
