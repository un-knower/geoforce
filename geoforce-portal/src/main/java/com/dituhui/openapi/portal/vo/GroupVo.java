package com.dituhui.openapi.portal.vo;

import java.util.List;

/**
 * Created by 9527 on 2017/11/15.
 */
public class GroupVo {
    private String groupId;
    private String groupName;
    private List<ApiVo> apiVos;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<ApiVo> getApiVos() {
        return apiVos;
    }

    public void setApiVos(List<ApiVo> apiVos) {
        this.apiVos = apiVos;
    }
}
