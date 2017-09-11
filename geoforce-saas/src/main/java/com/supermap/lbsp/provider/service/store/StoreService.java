package com.supermap.lbsp.provider.service.store;

import java.util.HashMap;
import java.util.List;

import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonStore;

public interface StoreService {
	/**
	 * 
	 * @param 增加门店
	 * @return
	 */
	public PersonStore addStore(PersonStore Store);

	/**
	 * 修改门店
	 * 
	 * @param person
	 * @return
	 */

	public int updateStore(PersonStore Store);

	/**
	 * 根据ID获取门店
	 * @param id
	 * @return
	 */

	public PersonStore getStore(String id);

	/**
	 * 查询门店
	 * @param map
	 * @return
	 */
	public Page queryStore(Page page,HashMap hm);
	
	/**
	 * 
	 * @param 删除门店
	 * @return
	 */
	public int delStore(PersonStore Store);
	
	
	
	/**
	 * 检查名字是否重复
	 * @param name
	 * @param eid
	 * @return
	 */
	public int hasName(String name,String eid);
	
	/**
	 * 根据关联表查询门店
	 * @param page
	 * @param hm
	 * @return
	 */
	public List<Object[]> queryStoreByPerson(Page page,HashMap hm);
	public Page queryStoreByPersonPage(Page page,HashMap hm);
	
}
