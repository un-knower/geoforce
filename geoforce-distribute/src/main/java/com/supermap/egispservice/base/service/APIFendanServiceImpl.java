package com.supermap.egispservice.base.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispservice.base.dao.APIFendanDao;
import com.supermap.egispservice.base.entity.APIFendanEntity;
import com.supermap.egispservice.geocoding.service.IGeocodingService;
@Transactional(rollbackFor=Exception.class)
@Service("apiFendanService")
public class APIFendanServiceImpl implements APIFendanService {

	@Autowired
	APIFendanDao apiFendandao;
	
	@Autowired
	private IGeocodingService geocodingService;
	
	@Override
	public String saveFendanAPI(APIFendanEntity record) {
		String id=null;
		if(record.getResulttype().equals("1")||record.getResulttype().equals("2")){
			Map<String,String> map=this.geocodingService.searchAdmincodeForCounty(record.getSmx().doubleValue(), record.getSmy().doubleValue());
			if(map!=null){
				record.setProvince(map.get("PROVINCE"));
				record.setCity(map.get("CITY2"));
				record.setCounty(map.get("COUNTY"));
				record.setAdmincode(map.get("ADMINCODE"));
			}
		}
		record=this.apiFendandao.save(record);
		id=record.getId();
		return id;
	}
	
	@Override
	public String saveFendanAPIs(List<APIFendanEntity> record) {
		if(record!=null&&record.size()>0){
			for(APIFendanEntity en:record){
				if(en.getResulttype().equals("1")||en.getResulttype().equals("2")){
					Map<String,String> map=this.geocodingService.searchAdmincodeForCounty(en.getSmx().doubleValue(), en.getSmy().doubleValue());
					if(map!=null){
						en.setProvince(map.get("PROVINCE"));
						en.setCity(map.get("CITY2"));
						en.setCounty(map.get("COUNTY"));
						en.setAdmincode(map.get("ADMINCODE"));
					}
				}
			}
		}
		this.apiFendandao.save(record);
		return null;
	}

}
