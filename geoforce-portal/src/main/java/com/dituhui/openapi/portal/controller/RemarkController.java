package com.dituhui.openapi.portal.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dituhui.openapi.base.service.IRemarkService;
import com.dituhui.openapi.entity.Remark;

@RestController
@RequestMapping("/remark")
public class RemarkController {

	@Reference
	private IRemarkService remarkService;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Map<String, Object> save(Remark remark) {
		try {
			remark.setCreateDate(new Timestamp(new Date().getTime()));
			remark = remarkService.save(remark);
			return buildResponseMap(true, 200, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buildResponseMap(false, 500, "error", null);
	}

	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Map<String, Object> delete(String id) {
		try {
			remarkService.delete(id);
			return buildResponseMap(true, 200, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buildResponseMap(false, 500, "error", null);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public Map<String, Object> update(Remark remark) {
		try {
			remark.setCreateDate(remarkService.findById(remark.getId()).getCreateDate());
			remarkService.update(remark);
			return buildResponseMap(true, 200, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buildResponseMap(false, 500, "error", null);
	}

	@RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
	public Map<String, Object> findById(@PathVariable String id) {
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("total", 1);
			result.put("data", remarkService.findById(id));
			return buildResponseMap(true, 200, null, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buildResponseMap(false, 500, "error", null);
	}

	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public Map<String, Object> query(int first, int max) {
		try {
			Long total = remarkService.queryCount();
			List<Remark> data = remarkService.queryPage(first, max);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("total", total);
			result.put("data", data);
			return buildResponseMap(true, 200, null, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buildResponseMap(false, 500, "error", null);
	}

	@RequestMapping(value = "/queryByRemarkDesc", method = RequestMethod.GET)
	public Map<String, Object> queryByRemarkDesc(int first, int max, String desc) {
		try {
			Long total = remarkService.queryByRemarkDescCount(desc);
			Map<String, Object> result = new HashMap<String, Object>();
			List<Remark> remarks = remarkService.queryPageByRemarkDesc(first, max, desc);
			result.put("total", total);
			result.put("data", remarks);
			return buildResponseMap(true, 200, null, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buildResponseMap(false, 500, "error", null);
	}

	private Map<String, Object> buildResponseMap(boolean success, int status, String info, Map<String, Object> result) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isSuccess", success);
		map.put("status", status);
		map.put("info", info);
		map.put("result", result);
		return map;
	}

}
