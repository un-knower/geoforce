package com.dituhui.openapi.portal.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chaosting.base.util.http.HttpUtil;
import com.chaosting.geoforce.saas.bean.DistrictResult;
import com.chaosting.geoforce.saas.bean.Point;
import com.chaosting.geoforce.saas.common.Constant;

@RestController
@RequestMapping("/district")
public class DistrictController {

	@RequestMapping(value = "/getProvince", method = RequestMethod.GET)
	public List<DistrictResult> getProvince() {
		String response = requestDistrictGetProvince();
		return parseDistrictResult(response);
	}
	
	@RequestMapping(value = "/getChildDistrict", method = RequestMethod.GET)
	public List<DistrictResult> getChildDistrict(String admincode) {
		String response = requestDistrictGetChildDistrict(admincode);
		return parseDistrictResult(response);
	}
	
	@RequestMapping(value = "/getDistrict", method = RequestMethod.GET)
	public DistrictResult getDistrict(String admincode) {
		String response = requestDistrictGetDistrict(admincode);
		return parseDistrictResultWithGeo(response);
	}
	
	private String requestDistrictGetDistrict(String admincode) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("ak", Constant.AK);
		paramMap.put("admincode", admincode);
		paramMap.put("coordType", "gcj02ll");
		paramMap.put("includeRegion", "true");
		return new HttpUtil().doGetString(Constant.DISTRICT_GET_DISTRICT_URL, null, paramMap);
	}
	
	private String requestDistrictGetChildDistrict(String admincode) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("ak", Constant.AK);
		paramMap.put("admincode", admincode);
		return new HttpUtil().doGetString(Constant.DISTRICT_GET_CHILD_DISTRICT_URL, null, paramMap);
	}
	
	private String requestDistrictGetProvince() {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("ak", Constant.AK);
		return new HttpUtil().doGetString(Constant.DISTRICT_GET_PROVINCE_URL, null, paramMap);
	}
	
	private List<DistrictResult> parseDistrictResult(String response) {
		List<DistrictResult> list = new ArrayList<DistrictResult>();
		JSONArray array = JSONObject.parseObject(response).getJSONArray("result");
		for(int i=0; i<array.size(); i++) {
			JSONObject obj = array.getJSONObject(i);
			list.add(new DistrictResult(obj.getString("admincode"), obj.getString("name"), obj.getString("fullName"), obj.getInteger("level"), null));
		}
		return list;
	}
	
	private DistrictResult parseDistrictResultWithGeo(String response) {
		JSONObject obj = JSONObject.parseObject(response).getJSONObject("result");
		JSONArray array = obj.getJSONObject("region").getJSONArray("points");
		return new DistrictResult(obj.getString("admincode"), obj.getString("name"), obj.getString("fullName"), obj.getInteger("level"), array.toJavaList(Point.class));
	}
}
