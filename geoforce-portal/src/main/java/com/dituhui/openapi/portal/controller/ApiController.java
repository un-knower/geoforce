package com.dituhui.openapi.portal.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dituhui.openapi.base.service.IApiService;
import com.dituhui.openapi.base.service.IApiUpdateLogService;
import com.dituhui.openapi.base.service.IGroupSetService;
import com.dituhui.openapi.entity.Api;
import com.dituhui.openapi.entity.ApiUpdateLog;
import com.dituhui.openapi.entity.RequestParameter;
import com.dituhui.openapi.entity.ReturnResult;
import com.dituhui.openapi.portal.vo.ApiBeanVo;
import com.dituhui.openapi.portal.vo.ApiUpdateLogVo;

/**
 * Created by 9527 on 2017/11/13.
 */
@RestController
@RequestMapping("/api")
public class ApiController extends BaseController {
    private static final Logger logger = Logger.getLogger(ApiController.class);
    /**
     * 调用接口
     **/
    @Reference
    private IApiService apiService;

    @Reference
    private IGroupSetService groupSetService;

    @Reference
    private IApiUpdateLogService apiUpdateLogService;

    /**
     * 列表
     **/
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    Map<String, Object> list(HttpServletResponse response) throws Exception {
        try {
            List<Api> apiList = apiService.findAll();
            /*List<GroupVo> groupVoList = new ArrayList<>();
            Map<String, List<ApiVo>> listMap = new HashMap<>();
            for (int i = 0; i < apiList.size(); i++) {
                Api api = apiList.get(i);
                String groupId = api.getGroupSetId();
                ApiVo apiVo = new ApiVo();
                apiVo.setApiId(api.getId());
                apiVo.setApiName(api.getApiName());
                GroupVo groupVo = new GroupVo();
                if (listMap.containsKey(groupId)) {
                    listMap.get(groupId).add(apiVo);
                } else {
                    List<ApiVo> apiVoList = new ArrayList<>();
                    apiVoList.add(apiVo);
                    listMap.put(groupId, apiVoList);
                    groupVo.setGroupId(groupId);
                    groupVo.setGroupName(api.getGroupSet().getGroupName());
                    groupVoList.add(groupVo);
                    apiVoList.clear();
                    apiVoList = null;
                }
                apiVo = null;
                groupVo = null;
            }
            for (int i = 0; i < groupVoList.size(); i++) {
                String groupId = groupVoList.get(i).getGroupId();
                groupVoList.get(i).setApiVos(listMap.get(groupId));
            }*/
            Map<String, Object> result = new HashMap<>();
            result.put("total", apiList.size());
            result.put("data", apiList);
            return buildResponseMap(true, 200, null, result);
        } catch (Exception e) {
            logger.info("product select all fail in product/list request:", e);
        }
        return buildResponseMap(false, 500, "error", null);
    }

    /**
     * 单个信息
     **/
    @RequestMapping(value = "/findOne/{id}", method = RequestMethod.GET)
    Map<String, Object> one(@PathVariable("id") String id) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            //Api api = apiService.findById(id);
            //ApiVo2 apiVO2 = new ApiVo2();
            //apiVO2.setApi(api);
            //List<ApiUpdateLogVo> apiUpdateLogVoList = new ArrayList<>();
            //List<ApiUpdateLog> apiUpdateLogList = apiUpdateLogService.findByApiId(api.getId());
            //
            //Map<String, List<ApiUpdateLogVo>> map = new HashMap<>();
            ////第一步获取时间
            //for (int i = 0; i < apiUpdateLogList.size(); i++) {
            //    ApiUpdateLog apiUpdateLog = apiUpdateLogList.get(i);
            //    String[] date = sdf.format(apiUpdateLog.getCreateTime()).split("-");
            //    if (map.containsKey(date[0] + "年")){
            //
            //    }else{
            //
            //    }
            //}
            ApiBeanVo apiVo = new ApiBeanVo();
            Api api = apiService.findOne(id);
            Map<String, Object> result = new HashMap<>();

