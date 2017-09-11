package com.supermap.egispservice.geocoding.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {
	
	
	@Value("#{configParam['dataset.name.province']}")
	private String datasetNameProvince;
	@Value("#{configParam['dataset.name.city']}")
	private String datasetNameCity;
	@Value("#{configParam['dataset.name.county']}")
	private String datasetNameCounty;
	@Value("#{configParam['dataset.name.town']}")
	private String datasetNameTown;
	
	public static final int ADMIN_LEVEL_PROVINCE = 1;
	public static final int ADMIN_LEVEL_CITY = 2;
	public static final int ADMIN_LEVEL_COUNTY = 3;
	public static final int ADMIN_LEVEL_TOWN = 4;
	
	public String getDatasetNameProvince() {
		return datasetNameProvince;
	}
	public void setDatasetNameProvince(String datasetNameProvince) {
		this.datasetNameProvince = datasetNameProvince;
	}
	public String getDatasetNameCity() {
		return datasetNameCity;
	}
	public void setDatasetNameCity(String datasetNameCity) {
		this.datasetNameCity = datasetNameCity;
	}
	public String getDatasetNameCounty() {
		return datasetNameCounty;
	}
	public void setDatasetNameCounty(String datasetNameCounty) {
		this.datasetNameCounty = datasetNameCounty;
	}
	
	
	public String getDatasetNameTown() {
		return datasetNameTown;
	}
	public void setDatasetNameTown(String datasetNameTown) {
		this.datasetNameTown = datasetNameTown;
	}

}
