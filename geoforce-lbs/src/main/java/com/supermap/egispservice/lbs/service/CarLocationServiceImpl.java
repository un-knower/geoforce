package com.supermap.egispservice.lbs.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispservice.lbs.constants.Config;
import com.supermap.egispservice.lbs.dao.CarGpsDao;
import com.supermap.egispservice.lbs.dao.DataWordbookDao;
import com.supermap.egispservice.lbs.entity.DataWordbook;
import com.supermap.egispservice.lbs.pojo.CarDept;
import com.supermap.egispservice.lbs.pojo.CarGps;
import com.supermap.egispservice.lbs.util.DateUtil;
import com.supermap.egispservice.lbs.util.DirectionUtil;

@Transactional
@Service("carLocationService")
public class CarLocationServiceImpl extends BaseService implements CarLocationService {

	@Autowired
	private CarGpsDao carGpsDao;
	@Autowired
	private CarService carService;
	@Autowired
	private DataWordbookDao dataWordbookDao;
	
	@Autowired
	Config config;
	
	@SuppressWarnings("unchecked")
	public List<CarGps> carLocation(HashMap<String, Object> hm)
			throws Exception {
		if(hm == null)
			return null;
		List<String> carIds = (List<String>)hm.get("carIds");
		if(carIds == null || carIds.isEmpty())
			return null;
		try {
			List<CarDept> cars = carService.queryCar(carIds);
			if(cars == null || cars.isEmpty())
				return null;
			List<CarGps> carGps = carGpsDao.queryCarCurrentGps(hm);
			
			return getCarGps(cars,carGps);
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * 
	* 方法名: getCarGps
	* 描述: 封装接口返回值 CarGps
	* @param cars
	* @param carGps
	* @return
	 */
	private List<CarGps> getCarGps(List<CarDept> cars,List<CarGps> carGps){
		List<CarGps> list = new ArrayList<CarGps>();
		for(CarDept car:cars){
			String carId = car.getId();
			CarGps gps = getGps(carGps,carId);
			if(gps == null){
				gps = new CarGps();		
			}
			gps.setId(carId);
			gps.setCarId(carId);
			gps.setLicense(car.getLicense());
			gps.setCarBrand(car.getBrand());
			//查询车辆类型
			String carTypeId = car.getTypeId();
			if(StringUtils.isNotBlank(carTypeId)){
				DataWordbook cartype = dataWordbookDao.findByCode(carTypeId);
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
			list.add(gps);
		}
		return list;
	}
	/**
	 * 
	* 方法名: getGps
	* 描述:根据carId获取GPs
	* @param carGps
	* @param carId
	* @return
	 */
	private CarGps getGps(List<CarGps> carGps,String carId){
		if(carGps == null || carGps.isEmpty())
			return null;
		CarGps ret = null;
		for(int i=0;i<carGps.size();i++){
			CarGps tmp = carGps.get(i);
			if(carId.equals(tmp.getCarId())){
				ret = carGps.remove(i);
				break;
			}
		}
		return ret;
	}
	@Override
	public String sendGetGpsMsg(String name, String msg) {
		return null;//SendMsgHttpClient.sendGetGPsMsg(name, msg);
	}
}
