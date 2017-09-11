package com.supermap.egispservice.lbs.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.supermap.egispservice.lbs.entity.RegionSet;
import com.supermap.egispservice.lbs.pojo.RegionSetRegionBean;


public interface RegionSetService {
	
	/**
	 * 添加
	 * @param regionSet
	 * @return
	 * @Author Juannyoh
	 * 2016-1-28上午11:20:46
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
	public List<RegionSetRegionBean> queryRegionSet(HashMap<String,Object> hm);
	
	/**
	 * 分页查询
	 * @param page
	 * @param hm
	 * @return
	 */
	
	public Map<String,Object> pagequeryRegionSet(HashMap<String,Object> hm);
	
}
