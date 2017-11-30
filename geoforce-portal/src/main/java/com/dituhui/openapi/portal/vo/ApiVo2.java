package com.dituhui.openapi.portal.vo;

import java.util.List;

import com.dituhui.openapi.entity.Api;

public class ApiVo2 {
    private Api api;
    private List<ApiUpdateLogVo> list;

    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }

    public List<ApiUpdateLogVo> getList() {
        return list;
    }

    public void setList(List<ApiUpdateLogVo> list) {
        this.list = list;
    }
}
