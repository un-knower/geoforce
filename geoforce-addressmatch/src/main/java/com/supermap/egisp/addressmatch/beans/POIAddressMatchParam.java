package com.supermap.egisp.addressmatch.beans;

import java.util.List;

/**
 * 
 * <p>Title: POIAddressMatchParam</p>
 * Description:		POI搜索参数
 *
 * @author Huasong Huang
 * CreateTime: 2015-8-19 上午11:01:46
 */
public class POIAddressMatchParam extends AddressMatchParam {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String filter;
	private List<String> returnFields;
	private int startRecord;
	private int expectCount;
	
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public List<String> getReturnFields() {
		return returnFields;
	}
	public void setReturnFields(List<String> returnFields) {
		this.returnFields = returnFields;
	}
	public int getStartRecord() {
		return startRecord;
	}
	public void setStartRecord(int startRecord) {
		this.startRecord = startRecord;
	}
	public int getExpectCount() {
		return expectCount;
	}
	public void setExpectCount(int expectCount) {
		this.expectCount = expectCount;
	}
	
	
	

}
