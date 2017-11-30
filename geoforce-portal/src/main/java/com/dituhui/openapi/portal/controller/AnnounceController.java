package com.dituhui.openapi.portal.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dituhui.openapi.base.service.IAnnounceService;
import com.dituhui.openapi.entity.Announce;

@RestController
@RequestMapping("announce")
public class AnnounceController {
    private static final Logger LOGGER = Logger.getLogger(AnnounceController.class);

    @Reference
    private IAnnounceService announceService;

    //查询
    @RequestMapping(value = "/query")
    public Map<String, Object> query(@RequestParam(defaultValue = "1", required = false) int first,
                                     @RequestParam(defaultValue = "10", required = false) int max) {
        try {
            Long count = announceService.queryCount("");
            List<Announce> announceList = announceService.queryPage(first, max, "");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("data", announceList);
            map.put("total", count);
            return buildResponseMap(true, 200, null, map);
        } catch (Exception r) {
            LOGGER.info("", r);
            return buildResponseMap(false, 500, "", null);
        }

    }

    //查询单个
    @RequestMapping(value = "/findById/{id}", method = RequestMethod.GET)
    public Map<String, Object> findById(@PathVariable String id) {
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            Announce announce = announceService.findById(id);
            result.put("total", 1);
            result.put("data", announce);
            return buildResponseMap(true, 200, null, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
