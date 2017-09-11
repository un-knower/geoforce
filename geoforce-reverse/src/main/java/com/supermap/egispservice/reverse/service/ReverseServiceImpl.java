package com.supermap.egispservice.reverse.service;

import com.supermap.cloud.base.util.sm.coordinate.CoordinateTranslator;
import com.supermap.convert.impl.BaiduCoordinateConvertImpl;
import com.supermap.convert.impl.SuperMapCoordinateConvertImpl;
import com.supermap.egispservice.base.entity.AddressInfo;
import com.supermap.egispservice.base.entity.PointParam;
import com.supermap.egispservice.reverse.utils.Config;
import com.supermap.egispservice.reverse.utils.ConvertCoordTool;
import com.supermap.egispservice.reverse.utils.HttpTool;
import com.supermap.entity.Point;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @author CaoBin mailto:caobin@supermap.com
 * @version 1.0
 * @description
 * @company SuperMap Software Co., Ltd.
 * @createDate 2015/9/6
 */
@Service
public class ReverseServiceImpl implements IReverseService {

    private final Logger log = Logger.getLogger(ReverseServiceImpl.class);

    @Autowired
    private Config config;

    @Override
    public List<AddressInfo> pointToAddress(List<PointParam> points,String type) throws Exception {
        if (points.size()>0) {
            List<AddressInfo> addressInfos=new ArrayList<AddressInfo>();
            for (int i = 0; i < points.size(); i++) {
                PointParam point = points.get(i);
                AddressInfo addressInfo=getAddress(point,type);
                if (addressInfo!=null) {
                    addressInfo.setCode(point.getCode());
                    addressInfo.setPoint(point.getPoint());
                    addressInfos.add(addressInfo);
                }
            }
            return addressInfos;
        }
        return null;
    }

    /**
     * 调用百度反向解析接口获取地址信息
     * @param point
     * @return
     * @throws Exception
     */
    public AddressInfo getAddress(PointParam point,String type) throws Exception {
        if (point != null) {
            Point p=convertCoord(point.getPoint().getX(),point.getPoint().getY(),type);
            String param = "ak=" + config.getBaiduAK() + "&callback=renderReverse&location=" + p.getLat() + "," + p.getLon() + "&output=json&pois=0";
            try {
                String content = HttpTool.getContent(config.getBaiduGeocoderUrl(), param);
                JSONObject obj= JSONObject.fromObject(content);
                JSONObject result=obj.getJSONObject("result");
                JSONObject addressComponent=result.getJSONObject("addressComponent");

                AddressInfo addressInfo=new AddressInfo();
                addressInfo.setAddress((String) result.get("formatted_address"));
                addressInfo.setCountry((String) addressComponent.get("country"));
                addressInfo.setProvince((String) addressComponent.get("province"));
                addressInfo.setCity((String) addressComponent.get("city"));
                addressInfo.setDistrict((String) addressComponent.get("district"));
                addressInfo.setStreet((String) addressComponent.get("street"));

                return addressInfo;
            } catch (IOException e) {
                log.error("调用百度反向接口获取地址失败！", e);
            }
        }

        return null;
    }
    /**
     * 根据type类型，将坐标转为百度经纬度
     * @param x
     * @param y
     * @return
     */
    public Point convertCoord(double x, double y,String type) {
        // 墨卡托转加偏经纬度
        CoordinateTranslator.Point point2d = new CoordinateTranslator.Point();
        point2d.setX(x);
        point2d.setY(y);
        // 如果传入类型为空，则判断坐标是经纬度还是摩卡托，并进行type赋值
        if(StringUtils.isEmpty(type)){
        	if(isLLPoint(point2d.getX(), point2d.getY())){
        		type = "SMLL";
        	}else{
        		type = "SMC";
        	}
        }
        com.utils.Point realGPSPoint = null;
        // 如果type为摩卡托，将其转为经纬度
        if("SMC".equalsIgnoreCase(type) || "SMLL".equalsIgnoreCase(type)){
        	if(!isLLPoint(point2d.getX(), point2d.getY())){
        		CoordinateTranslator.mercatorToLngLat(point2d);
        	}
        	// 加偏经纬度转真实经纬度
        	com.utils.Point adjustedGPSPoint = new com.utils.Point(point2d.getX(), point2d.getY());
        	realGPSPoint = ConvertCoordTool.adjustedGPS2RealGPS(adjustedGPSPoint);
        }else if("GPS".equalsIgnoreCase(type)){
        	realGPSPoint = new com.utils.Point();
        	realGPSPoint.setLon(point2d.getX());
        	realGPSPoint.setLat(point2d.getY());
        }


        // 真实经纬度转百度经纬度
        Point realGPSPoint2 = new Point(realGPSPoint.getLon(), realGPSPoint.getLat());
        Point baiduGPSPoint = null;
        try {
            baiduGPSPoint = ConvertCoordTool.realGPSPoint2BaiduGPS(realGPSPoint2);
        } catch (Exception e) {
            log.error("转换realGPSPoint 到 baiduGPS失败",e);
        }
        return baiduGPSPoint;
    }
    
    /**
     * 
     * <p>Title ：isLLPoint</p>
     * Description：判断是否为经纬度坐标，是则返回true，否则返回false
     * @param lon
     * @param lat
     * @return
     * Author：Huasong Huang
     * CreateTime：2015-11-2 上午10:25:55
     */
	private static boolean isLLPoint(double lon, double lat) {
		return !(lon > 180 || lon < -180 || lat > 90 || lat < -90);

	}
    
}
