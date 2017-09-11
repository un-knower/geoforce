package com.supermap.egisp.ts.service;

import com.supermap.egisp.ts.pojo.Point;
import com.supermap.egisp.ts.pojo.TSAnalystResult;

public interface TransportationAnalystService {
	
	
	
	/**
	 * 
	 * <p>Title ：pathAnalyst</p>
	 * Description：			路径分析
	 * @param startPoint	起点
	 * @param endPoint		终点
	 * @param passPoints	途经点
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-12-17 上午09:51:34
	 */
	public TSAnalystResult pathAnalyst(Point startPoint,Point endPoint,Point[] passPoints);

}
