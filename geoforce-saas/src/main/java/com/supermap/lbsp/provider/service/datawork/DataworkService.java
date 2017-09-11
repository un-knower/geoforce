package com.supermap.lbsp.provider.service.datawork;

import java.util.HashMap;
import java.util.List;

import com.supermap.lbsp.provider.hibernate.lbsp.DataWordbook;

public interface DataworkService {
	public List<DataWordbook> getDataWordbookList(HashMap<String, Object> hm);
}
