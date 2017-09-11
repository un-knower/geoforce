package com.supermap.egispservice.base.service;

import java.util.List;
import java.util.Map;

import com.supermap.egispservice.base.entity.APIFendanEntity;
import com.supermap.egispservice.base.entity.APILogEntity;

public interface APIFendanService {
	
	public String saveFendanAPI(APIFendanEntity record);
	
	public String saveFendanAPIs(List<APIFendanEntity> record);
	
}
