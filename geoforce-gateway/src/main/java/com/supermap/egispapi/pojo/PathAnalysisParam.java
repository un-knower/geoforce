package com.supermap.egispapi.pojo;

import com.supermap.egispservice.base.entity.PointXY;

import java.util.List;

/**
 * @author CaoBin mailto:caobin@supermap.com
 * @version 1.0
 * @description
 * @company SuperMap Software Co., Ltd.
 * @createDate 2015/7/14
 */
public class PathAnalysisParam {
    private PointXY startPoint;//起点
    private PointXY endPoint;//终点
    private List<PointXY> targetPoints;//途经点
    private int type=1;//查询模式 {1: 距离最短模式, 2: 时间最快模式}
    private int textInfo=0;//返回文本信息，可选{0：不返回（默认），1：返回文本信息}
    private int pathInfo=1;//返回坐标信息，可选{0：不返回，1：返回坐标信息（默认）}

    public PathAnalysisParam() {
    }

    public PointXY getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(PointXY startPoint) {
        this.startPoint = startPoint;
    }

    public PointXY getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(PointXY endPoint) {
        this.endPoint = endPoint;
    }

    public List<PointXY> getTargetPoints() {
        return targetPoints;
    }

    public void setTargetPoints(List<PointXY> targetPoints) {
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

    public int getPathInfo() {
        return pathInfo;
    }

    public void setPathInfo(int pathInfo) {
        this.pathInfo = pathInfo;
    }
}
