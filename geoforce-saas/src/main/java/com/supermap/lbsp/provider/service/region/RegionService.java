package com.supermap.lbsp.provider.service.region;

import java.util.HashMap;
import java.util.List;

import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.Region;

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

	/**查询区域
	 * 
	 * @return
	 */
	public List<Region> queryRegion(Page page, HashMap<String,Object> hm) throws Exception;
	

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
	
	public List<Region> queryAllregion(Page page, HashMap<String,Object> hm);

}
