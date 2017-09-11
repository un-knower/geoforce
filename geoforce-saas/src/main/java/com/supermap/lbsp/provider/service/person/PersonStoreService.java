package com.supermap.lbsp.provider.service.person;

import com.supermap.lbsp.provider.hibernate.lbsp.PersonStoreForeign;

public interface PersonStoreService {
	/**
	 * 添加关联表对象-人员门店
	 * @param person
	 * @return
	 */
	public PersonStoreForeign addPersonStoreDao(PersonStoreForeign personStore);

	
	public int delPersonStoreDao(String personid,String storeid);
	
	 public int delPersonStoreByPersonId(String personid);
}
