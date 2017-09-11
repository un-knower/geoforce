package com.supermap.egispweb.consumer.store.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispweb.consumer.store.StoreTypeConsumer;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonStoreType;
import com.supermap.lbsp.provider.service.store.StoreService;
import com.supermap.lbsp.provider.service.store.StoreTypeService;
@Component("storeTypeConsumer")
public class StoretypeConsumerImpl implements StoreTypeConsumer {
	@Reference(version="2.5.3")
	private StoreTypeService storeTypeService;
	@Override
	public PersonStoreType getStoreType(String id) {
		// TODO Auto-generated method stub
		return storeTypeService.getStoreType(id);
	}

	@Override
	public List<PersonStoreType> getStoreTypeList() {
		// TODO Auto-generated method stub
		return storeTypeService.getStoreTypeList();
	}

}
