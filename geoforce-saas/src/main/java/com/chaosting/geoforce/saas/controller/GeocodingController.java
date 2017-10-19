package com.chaosting.geoforce.saas.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.chaosting.base.util.http.HttpUtil;
import com.chaosting.geoforce.saas.common.Constant;

@RestController
@RequestMapping("/geocoding")
public class GeocodingController {

	@RequestMapping(value = "/single", method = RequestMethod.GET)
	public JSONObject single(String address) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("ak", Constant.AK);
		paramMap.put("addresses", genAddresses("1", address));
		paramMap.put("type", Constant.GCJ02LL);
		String result = new HttpUtil().doGetString(Constant.GEOCODING_URL, null, paramMap);
		return JSONObject.parseObject(result);
	}

	private String genAddresses(String id, String address) {
		return "[{\"id\":\"" + id + "\",\"address\":\"" + address + "\"}]";
	}
}
