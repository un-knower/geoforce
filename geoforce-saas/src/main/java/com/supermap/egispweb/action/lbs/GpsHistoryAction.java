package com.supermap.egispweb.action.lbs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.supermap.egispservice.base.entity.UserEntity;
import com.supermap.egispservice.lbs.entity.Car;
import com.supermap.egispservice.lbs.pojo.GpsHistoryReport;
import com.supermap.egispservice.lbs.service.CarHistoryService;
import com.supermap.egispservice.lbs.service.CarService;
import com.supermap.egispservice.lbs.util.Pagination;
import com.supermap.egispweb.pojo.export.GpsHistory;
import com.supermap.egispweb.util.DateUtil;

/**
 * 历史轨迹action
* @ClassName: TrackHistoryAction
* @author wyj
* @date 2013-8-28 下午02:15:08
 */
@Controller
public class GpsHistoryAction extends BaseAction {
	static Logger logger = Logger.getLogger(GpsHistoryAction.class.getName());
	
	@Autowired
	CarService carService;
	
	@Autowired
	CarHistoryService carHistoryService;
	
	/**
	 * 历史轨迹初始化
	* @Title: init
	* @return
	* String
	* @throws
	 */
	@RequestMapping(value="/com/supermap/toHistoryList")
	public String init(HttpServletRequest request){
		String statTime = DateUtil.format(new Date(), "yyyy-MM-dd");
		statTime = statTime +" 00:00:00";
		String endTime = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
		request.setAttribute("statTime", statTime);
		request.setAttribute("endTime", endTime);
		return "report/gpsHistoryReport";
	}
	
	/**
	 * 历史轨迹列表
	 */
	@RequestMapping(value="/com/supermap/historyList")
	@ResponseBody
	public Map<String,Object> list(HttpServletRequest request,HttpSession session){
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		String license = request.getParameter("license");
		String startDate = request.getParameter("starttime");
		String endDate = request.getParameter("endtime");
		Pagination page = getPage(request);
		if(StringUtils.isBlank(license)){
			return builderResult(null,"车牌号不能为空");
		}
		if(StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)){
			
			return builderResult(null,"时间不能不能为空");
		}
		try {
			//输入的车牌号是否存在多个
			Car car = this.carService.getCarByLicense(license,userEntity.getEid().getId());
			if(car == null){//每次只能查询一辆车的油位数据
				return builderResult(null,"车牌号不存在");
			}
			String carId = car.getId();//
			Date sDate = DateUtil.formatStringByDate(startDate, "yyyy-MM-dd HH:mm:ss");
			Date eDate = DateUtil.formatStringByDate(endDate, "yyyy-MM-dd HH:mm:ss");
			long diffTime = DateUtil.diffDates(eDate, sDate);
			if(diffTime < 0 || diffTime > 24*60*60*1000){//开始时间和结束时间超过24小时
				return builderResult(null,"开始时间和结束时间不能超过24小时");
			}
			page = this.carHistoryService.getCarHistoryGpsList(carId, sDate, eDate, page);
			return builderResult(page,null);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return builderResult(page,e.getMessage());
		}
	}
	/**
	 * 单车导出excel
	* @Title: exportByCar
	* @return
	* String
	* @throws
	 */
	@RequestMapping(value="/com/supermap/historyExport")
	public HashMap<String, Object> export(HttpServletRequest request
			,HttpSession session
			,HttpServletResponse response){
		String license = request.getParameter("license");
		String startDate = request.getParameter("startTime");
		String endDate = request.getParameter("endTime");
		
		UserEntity userEntity = (UserEntity)session.getAttribute("user");
		if(userEntity == null){
			return null;
		}
		
		if(StringUtils.isBlank(license)){
			
			return null;
		}
		if(StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)){
			
			return null;
		}
		
			//输入的车牌号是否存在多个
			Car car = this.carService.getCarByLicense(license,userEntity.getEid().getId());
			if(car == null){//每次只能查询一辆车的油位数据
				return null;
			}
			String carId = car.getId();//
			Date sDate = DateUtil.formatStringByDate(startDate, "yyyy-MM-dd HH:mm:ss");
			Date eDate = DateUtil.formatStringByDate(endDate, "yyyy-MM-dd HH:mm:ss");
			long diffTime = DateUtil.diffDates(eDate, sDate);
			if(diffTime < 0 || diffTime > 24*60*60*1000){//开始时间和结束时间超过24小时
				return null;
			}
			List<GpsHistoryReport> list = this.carHistoryService.getCarHistoryGpsList(carId, sDate, eDate);
			 List<GpsHistory> GpsHistorylist = getExportGpsHistoryList( list);
		String filename = "历史轨迹"+System.currentTimeMillis()+".xls";
		exportExcel(GpsHistorylist, filename,"exportTrack", startDate ,response, request);
				
		return null;
	}
	
	private List<GpsHistory> getExportGpsHistoryList(List<GpsHistoryReport> list){
		 List<GpsHistory> gpsHistoryList = new ArrayList<GpsHistory>();
		for (int i = 0; i < list.size(); i++) {
			GpsHistory gpsHistory = new GpsHistory();
			GpsHistoryReport gpsHistoryReport = list.get(i);
			gpsHistory.setLicense(gpsHistoryReport.getLicense());
			gpsHistory.setDirection(gpsHistoryReport.getDirection());
			gpsHistory.setGpsTime(gpsHistoryReport.getGpsTime());
			gpsHistory.setLatitude(gpsHistoryReport.getLatitude());
			gpsHistory.setLongitude(gpsHistoryReport.getLongitude());
			gpsHistory.setMile(gpsHistoryReport.getMile());
			gpsHistory.setSpeed(gpsHistoryReport.getSpeed());
			gpsHistoryList.add(gpsHistory);
			
		}
		return gpsHistoryList;
	}

	
}
