package com.supermap.lbsp.provider.service.store;

import java.util.List;

import com.supermap.lbsp.provider.hibernate.lbsp.PersonStoreType;

public interface StoreTypeService {
	public List<PersonStoreType> getStoreTypeList();
	
	public PersonStoreType getStoreType(String id);
}
