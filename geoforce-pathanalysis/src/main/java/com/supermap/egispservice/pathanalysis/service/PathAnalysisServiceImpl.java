package com.supermap.egispservice.pathanalysis.service;

import com.supermap.analyst.networkanalyst.*;
import com.supermap.cloud.base.util.sm.coordinate.CoordinateTranslator;
import com.supermap.data.*;
import com.supermap.egispservice.base.entity.PointXY;
import com.supermap.egispservice.base.entity.WeightNameType;
import com.supermap.egispservice.pathanalysis.constant.Config;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.*;

/**
 * 
 * Title 导航分析
 * Description 采用object-java进行导航分析
 * 
 */
//@Service
public class PathAnalysisServiceImpl extends BasePathAnalystEngine implements IPathAnalysisService {

	@Autowired
	private Config config;

	private final Logger logger = Logger.getLogger(PathAnalysisServiceImpl.class);

	/**
	 * 最佳路径分析
	 */
	@Override
	public Map<String, Object> bestPathAnalyst(PointXY start,PointXY end,List<PointXY> passPoints, boolean returnDetail,WeightNameType weightNameType){
		TransportationAnalystParameter parameter = this.createTransportAnalysParam( start, end, passPoints, returnDetail,weightNameType);
		logger.info("开始分析最佳路线:");
		// 弧段数少不代表弧段长度短
		boolean hasLeastEdgeCount = false;
		long startTime = System.currentTimeMillis();
		TransportationAnalystResult result = getTransportationAnalyst().findPath(parameter, hasLeastEdgeCount);
        Map<String ,Object> m=getText(result);//包括文本信息和距离
        List<PointXY> path = getPath(result);
        m.put("path",path);
        result.dispose();
		long endTime = System.currentTimeMillis();
        logger.info("分析完成,耗时[秒]:" + (endTime - startTime) / 1000.0);
		return m;
	}

	/**
	 * 创建分析参数
	 * @param start
	 * @param end
	 * @param passPoints
	 * @param withDetail
	 * @param weightNameType
	 * @return
	 */
	protected TransportationAnalystParameter createTransportAnalysParam(PointXY start,PointXY end,List<PointXY> passPoints, boolean withDetail,WeightNameType weightNameType) {
		TransportationAnalystParameter parameter = new TransportationAnalystParameter();

		// 设置障碍点及障碍边
		int[] barrierEdges = new int[0];
		parameter.setBarrierEdges(barrierEdges);

		int[] barrierNodes = new int[0];
		parameter.setBarrierNodes(barrierNodes);

		// 设置权值字段信息的名字标识
		String [] weightNames=config.getWeightName().split(",");
		int weightNameIndex;
		if(WeightNameType.MileAndTruck==weightNameType){
			weightNameIndex=0;
		}else if(WeightNameType.MileBlockTruck ==weightNameType){
			weightNameIndex=1;
		}else if(WeightNameType.TimeAndTruck ==weightNameType){
			weightNameIndex=2;
		}else {
			weightNameIndex=3;
		}
		parameter.setWeightName(weightNames[weightNameIndex].trim());

		parameter.setPoints(buildPoint2Ds(start,end,passPoints));

		// 设置最佳路径分析的返回对象
		parameter.setRoutesReturn(true);
//		parameter.setEdgesReturn(withDetail);
//		parameter.setNodesReturn(withDetail);
		parameter.setPathGuidesReturn(withDetail);
//		parameter.setStopIndexesReturn(withDetail);

		return parameter;
	}

	/**
	 * 获取路径
	 * @param analystResult
	 * @return
	 */
	private List<PointXY> getPath(TransportationAnalystResult analystResult){
		if(null != analystResult){
			GeoLineM[] geoLines = analystResult.getRoutes();
			List<PointXY> pathPoints = new ArrayList<PointXY>();
			if(null != geoLines && geoLines.length > 0){
				for(int i=0;i<geoLines.length;i++){
					GeoLineM geoLineM = geoLines[i];
					PointMs pointMs = geoLineM.getPart(0);
					int pointCount = pointMs.getCount();
					for(int j=0;j<pointCount;j++){
						PointM pointM = pointMs.getItem(j);
						pointM.getX();
						PointXY point = new PointXY(pointM.getX(),pointM.getY());
						pathPoints.add(point);
					}

				}
				return pathPoints;
			}
		}
		return null;
	}


