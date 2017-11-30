package com.dituhui.openapi.portal.vo;

import java.util.List;

import com.dituhui.openapi.entity.ApiUpdateLog;

public class ApiUpdateLogVo {
    private String year;
    private List<ApiUpdateLog> logs;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public List<ApiUpdateLog> getLogs() {
        return logs;
    }

    public void setLogs(List<ApiUpdateLog> logs) {
        this.logs = logs;
    }
}
