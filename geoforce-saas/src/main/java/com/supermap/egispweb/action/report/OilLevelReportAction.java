package com.supermap.egispweb.action.report;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.google.gson.reflect.TypeToken;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispweb.action.base.BaseAction;
import com.supermap.egispweb.consumer.car.CarConsumer;

import com.supermap.egispweb.util.DateUtil;
import com.supermap.lbsp.provider.bean.CarDept;
import com.supermap.lbsp.provider.bean.GpsHistoryReport;
import com.supermap.lbsp.provider.bean.OilLevelBean;
import com.supermap.lbsp.provider.bean.OilLevelInfoBean;
import com.supermap.lbsp.provider.bean.carlocation.CarHistoryGps;
import com.supermap.lbsp.provider.common.Page;
import com.supermap.lbsp.provider.hibernate.gps.CarGps;
import com.supermap.lbsp.provider.hibernate.lbsp.Car;


//@Controller
public class OilLevelReportAction extends BaseAction {
	private static final long serialVersionUID = 36L;
	static Logger logger = Logger.getLogger(OilLevelReportAction.class.getName());
	/**
	 * 车辆油耗统计初始化
	 * @return
	 */
	@RequestMapping(value="/com/supermap/toOilLevelReportList")
	public String init(){
		return "report/OilLevelReport";
	}
	/**
	 * 油位数据
	* @Title: list
	* @return
	* String
	* @throws
	 */
	@RequestMapping(value="/com/supermap/OilLevelReportList")
	@ResponseBody
	public OilLevelBean list(HttpServletRequest request,HttpSession session){
		/*UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}*/
		String license = request.getParameter("license");
		String startDate = request.getParameter("startTime");
		String endDate =request.getParameter("endTime");
		String oilType=request.getParameter("oilType");//类型 1 油位-里程曲线 2 油位-时间曲线
		if(StringUtils.isNotBlank(startDate)||StringUtils.isNotBlank(endDate)){
			System.out.println("startDate"+startDate+"endDate"+endDate);
		}
		Car car=null;
		String carId=null;
		try {
			car=carConsumer.getCarByLicense(license);
			if(car == null){//每次只能查询一辆车的油位数据
				return null;
			}
			carId = car.getId();
			Date sDate =DateUtil.formatStringByDate(startDate, "yyyy-MM-dd HH:mm:ss");
			Date eDate = DateUtil.formatStringByDate(endDate, "yyyy-MM-dd HH:mm:ss");
			 long diffTime=DateUtil.diffDates(eDate, sDate);
			if(diffTime < 0 || diffTime > 24*60*60*1000){//开始时间和结束时间超过24小时
				return null;
			}
			List<GpsHistoryReport> historyGps=carHistoryConsumer.getCarHistoryGpsList(carId, sDate, eDate);
			List<OilLevelInfoBean> list = new ArrayList<OilLevelInfoBean>();
			if(historyGps != null && historyGps.size()>0){
				for(GpsHistoryReport gps:historyGps){
					Double oil = gps.getOil();
					Double mile = gps.getMile();
					String gpsTime =gps.getGpsTime();
					if(oil == null){
						oil = (double)0;	
					}
					if(oil.doubleValue() > 0.5){
						OilLevelInfoBean oilLevel = new OilLevelInfoBean();
						oilLevel.setCarId(carId);
						oilLevel.setLicense(license);
						oilLevel.setGpsTime(gpsTime);
						if(gpsTime != null)
						oilLevel.setGpsTime(gpsTime);
						oilLevel.setMile(mile);
						oilLevel.setOil(oil);
						list.add(oilLevel);
					}
					
				}
			}
			if(list == null || list.size() == 0){
				return null;
			}
			OilLevelBean bean = new OilLevelBean();
			bean.setName(license);
			bean.setList(list);
			int len = list.size();
			List<String> xDatas = new ArrayList<String>();
			List<Double> yDatas = new ArrayList<Double>();
			double firstMile = list.get(0).getMile();
			for(int i=0;i<len;i++){
				OilLevelInfoBean oilLevel = list.get(i);
				Double mile = oilLevel.getMile();
				Double oil = oilLevel.getOil();
				String gpsTime = oilLevel.getGpsTime();
				//油位大于0.5为有效数据
				if(oil.doubleValue() > 0.5){
					if(oilType.equals("1")){//油位-里程曲线
						xDatas.add(String.valueOf(doubleToFiex((mile), 3)));
						yDatas.add(oil);
					}else{//油位-时间曲线
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date dateGpsTime = sdf.parse(gpsTime);
						xDatas.add(DateUtil.format(dateGpsTime, "HH:mm:ss"));
						yDatas.add(oil);
					}
				}
			}
			if(xDatas.size() > 0){				
				bean.setxData((String[])xDatas.toArray(new String[xDatas.size()]));
			}
			if(yDatas.size() > 0){
				bean.setyData((Double[])yDatas.toArray(new Double[yDatas.size()]));
			}
			return bean;
			
		} catch (Exception e) {
			
		}
		return null;
		
	}
	public double doubleToFiex(double db,int num){
		double ret = 0;
		try {
			String result = String.format("%."+num+"f", db);
			ret = Double.valueOf(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	

}
