package com.supermap.egispservice.lbs.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supermap.egispservice.base.entity.InfoDeptEntity;
import com.supermap.egispservice.base.service.InfoDeptService;
import com.supermap.egispservice.lbs.constants.Config;
import com.supermap.egispservice.lbs.dao.CarGpsDao;
import com.supermap.egispservice.lbs.entity.Car;
import com.supermap.egispservice.lbs.pojo.CarDept;
import com.supermap.egispservice.lbs.pojo.CarGps;
import com.supermap.egispservice.lbs.pojo.CarHistoryGps;
import com.supermap.egispservice.lbs.pojo.GpsHistoryReport;
import com.supermap.egispservice.lbs.pojo.MileReport;
import com.supermap.egispservice.lbs.util.DateUtil;
import com.supermap.egispservice.lbs.util.DirectionUtil;
import com.supermap.egispservice.lbs.util.Pagination;



@Service("carHistoryService")
public class CarHistoryServiceImpl extends BaseService implements CarHistoryService{
	@Autowired
	private CarService carService;
	@Autowired
	private InfoDeptService infoDeptService;
	@Autowired
	private CarGpsDao carGpsDao;
	
	@Autowired
	Config config;

	public CarHistoryGps carHistory(HashMap<String, Object> hm, Pagination page)
			throws Exception {
		
		if(hm == null)
			return null;
		String carId = (String)hm.get("carId");
		Date startDate = (Date)hm.get("startDate");
		Date endDate = (Date)hm.get("endDate");
		//必填项
		if(StringUtils.isBlank(carId) || startDate == null || endDate == null){
			return null;
		}
		Car car = this.carService.getCar(carId);
		if(car == null)
			return null;
		List<CarGps> list = carGpsDao.queryCarHistoryGps(hm, page);
		if(list == null || list.isEmpty())
			return null;
		CarHistoryGps carHistoryGps = loadCarHistoryGps(car,list);
		return carHistoryGps;
	}
		/**
	 * 
	* 方法名: loadCarHistoryGps
	* 描述: 封装历史轨迹数据
	* @param car
	* @param list
	* @return
	 */
	private CarHistoryGps loadCarHistoryGps(Car car,List<CarGps> list){
		CarHistoryGps carHistoryGps = new CarHistoryGps();
		carHistoryGps.setCarId(car.getId());
		carHistoryGps.setLicense(car.getLicense());
		
		for(CarGps gps:list){
			gps.setId(car.getId());
			gps.setCarId(car.getId());
			gps.setLicense(car.getLicense());
			gps.setCarBrand(car.getBrand());
			//查询车辆类型
//			String carTypeId = car.getType();
//			if(StringUtils.isNotBlank(carTypeId)){
//				DataWordbook cartype = dataWordbookDao.getDataWordbook(carTypeId);
//				if(cartype != null){
//					gps.setCarType(carTypeId);
//					gps.setCarTypeName(cartype.getName());
//				}
//			}
			Double direction = gps.getDirection();
			if(direction != null)
				gps.setDirectionStr(DirectionUtil.getDirection(direction));
			Date gpsDate = gps.getGpsDate();
			if(gpsDate != null)
				gps.setGpsTime(DateUtil.format(gpsDate, config.DATE_FORMAT));
			String alarm = gps.getAlarm();
			if(StringUtils.isNotBlank(alarm)){
				String[] tmp = alarm.split("#");//多个报警以‘#’分割
				List<String> alarmStrs = new ArrayList<String>();
				for(String str:tmp){
					alarmStrs.add(this.getALARM_MAP().get(str.trim()));
				}
				gps.setAlarmStr(StringUtils.join(alarmStrs, "；"));
			}
			gps.setStatus((short)1);
		}
		carHistoryGps.setList(list);
		
		return carHistoryGps;
	}
	@Override
	public Pagination getMileReportList(Pagination page, HashMap hm) {
		String deptId = (String) hm.get("deptId");
		List<CarDept> list = new ArrayList<CarDept>();
		if(deptId != null && !"".equals(deptId)){
			HashMap<String, Object> carHm = new HashMap<String, Object>();
			List<InfoDeptEntity> listDept = this.infoDeptService.getChildDepts(deptId);
			if(listDept != null && listDept.size()>0){
				List<String> deptIds = new ArrayList<String>(); 
				for (int i = 0; i < listDept.size(); i++) {
					InfoDeptEntity  dept = listDept.get(i);
					deptIds.add(dept.getId());
				}
				carHm.put("deptIds", deptIds);
			}
			String license = (String)hm.get("license");
			if(StringUtils.isNotBlank(license)){
				carHm.put("license", license);
			}
			String eid = (String)hm.get("eid");
			if(StringUtils.isNotBlank(eid)){
				carHm.put("eid", eid);
			}
			carHm.put("pageNumber", page.getPageNo());
			carHm.put("pageSize", page.getPageSize());
			
			Map<String,Object> cardeptMap=this.carService.queryCarPage(carHm);
			if(cardeptMap!=null&&cardeptMap.get("rows")!=null){
				list = (List<CarDept>) cardeptMap.get("rows");
				page.setTotalCount(cardeptMap.get("records")==null?0:Integer.parseInt(cardeptMap.get("records").toString()));
			}
		}
		if(list == null || list.size()==0){
			return page;
		}
		String startDate = (String)hm.get("startDate");
		String endDate = (String)hm.get("endDate");
		Date startTime = DateUtil.formatStringToDate(startDate+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
		Date endTime = DateUtil.formatStringToDate(endDate+" 23:59:59", "yyyy-MM-dd HH:mm:ss");
		List<MileReport> mileReportlist = new ArrayList<MileReport>();
		for (int i = 0; i < list.size(); i++) {
			CarDept car = list.get(i);
			String carId = car.getId();
			MileReport bean = new MileReport();
			InfoDeptEntity dept = this.infoDeptService.findDeptById(car.getDeptId());
			bean.setCarId(carId);
			bean.setLicense(car.getLicense());
			bean.setDeptCode(dept.getCode());
			bean.setDeptId(dept.getId());
			bean.setDeptName(dept.getName());
			List<CarGps> gps = carGpsDao.getCarHistoryByTime(carId, startTime, endTime);
			if(gps != null && gps.size() == 2){
				CarGps startGps = gps.get(0);
				CarGps endGps = gps.get(1);
				Double startMile = 0.0;
				if(startGps != null){
					Date startGpsDate = startGps.getGpsDate();
					if(startGpsDate != null)
						bean.setStartGpsTime(DateUtil.formatDateByFormat(startGpsDate, "yyyy-MM-dd HH:mm:ss"));
					startMile = startGps.getMile();
					if(startMile != null){
						startMile = doubleToFiex(startMile, 3);
						bean.setStartMile(startMile);
					}
				}
				Double endMile = 0.0;
				if(endGps != null){
					Date endGpsDate = endGps.getGpsDate();
					if(endGpsDate != null)
						bean.setEndGpsTime(DateUtil.formatDateByFormat(endGpsDate, "yyyy-MM-dd HH:mm:ss"));
					endMile = endGps.getMile();
					if(endMile != null){
						endMile = doubleToFiex(endMile, 3);
						bean.setEndMile(endMile);
					}
				}
				double diffMile = endMile - startMile;
				diffMile = doubleToFiex(diffMile, 3);
				if(diffMile < 0){
					diffMile = 0;
				}
				bean.setDiffMile(diffMile);
			}
			mileReportlist.add(bean);
		}
		if(mileReportlist != null){
			page.setResult(mileReportlist);
		}
		
		return page;
	}
	
	public Pagination getCarHistoryGpsList(String carId, Date startTime, Date endTime, Pagination page){

			try {
				HashMap<String, Object> hm = new HashMap<String, Object>();
                if(StringUtils.isBlank(carId)){
                	return page;
                }
                hm.put("carId", carId);
                if(startTime == null){
                	return page;
                }
                hm.put("startDate", startTime);
                if(endTime == null){
                	return page;
                }
                hm.put("endDate", endTime);
                hm.put("isRuning", false);
				List<CarGps> historyGps = carGpsDao.queryCarHistoryGps(hm,page);
				if(historyGps != null && historyGps.size()>0){
					List<GpsHistoryReport> list = new ArrayList<GpsHistoryReport>();
					for(CarGps gps:historyGps){
						GpsHistoryReport bean = loadTrack(gps);
						list.add(bean);
					}
					page.setResult(list);
				}else{
					return page;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return page;
	}
	
	public List<GpsHistoryReport> getCarHistoryGpsList(String carId, Date startTime, Date endTime) {
		try {
			HashMap<String, Object> hm = new HashMap<String, Object>();
            if(StringUtils.isBlank(carId)){
            	return null;
            }
            hm.put("carId", carId);
            if(startTime == null){
            	return null;
            }
            hm.put("startDate", startTime);
            if(endTime == null){
            	return null;
            }
            hm.put("endDate", endTime);
            hm.put("isRuning", false);
			List<CarGps> historyGps = carGpsDao.queryCarHistoryGps(hm,null);
			if(historyGps == null || historyGps.size()==0){
				
				return null;
			}
			List<GpsHistoryReport> list = new ArrayList<GpsHistoryReport>();
			for(CarGps gps:historyGps){
				GpsHistoryReport bean = loadTrack(gps);
				list.add(bean);
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	
	/**
	 * double截取精度，如精确到小数点后三位则num为3
	* @Title: doubleToFiex
	* @param db
	* @param num
	* @return
	* double
	* @throws
	 */
	private double doubleToFiex(double db,int num){
		double ret = 0;
		try {
			String result = String.format("%."+num+"f", db);
			ret = Double.valueOf(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 对象封装前端javabean
	* @Title: loadTrack
	* @return
	* SpeedInfoBean
	* @throws
	 */
	private GpsHistoryReport loadTrack(CarGps gps) throws Exception{
		GpsHistoryReport bean = new GpsHistoryReport();
		Car car = this.carService.getCar(gps.getCarId());
		bean.setId(gps.getId());
		bean.setLicense(car.getLicense());
		bean.setCarId(gps.getCarId());
		bean.setAddr(gps.getAddr());
		bean.setLatitude(gps.getLat());
		bean.setLongitude(gps.getLng());
		bean.setMile(doubleToFiex(gps.getMile(), 3));
		String date = DateUtil.formatDateByFormat(gps.getGpsDate(), "yyyy-MM-dd HH:mm:ss");
		bean.setGpsTime(date);
		bean.setSpeed(doubleToFiex(gps.getSpeed(), 2));
		bean.setDirection(DirectionUtil.getDirection(gps.getDirection()));
		bean.setImgpath(gps.getPicPath());
		bean.setOil(gps.getOil());
		return bean;
	}

	
}
