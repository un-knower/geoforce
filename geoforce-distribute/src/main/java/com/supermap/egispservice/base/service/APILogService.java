package com.supermap.egispservice.base.service;

import java.util.List;
import java.util.Map;

import com.supermap.egispservice.base.entity.APILogEntity;

public interface APILogService {
	public String saveLog(APILogEntity record);
	public String saveLogs(List<APILogEntity> record);
	
	public List<APILogEntity> getLogListByParam(Map<String,Object> map);
}
