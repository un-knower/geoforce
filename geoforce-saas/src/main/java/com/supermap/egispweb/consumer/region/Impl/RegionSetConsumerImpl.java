package com.supermap.egispweb.consumer.region.Impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.supermap.egispweb.consumer.region.RegionSetConsumer;
import com.supermap.lbsp.provider.bean.RegionSetRegionBean;
import com.supermap.lbsp.provider.common.Page;

import com.supermap.lbsp.provider.hibernate.lbsp.AlarmType;
import com.supermap.lbsp.provider.hibernate.lbsp.RegionSet;
import com.supermap.lbsp.provider.service.region.RegionSetService;
@Component("regionSetConsumer")
public class RegionSetConsumerImpl implements RegionSetConsumer {
	@Reference(version="2.5.3")
	private RegionSetService regionSetService;

	@Override
	public int delRegionSet(RegionSet regionSet) {

		return this.regionSetService.delRegionSet(regionSet);
	}
	@Override
	public RegionSet getRegionSet(String id) {
		
		return this.regionSetService.getRegionSet(id);
	}
	@Override
	public List<RegionSetRegionBean> queryRegionSet(Page page,
			HashMap<String, Object> hm) {
		
		return this.regionSetService.queryRegionSet(page, hm);
	}
	@Override
	public int upadateRegionSet(RegionSet regionSet) {
		
		return this.regionSetService.upadateRegionSet(regionSet);
	}
	@Override
	public int addRegionSet(RegionSet regionSet) {

		return this.regionSetService.addRegionSet(regionSet);
	}
	@Override
	public Page pagequeryRegionSet(Page page, HashMap<String, Object> hm) {
		// TODO Auto-generated method stub
		return this.regionSetService.pagequeryRegionSet(page, hm);
	}
	
}
