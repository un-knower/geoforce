package com.supermap.egispservice.base.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispservice.base.dao.APILogDao;
import com.supermap.egispservice.base.entity.APILogEntity;

@Transactional(rollbackFor=Exception.class)
@Service("apiLogService")
public class APILogServiceImpl implements APILogService {

	@Autowired
	APILogDao apiLogDao;
	
	@Override
	public String saveLog(APILogEntity record) {
		// TODO Auto-generated method stub
		String id=null;
		record=this.apiLogDao.save(record);
		id=record.getId();
		return id;
	}

	@Override
	public List<APILogEntity> getLogListByParam(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String saveLogs(List<APILogEntity> record) {
		this.apiLogDao.save(record);
		return null;
	}
}
