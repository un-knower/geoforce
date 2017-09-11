package com.supermap.egispapi.pojo;

public class OrderQueryParam {

	private String batch;
	private int pageSize = 10;
	private int pageNo = 1;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.batch + "," + this.pageNo + "," + this.pageSize;
	}
	public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	
	
}
