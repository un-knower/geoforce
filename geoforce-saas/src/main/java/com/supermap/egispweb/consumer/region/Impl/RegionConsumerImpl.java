package com.supermap.egispweb.consumer.region.Impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispweb.consumer.region.RegionConsumer;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.lbsp.Region;
import com.supermap.lbsp.provider.service.region.RegionService;

@Component("regionConsumer")
public class RegionConsumerImpl implements RegionConsumer {
	
	@Reference(version="2.5.3")
	private RegionService regionService;

	@Override
	public Region addRegion(Region region) {
		return this.regionService.addRegion(region);
	}

	@Override
	public int delRegion(Region region) {
		return this.regionService.delRegion(region);
	}

	@Override
	public Region getRegion(String id) throws Exception {
		return this.regionService.getRegion(id);
	}

	@Override
	public int hasName(String name, String eid) {
		return this.regionService.hasName(name, eid);
	}

	@Override
	public List<Region> queryRegion(Page page, HashMap<String, Object> hm)
			throws Exception {
		return this.regionService.queryRegion(page, hm);
	}

	@Override
	public int updateRegion(Region region) {
		return this.regionService.updateRegion(region);
	}

	@Override
	public List<Region> queryAllregion(Page page, HashMap<String,Object> hm) {
		// TODO Auto-generated method stub
		return this.regionService.queryAllregion(page, hm);
	}

}
