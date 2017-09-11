package com.supermap.egispweb.consumer.store.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.common.AjaxResult;
import com.supermap.egispweb.consumer.store.StoreConsumer;
import com.supermap.egispweb.util.TranslateXYUtil;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.info.Dept;
import com.supermap.lbsp.provider.hibernate.lbsp.PersonStore;
import com.supermap.lbsp.provider.service.dept.DeptService;
import com.supermap.lbsp.provider.service.store.StoreService;

@Component("storeConsumer")
public class StoreConsumerImpl implements StoreConsumer {
	@Reference(version="2.5.3")
	private StoreService storeService;
	@Reference(version="2.5.3")
	private DeptService deptService;
	@Override
	public AjaxResult addStore(PersonStore store, UserEntity userEntity){
		AjaxResult res = new AjaxResult();
		if(StringUtils.isBlank(store.getName())){
			res.setStatus((short)0);
			res.setInfo("请输入门店名称");
			return res;
		}
		if(StringUtils.isBlank(store.getAddress())){
			res.setStatus((short)0);
			res.setInfo("请输入门店地址");
			return res;
		}
		if(StringUtils.isBlank(store.getTypeId())){
			res.setStatus((short)0);
			res.setInfo("请输选择门店类型");
			return res;
		}
		int row = storeService.hasName(store.getName(), userEntity.getEid().getId());
		if(row > 0){
			res.setStatus((short)0);
			res.setInfo("门店名称重复");
			return res;
		}
		Double[] tmp = TranslateXYUtil.bdEncrypt(store.getCtLng(), store.getCtLat());
		store.setEid(userEntity.getEid().getId());
		store.setBdLng(doubleToFiex(tmp[0].doubleValue(), 7));
		store.setBdLat(doubleToFiex(tmp[1].doubleValue(), 7));
		store.setDeptId(userEntity.getDeptId().getId());
		store.setUserId(userEntity.getId());
		store.setSource((short)2);
		store.setOperDate(new Date());
		PersonStore personStore =  storeService.addStore(store);
		if(personStore != null){
			res.setStatus((short)1);
			res.setInfo("操作成功");
		}
		return res;
	}

	@Override
	public AjaxResult delStore(String storeIds) {
		if(StringUtils.isBlank(storeIds)){
			return new AjaxResult((short)0,"请选择要删除的门店");
		}
		String[] idArr = storeIds.split(",");
		List<String> errStore = new ArrayList<String>();
		for (int i = 0; i < idArr.length; i++) {
			PersonStore personStore = storeService.getStore(idArr[i]);
			if( personStore!= null){
				int row = storeService.delStore(personStore);
				if(row!=1){
					errStore.add(idArr[i]);
				}
			}else{
				errStore.add(idArr[i]);
			}
		}
		if(errStore.size()>0){
			return new AjaxResult((short)0,"有"+errStore.size()+"个门店删除失败");
		}
		return new AjaxResult((short)1,"操作成功");
	}

	@Override
	public PersonStore getStoreInfo(String id) {
		
		return storeService.getStore(id);
	}

	@Override
	public Page getStoreList(String name, String deptCode, UserEntity userEntity, Page page) {
		HashMap<String,Object> hm = new HashMap<String, Object>();
		if(StringUtils.isBlank(deptCode)){
			deptCode = userEntity.getDeptId().getCode();
		}
		String eid = userEntity.getEid().getId();
		List<String> detptIds =  deptcodeConvertDeptId(deptCode);
		if(detptIds!= null && detptIds.size()>0){
			hm.put("deptIds", detptIds);
		}
		if(StringUtils.isNotBlank(eid)){
			hm.put("eid", eid);
		}
		if(StringUtils.isNotBlank(name)){
			hm.put("name", name);
			
		}
		page = storeService.queryStore(page, hm);
		return page;
	}
	
	@Override
	public Page getUnallocatedStoreList(String name,String deptCode,UserEntity userEntity,Page page){
		HashMap<String,Object> hm = new HashMap<String, Object>();
		
		if(StringUtils.isBlank(deptCode)){
			deptCode = userEntity.getDeptId().getCode();
		}
		String eid = userEntity.getEid().getId();
		List<String> detptIds =  deptcodeConvertDeptId(deptCode);
		if(detptIds!= null && detptIds.size()>0){
			hm.put("deptIds", detptIds);
		}
		if(StringUtils.isNotBlank(eid)){
			hm.put("eid", eid);
		}
		if(StringUtils.isNotBlank(name)){
			hm.put("name", name);
		}
		
		hm.put("personAllot", false);
		page = storeService.queryStore(page, hm);
		return page;
	}
	
