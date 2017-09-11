package com.supermap.egisp.addressmatch.beans;

import com.supermap.search.entity.SearchParam;


public class SearchParamBySM extends SearchParam {

	public SearchParamBySM(String address, String stAddress, String province, String city, String district) {
		super(address, stAddress, province, city, district);
	}
	
	
	
	private float score;

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}
	
	
	

}
