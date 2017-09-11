package com.supermap.egispapi.pojo;

import java.util.List;

/**
 * @author CaoBin mailto:caobin@supermap.com
 * @version 1.0
 * @description
 * @company SuperMap Software Co., Ltd.
 * @createDate 2015/7/8
 */
public class PathPlanParam {
    private Point startPoint;//起点
    private List<Point> targetPoints;//途经点
    private int type=1;//查询模式 {1: 距离最短模式, 2: 时间最快模式}
    private int textInfo=0;//返回文本信息，可选{0：不返回（默认），1：返回文本信息}

    public PathPlanParam() {
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public List<Point> getTargetPoints() {
        return targetPoints;
    }

    public void setTargetPoints(List<Point> targetPoints) {
        this.targetPoints = targetPoints;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTextInfo() {
        return textInfo;
    }

    public void setTextInfo(int textInfo) {
        this.textInfo = textInfo;
    }

}
