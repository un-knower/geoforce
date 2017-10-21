package com.chaosting.geoforce.saas.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.chaosting.base.util.excel.ExcelUtil;
import com.chaosting.base.util.http.HttpUtil;
import com.chaosting.geoforce.saas.bean.GeocodingResult;
import com.chaosting.geoforce.saas.common.Constant;

@RestController
@RequestMapping("/geocoding")
public class GeocodingController {

	@RequestMapping(value = "/single", method = RequestMethod.GET)
	public GeocodingResult single(String address) {
		String response = requestGeocoding("1", address);
		return parseGeocodingResult(response, address);
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public Map<String, Object> upload(HttpServletRequest request) {
		List<GeocodingResult> list = new ArrayList<GeocodingResult>();
		try {
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
					request.getSession().getServletContext());
			if (multipartResolver.isMultipart(request)) {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				Iterator<String> iterator = multipartRequest.getFileNames();
				while (iterator.hasNext()) {
					MultipartFile file = multipartRequest.getFile(iterator.next());
					if (file != null) {
						ExcelUtil excel = ExcelUtil.open(file.getInputStream());
						List<String> ids = excel.getColumnContent(0, 0);
						if(ids.size() > Constant.GEOCODING_FREE_SIZE) {
							excel.close();
							return buildResult(10001, "error", null);
						}
						List<String> addresses = excel.getColumnContent(0, 1);
						excel.close();
						for (int i = 1; i < ids.size(); i++) {
							String id = ids.get(i);
							String address = addresses.get(i);
							if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(address)) {
								String response = requestGeocoding(id, address);
								list.add(parseGeocodingResult(response, address));
							}
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buildResult(10000, "ok", list);
	}

	private String requestGeocoding(String id, String address) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("ak", Constant.AK);
		paramMap.put("addresses", "[{\"id\":\"" + id + "\",\"address\":\"" + address + "\"}]");
		paramMap.put("type", Constant.GCJ02LL);
		return new HttpUtil().doGetString(Constant.GEOCODING_URL, null, paramMap);
	}

	private GeocodingResult parseGeocodingResult(String response, String address) {
		JSONObject json = JSONObject.parseObject(response).getJSONArray("result").getJSONObject(0);
		return new GeocodingResult(json.getString("id"), address, json.getString("x"), json.getString("y"));
	}
	
	private Map<String, Object> buildResult(int status, String info, List<GeocodingResult> list) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		map.put("info", info);
		if (list != null) {
			map.put("result", list);
		}
		return map;
	}
}
