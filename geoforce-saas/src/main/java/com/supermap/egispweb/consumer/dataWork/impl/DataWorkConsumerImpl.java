package com.supermap.egispweb.consumer.dataWork.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispservice.lbs.entity.DataWordbook;
import com.supermap.egispservice.lbs.service.DataworkService;
import com.supermap.egispweb.consumer.dataWork.DataWorkConsumer;
/*import com.supermap.lbsp.provider.hibernate.lbsp.DataWordbook;
import com.supermap.lbsp.provider.service.datawork.DataworkService;*/
@Component("dataWorkConsumer")
public class DataWorkConsumerImpl implements DataWorkConsumer {
	@Reference(version="2.5.3")
	private DataworkService dataworkService;
	@Override
	public List<DataWordbook> getDataWordbookList(HashMap<String, Object> hm) {
		
		return dataworkService.getDataWordbookList(hm);
	}

}