	/**
	 * 获取中文转向信息
	 * @param result
	 */
	public Map<String,Object> getText(TransportationAnalystResult result ) {
		PathGuide[] pathGuides = result.getPathGuides();
		int k = 0;
		StringBuilder show = new StringBuilder();
		double totalLength = 0;
		show.append("[");
		for (PathGuide pathGuide : pathGuides) {
			show.append("{").append("text:\"");
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
						show.append("到达[").append(item.getIndex()-1).append("号点],在道路").append(side).append(dis).append(";");
					} else {
						show.append("到达终点,在道路").append(side).append(dis);
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
						show.append("朝").append(direct).append("行走").append(Math.round(itemDistence )).append("米;");
					} else {
						String roadString = roadName.equals("") ? "匿名路段" : roadName;
						show.append("沿着").append(roadString).append(",朝").append(direct).append("行走")
								.append(Math.round( itemDistence)).append("米;");
					}

					length += item.getLength();
				}
			}
			totalLength += length;
//			show.append(totalLength*DISTENCE_METER_PER_DEGREE);
			if(k++==pathGuides.length-1){
				show.append("\"}");
			}else{
				show.append("\"},");
			}
		}
//		show.append(countLength);
		show.append("]");
        Map<String,Object> m=new HashMap<String, Object>();
        m.put("text", JSONArray.fromObject(show.toString()));
        m.put("dist",totalLength*config.getDistenceMeterPerDegree());

		return m;

	}

	public double getTotalWeight(TransportationAnalystResult result) {
		double totalWeight = 0;
		double[] weights = result.getWeights();
		for (double weight : weights) {
			totalWeight += weight;
		}
		logger.info("totalWeight = " + totalWeight);

		return totalWeight;
	}

	/**
	 * 销毁objects java对象
	 */
	@Override
	public void closeObjectJava() {
		super.destoryObjectJava();
	}

	/**
	 * 初始化objects java
	 */
	@Override
	public void initObjectJava() {
		boolean flag=false;
		if(config.getSwap()==1){
			flag=super.init();
		}else{
			flag=super.initOracle();//调用oracle数据库
		}
		if(!flag){
			logger.info("初始化obj java 获取数据 源 失败");
		}else logger.info("初始化obj java 获取数据 源成功");
	}

	private Point2Ds buildPoint2Ds(PointXY startPoint, PointXY endPoint, List<PointXY> passPoints){
		Point2Ds point2ds = new Point2Ds();
		point2ds.add(convert(startPoint));
		if(null != passPoints){
			for(PointXY p : passPoints){
				point2ds.add(new Point2D(p.getX(),p.getY()));
			}
		}
		point2ds.add(convert(endPoint));
		return point2ds;
	}


	private Point2D convert(PointXY point ){
		Point2D point2D = new Point2D();
		point2D.setX(point.getX());
		point2D.setY(point.getY());
		return point2D;
	}

	/**
	 * 查找一个点最近的垂点
	 */
	@Override
	public Map<String, Object> findNearestPoint(PointXY p) {
		// TODO Auto-generated method stub
		Map<String,Object> map=null;
		GeoPoint point = new GeoPoint(p.getX(), p.getY());
		long startTime = System.currentTimeMillis();
		Point2D nearestPoint =this.getNearestPoint(point);
		long endTime = System.currentTimeMillis();
		//System.out.println(nearestPoint);
		//System.out.println("calculate time(ms): " + (endTime - startTime));
		if(nearestPoint!=null){
			map=new HashMap<String,Object>();
			PointXY pxy=new PointXY();
			pxy.setX(nearestPoint.getX());
			pxy.setY(nearestPoint.getY());
			map.put("result", pxy);
		}
		return map;
	}
	
	
	public GeoLine getGeoLine(GeoPoint point) {
		// 设置查询参数
		QueryParameter parameter = new QueryParameter();
		parameter.setCursorType(CursorType.STATIC);
		parameter.setSpatialQueryMode(SpatialQueryMode.INTERSECT);
		parameter.setHasGeometry(true);
		double distance = 200;
		// 构建矩形
		GeoRegion region = this.getGeoRegion(point, distance);
		parameter.setSpatialQueryObject(region);

		// 进行查询
		Recordset recordset = BasePathAnalystEngine.APIdatasetVector.query(parameter);
		int recordCount = recordset.getRecordCount();
		recordset.moveFirst();
		double minDistance = 0;
		Geometry minDistanceGeometry = null;
		for (int i = 0; i < recordCount; i++) {
			Geometry geometry = recordset.getGeometry();
			double geometryDistance = Geometrist.distance(geometry, point);
			if (minDistanceGeometry == null || (minDistance > geometryDistance)) {
				minDistanceGeometry = geometry;
				minDistance = geometryDistance;
			}
			recordset.moveNext();
		}

		// 依次关闭所有对象
		recordset.close();
		recordset.dispose();

		return (GeoLine) minDistanceGeometry;
	}
	
	
	protected GeoRegion getGeoRegion(GeoPoint point, double distance) {
		// 经纬度转墨卡托
		CoordinateTranslator.Point point2d = new CoordinateTranslator.Point();
		point2d.setX(point.getX());
		point2d.setY(point.getY());
		CoordinateTranslator.lngLatToMercator(point2d);

		Point2Ds points = new Point2Ds();
		CoordinateTranslator.Point leftBottomPoint = new CoordinateTranslator.Point();
		leftBottomPoint.setX(point2d.getX() - distance / 2.0);
		leftBottomPoint.setY(point2d.getY() - distance / 2.0);
		// 墨卡托转经纬度
		CoordinateTranslator.mercatorToLngLat(leftBottomPoint);
		points.add(new Point2D(leftBottomPoint.getX(), leftBottomPoint.getY()));
		CoordinateTranslator.Point leftTopPoint = new CoordinateTranslator.Point();
		leftTopPoint.setX(point2d.getX() - distance / 2.0);
		leftTopPoint.setY(point2d.getY() + distance / 2.0);
		// 墨卡托转经纬度
		CoordinateTranslator.mercatorToLngLat(leftTopPoint);
		points.add(new Point2D(leftTopPoint.getX(), leftTopPoint.getY()));
		CoordinateTranslator.Point rightTopPoint = new CoordinateTranslator.Point();
		rightTopPoint.setX(point2d.getX() + distance / 2.0);
		rightTopPoint.setY(point2d.getY() + distance / 2.0);
		// 墨卡托转经纬度
		CoordinateTranslator.mercatorToLngLat(rightTopPoint);
		points.add(new Point2D(rightTopPoint.getX(), rightTopPoint.getY()));
		CoordinateTranslator.Point rightBottomPoint = new CoordinateTranslator.Point();
		rightBottomPoint.setX(point2d.getX() + distance / 2.0);
		rightBottomPoint.setY(point2d.getY() - distance / 2.0);
		// 墨卡托转经纬度
		CoordinateTranslator.mercatorToLngLat(rightBottomPoint);
		points.add(new Point2D(rightBottomPoint.getX(), rightBottomPoint.getY()));
		points.add(new Point2D(leftBottomPoint.getX(), leftBottomPoint.getY()));
		GeoRegion region = new GeoRegion(points);
		return region;
	}
	
	public Point2D getNearestPoint(GeoPoint point){
		Point2D nearestPoint=null;
		GeoLine geoLine = this.getGeoLine(point);
		if(point!=null&&geoLine!=null){
			// 获取垂足点
			nearestPoint = Geometrist.nearestPointToVertex(new Point2D(point.getX(), point.getY()), geoLine);
		}
		return nearestPoint;
	}

}
