package com.supermap.egispservice.lbs.service;

import java.util.HashMap;
import java.util.List;

import com.supermap.egispservice.lbs.entity.DataWordbook;


public interface DataworkService {
	
	public List<DataWordbook> getDataWordbookList(HashMap<String, Object> hm);
}
