package com.dituhui.openapi.portal.vo;

import java.util.List;

import com.dituhui.openapi.entity.Api;

public class ApiBeanVo extends Api {
    private String dateTime;
    private List<ApiUpdateLogVo> years;

    public List<ApiUpdateLogVo> getYears() {
        return years;
    }

    public void setYears(List<ApiUpdateLogVo> years) {
        this.years = years;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
