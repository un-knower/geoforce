package com.supermap.egispservice.base.entity;

import java.io.Serializable;

/**
 * @author CaoBin mailto:caobin@supermap.com
 * @version 1.0
 * @description 用户请求的点信息
 * @company SuperMap Software Co., Ltd.
 * @createDate 2015/9/6
 */
public class PointParam implements Serializable{
    private static final long serialVersionUID = 0;
    /**
     * 唯一代码，可以是ID，编号
     */
    private String code;
    private PointXY point;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public PointXY getPoint() {
        return point;
    }

    public void setPoint(PointXY point) {
        this.point = point;
    }
}
