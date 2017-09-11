package com.supermap.egispservice.pathplan.service;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.supermap.analyst.networkanalyst.*;
import com.supermap.cloud.base.util.sm.coordinate.CoordinateTranslator;
import com.supermap.data.*;
import com.supermap.egispservice.base.entity.GroupType;
import com.supermap.egispservice.base.entity.PathPlanCar;
import com.supermap.egispservice.base.entity.Point;
import com.supermap.egispservice.base.entity.WeightNameType;
import com.supermap.egispservice.pathplan.constant.Config;
import com.supermap.egispservice.pathplan.util.FileUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PathPlanServiceAPIImpl implements IPathPlanServiceAPI {
	@Autowired
	private Config config;
	@Autowired
	private INavigateAnalystEngineerService navigateAnalystEngineerService;
	private final Logger logger = Logger.getLogger(PathPlanServiceAPIImpl.class);

    /**
     * 路径分析API接口
     */
    @Override
    public Map<String, Object> generateDataByVRPPathForAPI(List<Point> netPoints, List<Point> orderPoints,boolean withDetail,GroupType groupType,WeightNameType weightNameType) throws Exception {
        //1. 订单
        List<Point2D> orderPointsList = new ArrayList<Point2D>();
        for (Point point : orderPoints) {
            Point2D point2d = new Point2D(point.getLon(), point.getLat());
            orderPointsList.add(point2d);
        }
        // 网点
        List<Point2D> netPoints2 = new ArrayList<Point2D>();
        for (Point point : netPoints) {
            Point2D point2d = new Point2D(point.getLon(), point.getLat());
            netPoints2.add(point2d);
        }
        // 3.将导航结果生成json对象
        Map<String, Object> m =null;
        if(groupType==GroupType.NoneGroup){
            //API使用单辆车运载所有订单，不考虑多辆车，多条路线的情况
            List<PathPlanCar> pathPlanCars=new ArrayList<PathPlanCar>();
            PathPlanCar car =new PathPlanCar();
            car.setLoadOrderNumber(config.getLoadOrderNumber());
            pathPlanCars.add(car);

            m = generatePathDataByVRPPath(orderPointsList, netPoints2,pathPlanCars, withDetail, weightNameType);
        }
        return m;
    }

	
	/**
	 * 使用findVRPPath方法实现路径分析
	 * 
	 * @param orderPointsList
	 * @param netPoints
	 * @param withDetail
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> generatePathDataByVRPPath(List<Point2D> orderPointsList, List<Point2D> netPoints,List<PathPlanCar> pathPlanCars, boolean withDetail,WeightNameType weightNameType ) throws Exception {
		//ArrayList<Point2Ds> orderPointsList = navigateAnalystEngineerService.sortOrders(netPoints, orderPointsList, config.getCarLoad(), GroupType.RadialGroup);
		
		Map<String, Object> m=new HashMap<String, Object>();
		List<JSONObject> jsonPointList = new ArrayList<JSONObject>();
		int[] parts = null;// 在fetchResultForVRPPath方法中初始化
		// 分析
		logger.info("导航分析中(findVRPPath)......");
		TransportationAnalystResult result = navigateAnalystEngineerService.findVRPPath(orderPointsList, netPoints,pathPlanCars, withDetail, weightNameType);
		if (result != null) {
			parts = new int[result.getRoutes().length];
			fetchResultForVRPPath(result, jsonPointList, parts);
			JSONObject path=convertLineToJson(jsonPointList, parts);
			String stopIndexes=fetchStopIndexes(result,0);
			String pathGuides=showResult(result);
			m.put("path", path);
			m.put("stopIndexes", JSONArray.fromObject(stopIndexes));
			m.put("pathGuides", JSONArray.fromObject(pathGuides));
			result.dispose();
		} else {
			logger.error("导航分析结果为null");
		}
		return m;
	}





	private static JSONObject convertLineToJson(List<JSONObject> pointList, int[] parts) {
		if (pointList == null || pointList.size() == 0)
			throw new RuntimeException("pointList is null/empty");

		JSONObject pointJson = new JSONObject();
		pointJson.accumulate("point2Ds", pointList);
		pointJson.accumulate("parts", parts);
		JSONObject resultJson = new JSONObject();
		resultJson.accumulate("result", pointJson);
		return resultJson;
	}


	/**
	 * 获取中文转向信息
	 * @param result
	 */
	public String showResult(TransportationAnalystResult result ) {
		PathGuide[] pathGuides = result.getPathGuides();
		int k = 0;
		StringBuilder show = new StringBuilder();
		double totalLength = 0;
		String countLength = "";
		show.append("[");
		for (PathGuide pathGuide : pathGuides) {
			show.append("{index:").append(k).append(",text:\"");
			double length = 0;
			for (int j = 1; j < pathGuide.getCount(); j++) {
				PathGuideItem item = pathGuide.get(j);
				// 导引子项为站点的添加方式
				if (item.isStop()) {
					String side = "无";
					if (item.getSideType() == SideType.LEFT)
						side = "左侧";
					if (item.getSideType() == SideType.RIGHT)
						side = "右侧";
					if (item.getSideType() == SideType.MIDDLE)
						side = "道路上";
//					String dis = NumberFormat.getInstance().format(item.getDistance());
					String dis = Math.round(item.getDistance())+"米";
					dis=dis.equals("0米")?"":dis;
					if (item.getIndex() == -1 && item.getID() == -1) {
						continue;
					}
					if (j != pathGuide.getCount() - 1) {
						show.append("到达[").append(item.getIndex()-1).append("号点],在道路").append(side).append(dis).append("<br>");
					} else {
//						show.append("到达终点,在道路").append(side).append(dis).append("<br>\",length:");
						show.append("到达终点,在道路").append(side).append(dis).append("<br>\"");//不保存长度
					}
				}
				// 导引子项为弧段的添加方式
				if (item.isEdge()) {
					String direct = "直行";
					if (item.getDirectionType() == DirectionType.EAST)
						direct = "东";
					if (item.getDirectionType() == DirectionType.WEST)
						direct = "西";
					if (item.getDirectionType() == DirectionType.SOUTH)
						direct = "南";
					if (item.getDirectionType() == DirectionType.NORTH)
						direct = "北";
					String weight = NumberFormat.getInstance().format(item.getWeight());
					String roadName = item.getName();
					double itemDistence=item.getLength()* config.getDistenceMeterPerDegree();
					if (weight.equals("0") && roadName.equals("")) {
						show.append("朝").append(direct).append("行走").append(Math.round(itemDistence)).append("米<br>");
					} else {
						String roadString = roadName.equals("") ? "匿名路段" : roadName;
						show.append("沿着").append(roadString).append(",朝").append(direct).append("行走")
						.append(Math.round( itemDistence)).append("米<br>");
//						show.append("沿着[").append(roadString).append("(EdgId:").append(item.getID()).append(")],朝").append(direct).append("行走")
//						.append(Math.round( item.getLength()*100000)).append("米<br>");
					}

					length += item.getLength();
				}
			}
			totalLength += length;
			countLength += length + "_";
//			show.append(length);//不保存长度
			if(k++==pathGuides.length-1){
				show.append("}");
			}else{
				show.append("},");
			}
		}
		countLength += totalLength;
		//show.append(countLength);
		show.append("]");
		return show.toString();

	}
	/**
	 * 返回stopindexes 途径节点的顺序
	 * type:步长。通过遍历多个结果集的路径，需要合并为一个stopindex，则里面的值需要根据步长累加。
	 * @param result
	 * @return
	 */
	private String fetchStopIndexes(TransportationAnalystResult result,int step){

		StringBuffer sb=new StringBuffer();
		sb.append("[");
		int [][] stopIndexs=result.getStopIndexes();
		int k=0;
		int lineNumber=result.getRoutes().length;
		if(lineNumber!=stopIndexs.length){
			k=lineNumber;
		}
		for (int i = k; i < stopIndexs.length; i++) {
			sb.append("[");
			int [] pathStopIndexs=stopIndexs[i];
			for (int j = 0; j < pathStopIndexs.length; j++) {
				if(j==pathStopIndexs.length-1){
					sb.append(pathStopIndexs[j]+step);
				}else{
					sb.append(pathStopIndexs[j]+step).append(",");
				}
			}
			if(i==stopIndexs.length-1){
				sb.append("]");
			}else{
				sb.append("],");
			}
		}
		sb.append("]");
		return sb.toString();
	}
	/**
	 * 从TransportationAnalystResult提取道路轨迹
	 */
	private void fetchResultForVRPPath(TransportationAnalystResult result, List<JSONObject> jsonPointList, int[] parts) {
		GeoLineM[] linems = result.getRoutes();
		if (linems == null) {
			logger.info("result.getRoutes() == null");
		} else if (linems.length == 0) {
			logger.info("result.getRoutes().length == 0");
		} else {
			logger.info("linems.length = " + linems.length);
			for (int i = 0; i < linems.length; i++) {
				GeoLineM line = linems[i];

				int linePartCount = line.getPartCount();
				logger.info("linePartCount = " + linePartCount);
				// 假设linePartCount都是1
				PointMs part = line.getPart(0);
				int pointCount = part.getCount();
				logger.info("pointCount = " + pointCount);
				parts[i] += pointCount;
				for (int j = 0; j < pointCount; j++) {
					PointM pointM = part.getItem(j);
					JSONObject jsonPoint = new JSONObject();
					// 将经纬度转为墨卡托点
					jsonPoint.accumulate("x", CoordinateTranslator.lngToMercator(pointM.getX()));
					jsonPoint.accumulate("y", CoordinateTranslator.latToMercator(pointM.getY()));
					jsonPointList.add(jsonPoint);
				}
			}
		}
	}

}