	@Override
	public Page getStoreListPerson(String name, String personId,  Page page) {
		HashMap<String,Object> hm = new HashMap<String, Object>();
		if(StringUtils.isNotBlank(name)){
			hm.put("name", name);
			
		}
		if(StringUtils.isNotBlank(personId)){
			hm.put("personId", personId);
			
		}
		page = storeService.queryStoreByPersonPage(page, hm);
		List<Object[]> list = page.getResult();
		List<PersonStore> StoreList = new ArrayList<PersonStore>();
		for (int i = 0; i < list.size(); i++) {
			Object[] obj = list.get(i);
			PersonStore store = (PersonStore)obj[0];
			StoreList.add(store);
		}
		page.setResult(StoreList);
		return page;
	}

	@Override
	public AjaxResult updateStore(PersonStore store, UserEntity userEntity) {
		AjaxResult res = new AjaxResult();
		if(StringUtils.isBlank(store.getName())){
			res.setStatus((short)0);
			res.setInfo("请输入门店名称");
			return res;
		}
		if(StringUtils.isBlank(store.getAddress())){
			res.setStatus((short)0);
			res.setInfo("请输入门店地址");
			return res;
		}
		if(StringUtils.isBlank(store.getTypeId())){
			res.setStatus((short)0);
			res.setInfo("请输选择门店类型");
			return res;
		}
		PersonStore storeDb = storeService.getStore(store.getId());
		if(!store.getName().equals(storeDb.getName())&& storeService.hasName(store.getName(), userEntity.getEid().getId())>0){
			res.setStatus((short)0);
			res.setInfo("门店名称重复");
			return res;
		}
		storeDb.setAddress(store.getAddress());
		storeDb.setCtLat(store.getCtLat());
		storeDb.setCtLng(store.getCtLng());
		Double[] tmp = TranslateXYUtil.bdEncrypt(store.getCtLng(), store.getCtLat());
		storeDb.setBdLng(doubleToFiex(tmp[0].doubleValue(), 7));
		storeDb.setBdLat(doubleToFiex(tmp[1].doubleValue(), 7));
		storeDb.setName(store.getName());
		storeDb.setOperDate(new Date());
		storeDb.setIco(store.getIco());
		storeDb.setShopkeeperName(store.getShopkeeperName());
		storeDb.setShopkeeperPhone(store.getShopkeeperPhone());
		storeDb.setSource((short)2);
		storeDb.setUserId(userEntity.getId());
		storeDb.setDeptId(store.getDeptId());
		int row = storeService.updateStore(storeDb);
		if(row == 0){
			res.setStatus((short)0);
			res.setInfo("修改失败");
			return res;
		}
		res.setStatus((short)1);
		res.setInfo("修改成功");
		return res;
	}
	/**
	 * double截取精度，如精确到小数点后三位则num为3
	* @Title: doubleToFiex
	* @param db
	* @param num
	* @return
	* double
	* @throws
	 */
	private Float doubleToFiex(double d,int num){
		float ret = (float) 0.0;
		try {
			String result = String.format("%."+num+"f", d);
			ret = Float.valueOf(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	/**
	 * 部门code转化id
	 * @param deptcode
	 * @return
	 */
	private List<String> deptcodeConvertDeptId( String deptcode){
		
		if(deptcode != null){
			HashMap<String,Object> deptHm = new HashMap<String, Object>();
			deptHm.put("code", deptcode);
			List<String> listDeptId = new ArrayList<String>();
			List listDept = this.deptService.queryDept(null, deptHm);
			if(listDept != null && listDept.size()>0){
				for (int i = 0; i < listDept.size(); i++) {
					Dept dept = (Dept)listDept.get(i);
					listDeptId.add(dept.getId());
				}
				return listDeptId;
			}
			
			return null;
			
		}
		return null;
		
	}

	

}
