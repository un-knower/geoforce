package com.supermap.egispapi.service;

import java.util.List;

import com.supermap.egispapi.pojo.GeocodingParam;
import com.supermap.egispapi.pojo.GeocodingResult;

public interface IGeocodingService {
	

	public List<GeocodingResult> addressMatch(List<GeocodingParam> addresses,String type);
	
	
}
