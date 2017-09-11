package com.supermap.egispservice.lbs.service;

import java.util.HashMap;
import java.util.Map;

import com.supermap.egispservice.lbs.entity.Region;


/**
 * 车辆围栏报警
 * @author wxn
 *
 */
public interface RegionService {
	/**
	 * 增加
	 * 
	 * @param fenceInfo
	 * @return
	 */
	public Region addRegion(Region region);

	/**
	 * 修改
	 * 
	 * @param fenceInfo
	 * @return
	 */
	public int updateRegion(Region region);

	/**
	 * 删除
	 * @param fenceInfo
	 * @return
	 */
	public int delRegion(Region region);

	/**查询区域  分页
	 * 
	 * @return
	 */
	public Map<String,Object> queryFence(HashMap<String,Object> hm) throws Exception;
	

	/**
	 * 通过Id获取区域
	 * @param id
	 * @return
	 */
	public Region getRegion(String id) throws Exception;
	
	/**
	 * 校验名称是否存在
	 * @param name
	 * @param eid 
	 * @return
	 */
	public int hasName(String name, String eid);
	
	/**
	 * 查询所有的  围栏   分页
	 * @param hm
	 * @return
	 * @Author Juannyoh
	 * 2016-1-28上午9:59:47
	 */
	public Map<String,Object> queryAllregion(HashMap<String,Object> hm);

}
