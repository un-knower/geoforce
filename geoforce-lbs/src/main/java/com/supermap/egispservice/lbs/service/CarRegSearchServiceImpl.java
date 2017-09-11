package com.supermap.egispservice.lbs.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.egispservice.lbs.constants.Config;
import com.supermap.egispservice.lbs.dao.CarGpsDao;
import com.supermap.egispservice.lbs.dao.DataWordbookDao;
import com.supermap.egispservice.lbs.entity.Car;
import com.supermap.egispservice.lbs.entity.DataWordbook;
import com.supermap.egispservice.lbs.pojo.CarGps;
import com.supermap.egispservice.lbs.util.DateUtil;
import com.supermap.egispservice.lbs.util.DirectionUtil;
import com.supermap.egispservice.lbs.util.RegionUtil;
import com.supermap.egispservice.lbs.util.TranslateXYUtil;


/**
 * 
* ClassName：CarRegSearchService   
* 类描述：   区域查询车辆service
* 操作人：wangshuang   
* 操作时间：2014-9-11 下午02:03:46     
* @version 1.0
 */
@Service("carRegSearchService")
public class CarRegSearchServiceImpl extends BaseService implements CarRegSearchService{
	@Autowired
	private CarService  carService;
	@Autowired
	private CarGpsDao carGpsDao;
	@Autowired
	private DataWordbookDao dataWordbookDao;
	
	@Autowired
	Config config;
	
	@Override
	public List<CarGps> queryCarsByRegion(String lngLats, String mapType,
			String deptCode) throws Exception {
		
		if (StringUtils.isBlank(lngLats)) {
			return null;
		}
		String[] regionArray = lngLats.split(";");
		//存储查询区域的经纬度list
		ArrayList<Double> polygonXA = new ArrayList<Double>();
		ArrayList<Double> polygonYA = new ArrayList<Double>();
		double minLng=0,minLat=0,maxLng=0,maxLat=0;
		Double[] xyTmp;
		try{
			for (int i=0; i<regionArray.length; i++){
				String[] xy = regionArray[i].split(",");
				double x = Double.valueOf(xy[0]);
				double y = Double.valueOf(xy[1]);
				if(mapType.equals("baidu")){
					xyTmp = TranslateXYUtil.bdDecrypt(x, y);
					x = xyTmp[0].doubleValue();
					y = xyTmp[1].doubleValue();
				}
				if(i == 0){
					minLng = x;
					minLat = y;
					maxLng = x;
					maxLat = y;
				}
				if(x <minLng){
					minLng = x;
				}
				if(x > maxLng){
					maxLng = x;
				}
				if(y < minLat){
					minLat = y;
				}
				if(y > maxLat){
					maxLat = y;
				}
				
				polygonXA.add(x);
				polygonYA.add(y);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		List<CarGps> gpsList = null;
		try {
			gpsList = carGpsDao.queryCarCurrentGpsByBound(deptCode, minLng, minLat, maxLng, maxLat);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(gpsList == null || gpsList.isEmpty()){
			return null;
		}
		
		List<CarGps> carGpsList = new ArrayList<CarGps>();
		RegionUtil fence = new RegionUtil();
		for(CarGps gps:gpsList){
			boolean flag = fence.isPointInPolygon(gps.getLng(), gps.getLat(), polygonXA, polygonYA);
			if(flag){
				gps = getCarGps(gps);
				if(gps != null)
					carGpsList.add(gps);
			}
		}
		return carGpsList;
	}
	private CarGps getCarGps(CarGps gps){
		
		String carId = gps.getCarId();
		if(StringUtils.isBlank(carId))
			return null;
		gps.setId(carId);
		Car car = this.carService.getCar(carId);
		if(car == null)
			return null;
		gps.setLicense(car.getLicense());
		gps.setCarBrand(car.getBrand());
		//查询车辆类型
		String carTypeId = car.getType();
		if(StringUtils.isNotBlank(carTypeId)){
			DataWordbook cartype = this.dataWordbookDao.findByCode(carTypeId);
			if(cartype != null){
				gps.setCarType(carTypeId);
				gps.setCarTypeName(cartype.getName());
			}
		}
		Double direction = gps.getDirection();
		if(direction != null)
			gps.setDirectionStr(DirectionUtil.getDirection(direction));
		Date gpsDate = gps.getGpsDate();
		if(gpsDate != null)
			gps.setGpsTime(DateUtil.format(gpsDate, Config.DATE_FORMAT));
		String alarm = gps.getAlarm();
		if(StringUtils.isNotBlank(alarm)){
			String[] tmp = alarm.split("#");//多个报警以‘#’分割
			List<String> alarmStrs = new ArrayList<String>();
			for(String str:tmp){
				alarmStrs.add(this.getALARM_MAP().get(str.trim()));
			}
			gps.setAlarmStr(StringUtils.join(alarmStrs, "；"));
		}
		//在线离线状态
		int status = 3;//1在线 2离线 3未上线（无记录）
		if(gpsDate != null){
			long milTime = DateUtil.diffDates(new Date(), gpsDate)/1000;//单位秒
			if(milTime <= 5 * 60){//默认5分钟离线
				status = 1;
			}else {
				status = 2;
			}
		}
		gps.setStatus(status);
		return gps;
	}
}
