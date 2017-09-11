package com.supermap.egispweb.consumer.store;

import javax.servlet.http.HttpSession;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.common.AjaxResult;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonStore;

public interface StoreConsumer {
	/**
	 * 返回门店列表
	 * @param name
	 * @param deptCode
	 * @param session
	 * @return
	 */
	public Page getStoreList(String name,String deptCode,UserEntity userEntity, Page page);
	public Page getStoreListPerson(String name,String personId, Page page);
	public Page getUnallocatedStoreList(String name,String deptCode,UserEntity userEntity,Page page);
	
	/**
	 * 添加门店
	 * @param Store
	 * @param userEntity
	 * @return
	 */
	public AjaxResult addStore(PersonStore Store,UserEntity userEntity);
	
	/**
	 * 修改门店
	 * @param Store
	 * @param userEntity
	 * @return
	 */
	public AjaxResult updateStore(PersonStore Store,UserEntity userEntity);
	
	
	/**
	 * 删除门店
	 * @param Store
	 * @param userEntity
	 * @return
	 */
	public AjaxResult delStore(String storeIds);
	
	/**
	 * 得到门店信息
	 * @param id
	 * @return
	 */
	public PersonStore getStoreInfo(String id);

	
}
