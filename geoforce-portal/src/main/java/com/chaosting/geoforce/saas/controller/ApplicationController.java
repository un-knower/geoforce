package com.chaosting.geoforce.saas.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
/*import com.dituhui.openapi.base.service.IApplicationService;
import com.dituhui.openapi.entity.Api;
import com.dituhui.openapi.entity.Application;*/

//@RestController
//@RequestMapping("/application")
public class ApplicationController {

	/*@Reference
	private IApplicationService applicationService;*/

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Map<String, Object> save(/*Application application,*/
			@RequestParam(value = "apiIds[]") List<String> apiIds) {
		try {
			/*List<Api> apis = new ArrayList<Api>(apiIds.size());
			for (String apiId : apiIds) {
				apis.add(new Api(apiId));
			}
			application.setApis(apis);
			application.setAppKey(UUID.randomUUID().toString().replaceAll("-", ""));

			applicationService.save(application);*/
			return buildResponseMap(true, 200, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buildResponseMap(false, 500, "error", null);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Map<String, Object> delete(String id) {
		try {
			/*applicationService.delete(id);*/
			return buildResponseMap(true, 200, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buildResponseMap(false, 500, "error", null);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Map<String, Object> update(/*Application applicationVo,*/
			@RequestParam(value = "apiIds[]") List<String> apiIds) {
		try {
			/*Application application = applicationService.findById(applicationVo.getId());
			
			List<Api> apis = new ArrayList<Api>(apiIds.size());
			for (String apiId : apiIds) {
				apis.add(new Api(apiId));
			}
			application.setApis(apis);
			application.setWhiteList(applicationVo.getWhiteList());

			applicationService.update(application);*/
			return buildResponseMap(true, 200, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buildResponseMap(false, 500, "error", null);
	}

	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public Map<String, Object> query(int first, int max) {
		/*try {
			Long total = applicationService.queryCount();
			List<Application> data = applicationService.queryPage(first, max);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("total", total);
			result.put("data", data);
			return buildResponseMap(true, 200, null, result);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return buildResponseMap(false, 500, "error", null);
	}

	@RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
	public Map<String, Object> findById(@PathVariable String id) {
		/*try {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("total", 1);
			result.put("data", applicationService.findById(id));
			return buildResponseMap(true, 200, null, result);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		return buildResponseMap(false, 500, "error", null);
	}

	private Map<String, Object> buildResponseMap(boolean success, int status,
			String info, Map<String, Object> result) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isSuccess", success);
		map.put("status", status);
		map.put("info", info);
		map.put("result", result);
		return map;
	}
}
