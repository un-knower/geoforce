package com.supermap.egispweb.consumer.dataWork;

import java.util.HashMap;
import java.util.List;

import com.supermap.egispservice.lbs.entity.DataWordbook;

//import com.supermap.lbsp.provider.hibernate.lbsp.DataWordbook;

public interface DataWorkConsumer {
	public List<DataWordbook> getDataWordbookList(HashMap<String, Object> hm);
}
