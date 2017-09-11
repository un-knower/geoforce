package com.supermap.egispservice.pathanalysis.service;

import com.supermap.egispservice.base.entity.PointXY;
import com.supermap.egispservice.base.entity.WeightNameType;

import java.util.List;
import java.util.Map;

public interface IPathAnalysisService {

	/**
	 * 最近路径分析
	 * @param start
	 * @param end
	 * @param passPoints
	 * @param returnDetail
	 * @param weightNameType
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> bestPathAnalyst(PointXY start, PointXY end, List<PointXY> passPoints, boolean returnDetail, WeightNameType weightNameType) throws Exception ;

	/**
	 * 关闭objects java
	 */
	void closeObjectJava();

	/**
	 * 初始化objects java
	 */
	void initObjectJava();
	
	/**
	 * 查找一个点最近的垂点
	 * @param point
	 * @return
	 * @Author Juannyoh
	 * 2016-1-7下午2:56:15
	 */
	public Map<String,Object> findNearestPoint(PointXY point);


}