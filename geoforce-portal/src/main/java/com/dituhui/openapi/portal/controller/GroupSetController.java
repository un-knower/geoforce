package com.dituhui.openapi.portal.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONArray;
import com.dituhui.openapi.base.service.IApiService;
import com.dituhui.openapi.base.service.IGroupSetService;

@RestController
@RequestMapping("groupSet")
public class GroupSetController extends BaseController {
    private static final Logger LOGGER = Logger.getLogger(GroupSetController.class);

    @Reference
    private IGroupSetService groupSetService;
    @Reference
    private IApiService apiService;

    @RequestMapping("findColumn")
    public Map<String, Object> findColumn() {
        try {
        	JSONArray list = this.groupSetService.findColumnByRedis();
            Map<String, Object> map = new HashMap<>();
            map.put("total", list.size());
            map.put("data", list);
            return buildResponseMap(true, 200, "", map);
        } catch (Exception e) {
            LOGGER.info("e:", e);
            return buildResponseMap(false, 500, "服务异常", null);
        }
    }

    @RequestMapping("findAll")
    public Map<String, Object> findAll() {
        try {
            JSONArray list = this.groupSetService.loadAllByRedis();
            Map<String, Object> map = new HashMap<>();
            map.put("total", list.size());
            map.put("data", list);
            return buildResponseMap(true, 200, "", map);
        } catch (Exception e) {
            LOGGER.info("e:", e);
            return buildResponseMap(false, 500, "服务异常", null);
        }
    }


}
