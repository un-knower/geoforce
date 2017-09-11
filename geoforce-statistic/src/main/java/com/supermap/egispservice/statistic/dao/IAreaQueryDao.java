package com.supermap.egispservice.statistic.dao;

import java.util.Map;

public interface IAreaQueryDao {

	/**
	 * 根据网点id或者编号 查找区划名称
	 * 注：通过编号查询时最好同时传dcode
	 * @param id
	 * @param number
	 * @return
	 * @Author Juannyoh
	 * 2016-11-30上午10:18:14
	 */
	public String queryNameByIdOrNumber(String id,String number,String dcode);
	
	/**
	 * 查找关联区划信息
	 * @param relationareaid
	 * @return
	 */
	public Map<String,String> findFirstRelationAreaAttrs(String relationareaid);
	
	
	/**
	 * 根据区划名称及企业ID去查询区划信息
	 * @param name
	 * @param eid
	 * @return
	 */
	public Map<String,String> findAreaByNameAndEid(String name,String eid); 
}
