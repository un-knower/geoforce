package com.supermap.egisp.ts.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.supermap.analyst.networkanalyst.TransportationAnalyst;
import com.supermap.analyst.networkanalyst.TransportationAnalystParameter;
import com.supermap.analyst.networkanalyst.TransportationAnalystResult;
import com.supermap.convert.impl.BaiduCoordinateConvertImpl;
import com.supermap.convert.impl.SuperMapCoordinateConvertImpl;
import com.supermap.data.GeoLineM;
import com.supermap.data.Point2D;
import com.supermap.data.Point2Ds;
import com.supermap.data.PointM;
import com.supermap.data.PointMs;
import com.supermap.egisp.ts.TransportationAnalystManager;
import com.supermap.egisp.ts.constants.TSConstants;
import com.supermap.egisp.ts.pojo.Point;
import com.supermap.egisp.ts.pojo.TSAnalystResult;
import com.supermap.egisp.ts.service.TransportationAnalystService;
@Component("tsService")
public class TransportationAnalystServiceImpl implements TransportationAnalystService {
	
	
	public TSAnalystResult pathAnalyst(Point startPoint, Point endPoint, Point[] passPoints) {
		TSAnalystResult result = null;
		if(TransportationAnalystManager.isAnalystInitSuccess()){
			TransportationAnalystParameter parameter = buildParameter(startPoint, endPoint, passPoints);
			TransportationAnalyst analyst = TransportationAnalystManager.getAnalyst();
			TransportationAnalystResult analystResult = analyst.findPath(parameter, false);
			result = buildResult(analystResult);
		}
		return result;
	}
	
	/**
	 * 
	 * <p>Title ：buildParameter</p>
	 * Description：			构建路径分析参数
	 * @param startPoint
	 * @param endPoint
	 * @param passPoints
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2014-12-17 上午09:57:33
	 */
	private TransportationAnalystParameter buildParameter(Point startPoint, Point endPoint, Point[] passPoints){
		Point2Ds point2ds = new Point2Ds();
		point2ds.add(convert(startPoint));
		if(null != passPoints){
			for(Point p : passPoints){
				point2ds.add(new Point2D(p.getX(),p.getY()));
			}
		}
		point2ds.add(convert(endPoint));
		TransportationAnalystParameter parameter = new TransportationAnalystParameter();
		parameter.setRoutesReturn(true);
		parameter.setPoints(point2ds);
		return parameter;
	}

	/**
	 * 墨卡托
	 */
	private static final int TYPE_MC = 1;
	/**
	 * 经纬度
	 */
	private static final int TYPE_LL = 2;
	
	
	private Point2D convert(Point point ){
		Point2D point2D = new Point2D();
		com.supermap.entity.Point pp = new com.supermap.entity.Point(point.getX(),point.getY());
		if(TYPE_MC == TSConstants.NETWORT_TYPE && BaiduCoordinateConvertImpl.isLLPoint(pp)){
			pp = SuperMapCoordinateConvertImpl.smLL2MC(pp);
		}else if(TYPE_LL == TSConstants.NETWORT_TYPE && !BaiduCoordinateConvertImpl.isLLPoint(pp)){
			pp = SuperMapCoordinateConvertImpl.smMCToLatLon(pp);
		}
		point2D.setX(pp.getLon());
		point2D.setY(pp.getLat());
		System.out.println(point2D.getX()+","+point2D.getY());
		return point2D;
	}
	
	
	private TSAnalystResult buildResult(TransportationAnalystResult analystResult){
		TSAnalystResult result = new TSAnalystResult();
		if(null != analystResult){
			GeoLineM[] geoLines = analystResult.getRoutes();
			List<Point> pathPoints = new ArrayList<Point>();
			if(null != geoLines && geoLines.length > 0){
				for(int i=0;i<geoLines.length;i++){
					GeoLineM geoLineM = geoLines[i];
					PointMs pointMs = geoLineM.getPart(0);
					int pointCount = pointMs.getCount();
					for(int j=0;j<pointCount;j++){
						PointM pointM = pointMs.getItem(j);
//						com.supermap.entity.Point pp = new com.supermap.entity.Point(pointM.getX(),pointM.getY());
//						if(!BaiduCoordinateConvertImpl.isLLPoint(pp)){
//							pp = SuperMapCoordinateConvertImpl.smMCToLatLon(pp);
//						}
						
//						Point point = new Point(pp.getLon(),pp.getLat());
						Point point = new Point(pointM.getX(),pointM.getY());
						pathPoints.add(point);
					}
					
				}
				Point[] p = new Point[pathPoints.size()];
				pathPoints.toArray(p);
				result.setPath(p);
			}
		}
		return result;
	}
	
	
}
