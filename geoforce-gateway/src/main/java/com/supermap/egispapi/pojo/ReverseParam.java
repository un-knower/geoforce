package com.supermap.egispapi.pojo;

import com.supermap.egispservice.base.entity.PointParam;

import java.util.List;

/**
 * @author CaoBin mailto:caobin@supermap.com
 * @version 1.0
 * @description
 * @company SuperMap Software Co., Ltd.
 * @createDate 2015/9/7
 */
public class ReverseParam {
	
	private String type;
    private List<PointParam> points;
    private int from = 0;

    public List<PointParam> getPoints() {
        return points;
    }

    public void setPoints(List<PointParam> points) {
        this.points = points;
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}
    
    
    
}
