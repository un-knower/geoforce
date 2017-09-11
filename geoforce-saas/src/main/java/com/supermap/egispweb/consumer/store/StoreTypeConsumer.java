package com.supermap.egispweb.consumer.store;

import java.util.List;

import com.supermap.lbsp.provider.hibernate.lbsp.PersonStoreType;

public interface StoreTypeConsumer {
public List<PersonStoreType> getStoreTypeList();
	
	public PersonStoreType getStoreType(String id);
}
