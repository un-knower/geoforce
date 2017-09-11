package com.supermap.egispservice.base.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.supermap.convert.impl.BaiduCoordinateConvertImpl;
import com.supermap.convert.impl.SuperMapCoordinateConvertImpl;
import com.supermap.egispservice.base.constants.StatusConstants;
import com.supermap.egispservice.base.pojo.CoorConvertResult;
import com.supermap.egispservice.base.pojo.Point;

@Service("coorConvertService")
public class CoorConvertServiceImpl implements ICoorConvertService {

	private static SuperMapCoordinateConvertImpl smConvert = new SuperMapCoordinateConvertImpl();
	
	private static BaiduCoordinateConvertImpl bdConvert = new BaiduCoordinateConvertImpl();
	
	@Override
	public CoorConvertResult coorCovert(String coords, String from, String to) {
		CoorConvertResult ccr = null;
		// 解析待转换的坐标
		List<com.supermap.entity.Point> points = parsePoints(coords);
		// 
		if(null != points && points.size() > 0){
			if(StatusConstants.COOR_SMM.equals(from) || StatusConstants.COOR_SMLL.equals(from)){
				sm2sm(points, to);
			}else if(StatusConstants.COOR_BDLL.equals(from) || StatusConstants.COOR_BDM.equals(from)){
				bd2sm(points,to);
			}else if(StatusConstants.COOR_GPS.equals(from)){
				gps2sm(points,to);
			}else{
				throw new NullPointerException("未能识别坐标类型["+from+"]");
			}
			// 结果转换
			ccr = assembleResult(points); 
		}
		return ccr;
	}
	
	/**
	 * 
	 * <p>Title ：assembleResult</p>
	 * Description：转配结果
	 * @param points
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-9-22 下午04:02:31
	 */
	private CoorConvertResult assembleResult(List<com.supermap.entity.Point> points){
		List<Point> rps = new ArrayList<Point>();
		for(com.supermap.entity.Point p : points){
			Point rp = new Point();
			rp.setX(p.getLon());
			rp.setY(p.getLat());
			rps.add(rp);
		}
		CoorConvertResult ccr = new CoorConvertResult();
		ccr.setCoords(rps);
		return ccr;
	}

	/**
	 * 
	 * <p>Title ：gps2sm</p>
	 * Description： gps转换为超图坐标
	 * @param points
	 * @param to
	 * Author：Huasong Huang
	 * CreateTime：2015-9-22 下午03:09:17
	 */
	private void gps2sm(List<com.supermap.entity.Point> points, String to) {
		for (int i = 0; i < points.size(); i++) {
			com.supermap.entity.Point p = points.get(i);
			p = smConvert.gps2LL(p);
			if (StatusConstants.COOR_SMM.equals(to)) {
				p = SuperMapCoordinateConvertImpl.smLL2MC(p);
			}
			points.set(i, p);
		}
	}

	/**
	 * 
	 * <p>Title ：bd2sm</p>
	 * Description： 百度坐标转超图
	 * @param points
	 * @param to
	 * Author：Huasong Huang
	 * CreateTime：2015-9-22 下午03:09:03
	 */
	private void bd2sm(List<com.supermap.entity.Point> points, String to) {
		for (int i = 0; i < points.size(); i++) {
			com.supermap.entity.Point p = points.get(i);
			p = bdConvert.convertV2(p);
			if (StatusConstants.COOR_SMM.equals(to)) {
				p = SuperMapCoordinateConvertImpl.smLL2MC(p);
			}
			points.set(i, p);
		}
	}

	/**
	 * 
	 * <p>Title ：sm2sm</p>
	 * Description：	超图坐标转超图坐标
	 * @param points
	 * @param to	目标类型
	 * Author：Huasong Huang
	 * CreateTime：2015-9-22 下午02:49:02
	 */
	private void sm2sm(List<com.supermap.entity.Point> points, String to) {
		if (StatusConstants.COOR_SMLL.equals(to)) {
			for (int i = 0; i < points.size(); i++) {
				com.supermap.entity.Point p = points.get(i);
				// 如果坐标是摩卡托才进行解析
				if(!BaiduCoordinateConvertImpl.isLLPoint(p)){
					p = SuperMapCoordinateConvertImpl.smMCToLatLon(p);
				}
				points.set(i, p);
				Point rp = new Point();
			}
		}else{
			for (int i = 0; i < points.size(); i++) {
				com.supermap.entity.Point p = points.get(i);
				// 如果坐标是经纬度才进行解析
				if(BaiduCoordinateConvertImpl.isLLPoint(p)){
					p = SuperMapCoordinateConvertImpl.smLL2MC(p);
				}
				points.set(i, p);
			}
		}
	}	
	
	
	/**
	 * 
	 * <p>Title ：parsePoints</p>
	 * Description：	解析待转换的坐标
	 * @param coords
	 * @return
	 * Author：Huasong Huang
	 * CreateTime：2015-9-22 下午02:19:08
	 */
	private List<com.supermap.entity.Point> parsePoints(String coords) {
		List<com.supermap.entity.Point> points = null;
		try {
			points = new ArrayList<com.supermap.entity.Point>();
			if (StringUtils.isEmpty(coords)) {
				throw new NullPointerException("待转换坐标不能为空");
			}
			String[] coordArray = coords.split(";");

			for (String coord : coordArray) {
				if (!StringUtils.isEmpty(coord) && coord.indexOf(",") > 0) {
					int index = coord.indexOf(",");
					double lon = Double.parseDouble(coord.substring(0, index));
					double lat = Double.parseDouble(coord.substring(index + 1, coord.length()));
					com.supermap.entity.Point p = new com.supermap.entity.Point();
					p.setLon(lon);
					p.setLat(lat);
					points.add(p);
				}
			}
			return points;
		} catch (Exception e) {
			throw new NullPointerException(e.getMessage());
		}
	}
}
