package com.supermap.egispservice.pathplan.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.egispservice.base.entity.GroupType;
import com.supermap.egispservice.base.entity.Point;
import com.supermap.egispservice.base.entity.RouteTaskEntity;
import com.supermap.egispservice.base.entity.WeightNameType;

/**
 * 
 * @description 路径分析服务
 * @author CaoBin mailto:caobin@supermap.com
 * @company SuperMap Software Co., Ltd.
 * @createDate 2014-10-14
 * @version 1.0
 */
@Transactional
@Service
public class PathPlanServiceImpl implements IPathPlanService {

	private final Logger logger = Logger.getLogger(PathPlanServiceImpl.class);
	@Autowired
	private INavigateAnalystEngineerService navigateAnalystEngineerService;
	@Autowired
	private IDataGenerateService dataGenerateService;
	@Autowired
	private IRouteTaskService routeTaskService;
	@Autowired
	private IPathPlanJobService pathPlanJobService;

	/**
	 * 获取线路点长度
	 * 
	 * @param points
	 * @param returnDetail
	 * @return Map<String, Object> length：int:线路长度，detail：String：中文道路信息，flag：ok,error;info:描述信息
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> getLength(List<Point> points, boolean returnDetail) throws Exception {
		Point2Ds point2Ds = new Point2Ds();
		for (Point point : points) {
			Point2D point2d = new Point2D(point.getLon(), point.getLat());
			point2Ds.add(point2d);
		}
		double length = navigateAnalystEngineerService.calculateLength(navigateAnalystEngineerService.bestPathAnalyst(point2Ds, returnDetail));
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("length", length);
		m.put("flag", "ok");
		m.put("info", "成功");
		return m;
	}

	@Override
	public boolean planByVRPPath(String taskId, boolean prohibitViaduct, boolean returnDetail,GroupType groupType,WeightNameType weightNameType,Class classz) {
		try {
			RouteTaskEntity routeTaskEntity = routeTaskService.findById(taskId);
			pathPlanJobService.addJob(routeTaskEntity,groupType, weightNameType,classz);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean planByMTSPPath(List<Point> orderPoints, List<Point> netPoints, String taskId, boolean prohibitViaduct, boolean returnDetail) {
		try {
			// 订单
			Point2Ds orderPoint2Ds = new Point2Ds();
			for (Point point : orderPoints) {
				Point2D point2d = new Point2D(point.getLon(), point.getLat());
				orderPoint2Ds.add(point2d);
			}
			// 网点
			Point2D point2d = null;
			for (Point point : netPoints) {
				point2d = new Point2D(point.getLon(), point.getLat());
				break;
			}
			dataGenerateService.generateDataByMTSPPath(point2d, orderPoint2Ds, taskId, returnDetail, prohibitViaduct);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