            if(null == api){
                result.put("total", 0);
                result.put("data", null);
                return result;
            }
            api.setRequestParameters(rpTree(api.getRequestParameters()));
            api.setReturnResults(rrTree(api.getReturnResults()));
            List<ApiUpdateLog> apiUpdateLogs = apiUpdateLogService.findByApiId(id);
            BeanUtils.copyProperties(api, apiVo);
            List<ApiUpdateLogVo> apiUpdateLogVoList = new ArrayList<>();
            Map<String, List<ApiUpdateLog>> map = new LinkedHashMap<>();

            for (int i = 0; i < apiUpdateLogs.size() ; i++) {
                ApiUpdateLog apiUpdateLog = apiUpdateLogs.get(i);
                String[] ymd = sdf.format(apiUpdateLog.getCreateTime()).split("-");
                String year = "";
                if(ymd.length > 0){
                    year = ymd[0] + "年";
                }
                if (map.containsKey(year)) {
                    map.get(year).add(apiUpdateLog);
                } else {
                    List<ApiUpdateLog> updateLogs = new ArrayList<>();
                    updateLogs.add(apiUpdateLog);
                    map.put(year, updateLogs);
                }
            }
            for (String year : map.keySet()) {
                ApiUpdateLogVo apiUpdateLogVo = new ApiUpdateLogVo();
                apiUpdateLogVo.setYear(year);
                apiUpdateLogVo.setLogs(map.get(year));
                apiUpdateLogVoList.add(apiUpdateLogVo);
            }
            apiVo.setYears(apiUpdateLogVoList);
            result.put("total", 1);
            result.put("data", apiVo);
            return buildResponseMap(true, 200, null, result);
        } catch (Exception e) {
            logger.info("product select one fail in product/findOne/{id} request:", e);
        }
        return buildResponseMap(false, 500, "error", null);
    }

    public LinkedList<RequestParameter> rpTree(List<RequestParameter> list){
        LinkedList<RequestParameter> topRequestParameter = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            RequestParameter rp = list.get(i);
            if(StringUtils.isEmpty(rp.getPname())){
                RequestParameter requestParameter = rp;
                requestParameter.setParameters(rpFindByName(list,rp.getApiParameterName()));
                topRequestParameter.add(requestParameter);
            }
        }
        return topRequestParameter;
    }
    //findByName
    public LinkedList<RequestParameter> rpFindByName(List<RequestParameter> list,String pname){
        LinkedList<RequestParameter> parameterList = new LinkedList<>();
        for (int i = 0; i < list.size() ; i++) {
            RequestParameter rp = list.get(i);
            if(!StringUtils.isEmpty(rp.getPname()) && pname.equals(rp.getPname())){
                rp.setParameters(rpFindByName(list,rp.getApiParameterName()));
                parameterList.add(rp);
            }
        }
        return parameterList;
    }

    public LinkedList<ReturnResult> rrTree(List<ReturnResult> list){
        LinkedList<ReturnResult> topReturnResult = new LinkedList<>();
        for (int i = 0; i < list.size(); i++) {
            ReturnResult rr = list.get(i);
            if(StringUtils.isEmpty(rr.getPname())){
                ReturnResult returnResult = rr;
                returnResult.setReturnResults(rrTindByName(list,rr.getName()));
                topReturnResult.add(returnResult);
            }
        }
        return topReturnResult;
    }
    //findByName
    public LinkedList<ReturnResult> rrTindByName(List<ReturnResult> list,String pname){
        LinkedList<ReturnResult> returnResults = new LinkedList<>();
        for (int i = 0; i < list.size() ; i++) {
            ReturnResult rr = list.get(i);
            if(!StringUtils.isEmpty(rr.getPname()) && pname.equals(rr.getPname())){
                rr.setReturnResults(rrTindByName(list,rr.getName()));
                returnResults.add(rr);
            }
        }
        return returnResults;
    }


}
