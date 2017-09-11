package com.supermap.egisp.addressmatch.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {

	/**
	 * 批量提交地址解析的最大值
	 */
	@Value("#{configParam['batch.size.max']}")
	private int batchMaxSize = 50;
	
	@Value("#{configParam['district.required']}")
	private Boolean districtRequired = true;

	
	@Value("#{configParam['score.high']}")
	private Float scoreHign;
	
	@Value("#{configParam['score.low']}")
	private Float scoreLow;
	
	@Value("#{configParam['default.strategy']}")
	private Integer defaultStrategy = 3;
	
	public int getBatchMaxSize() {
		return batchMaxSize;
	}

	public void setBatchMaxSize(int batchMaxSize) {
		this.batchMaxSize = batchMaxSize;
	}

	public Boolean isDistrictRequired() {
		return districtRequired;
	}

	public void setDistrictRequired(Boolean districtRequired) {
		this.districtRequired = districtRequired;
	}

	public Float getScoreHign() {
		return scoreHign;
	}

	public void setScoreHign(Float scoreHign) {
		this.scoreHign = scoreHign;
	}

	public Float getScoreLow() {
		return scoreLow;
	}

	public void setScoreLow(Float scoreLow) {
		this.scoreLow = scoreLow;
	}

	public Integer getDefaultStrategy() {
		return defaultStrategy;
	}

	public void setDefaultStrategy(Integer defaultStrategy) {
		this.defaultStrategy = defaultStrategy;
	}
	

	
	
}
