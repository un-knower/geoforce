package com.supermap.egispweb.consumer.region;

import java.util.HashMap;
import java.util.List;

import com.supermap.lbsp.provider.bean.RegionSetRegionBean;
import com.supermap.lbsp.provider.common.Page;

import com.supermap.lbsp.provider.hibernate.lbsp.AlarmType;
import com.supermap.lbsp.provider.hibernate.lbsp.RegionSet;

public interface RegionSetConsumer {
	/**
	 * 
	 * @param 增加 
	 * @return
	 */
	public int addRegionSet(RegionSet regionSet) ;
	/**
	 * 
	 * @param 修改
	 * @return
	 */
	public int upadateRegionSet(RegionSet regionSet) ;
	
	/**
	 * 根据主键ID查询
	 * 
	 * @param id
	 * @return
	 */
	public RegionSet getRegionSet(String id);
	/**
	 * 删除
	 * @param personInfo
	 * @return
	 */
	public int delRegionSet(RegionSet regionSet);
	/**
	 * 查询
	 * @return
	 */
	public List<RegionSetRegionBean> queryRegionSet(Page page, HashMap<String,Object> hm);
	
	
	
	public Page pagequeryRegionSet(Page page,HashMap<String,Object> hm);
	
}
