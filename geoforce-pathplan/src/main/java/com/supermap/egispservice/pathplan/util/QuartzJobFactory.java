package com.supermap.egispservice.pathplan.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispservice.base.entity.PathPlanCar;
import com.supermap.egispservice.base.entity.Point;
import com.supermap.egispservice.base.entity.RouteTaskEntity;
import com.supermap.egispservice.base.entity.WeightNameType;
import com.supermap.egispservice.base.service.ILogisticsOrderService;
import com.supermap.egispservice.pathplan.service.IDataGenerateService;
import com.supermap.egispservice.pathplan.service.IRouteTaskService;

@DisallowConcurrentExecution
public class QuartzJobFactory implements Job {
	private final Logger logger = Logger.getLogger(QuartzJobFactory.class);

	@Autowired
	private IDataGenerateService dataGenerateService;
	@Autowired
	private IRouteTaskService routeTaskService;
	@Autowired
	private ILogisticsOrderService logisticsOrderService;

	@Transactional
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.info("任务成功运行");

		ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");
		// 更改任务状态：7：正在运行
		RouteTaskEntity taskEntity = routeTaskService.findById(scheduleJob.getJobId());
		taskEntity.setTaskStatusId(Byte.valueOf("7"));
		routeTaskService.save(taskEntity);

		// 获取数据
		Map<String, Object> m = null;
		try {
			m = routeTaskService.getNetsAndOrders(scheduleJob.getJobId());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String ordersCoords = (String) m.get("ordersCoords");
		List<Point> orderPoints = new ArrayList<Point>();
		String[] ordersCoordsArray = ordersCoords.split(",");
		for (int i = 0; i < ordersCoordsArray.length; i += 2) {
			Point p = new Point();
			p.setLon(Double.valueOf(ordersCoordsArray[i]));
			p.setLat(Double.valueOf(ordersCoordsArray[i + 1]));
			orderPoints.add(p);
		}
		String netcoord = (String) m.get("netcoord");
		List<Point> netPoints = new ArrayList<Point>();
		String[] netCoordsArray = netcoord.split(",");
		Point netPoint = new Point();
		netPoint.setLon(Double.valueOf(netCoordsArray[0]));
		netPoint.setLat(Double.valueOf(netCoordsArray[1]));
		netPoints.add(netPoint);

		//添加车辆
		List<PathPlanCar> pathPlanCarsList=new ArrayList<PathPlanCar>();
		String pathPlanCars = (String) m.get("pathPlanCars"); 
		if(pathPlanCars.indexOf(";")>0){
			String [] pathPlanCarsArray=pathPlanCars.split(";");
			for (int i = 0; i < pathPlanCarsArray.length; i++) {
				PathPlanCar car= createPathPlanCarByStringProperties(pathPlanCarsArray[i]);
				pathPlanCarsList.add(car);
			}
		}else{
			PathPlanCar car= createPathPlanCarByStringProperties(pathPlanCars);
			pathPlanCarsList.add(car);
		}
		
		long jobStartTime = System.currentTimeMillis();
		try {
			dataGenerateService.generateDataByVRPPath(netPoints, orderPoints, pathPlanCarsList,scheduleJob.getJobName(), true,scheduleJob.getGroupType(),scheduleJob.getWeightNameType());
			long jobEndTime = System.currentTimeMillis();
			taskEntity.setConsumeTime(Integer.valueOf(((jobEndTime - jobStartTime) / 1000) + ""));
			taskEntity.setResultPath(scheduleJob.getJobId() + File.separator + "path.json");
			taskEntity.setTaskStatusId(Byte.valueOf("8"));
			routeTaskService.save(taskEntity);
			//更新订单状态为已规划
			String ordersIds = (String) m.get("ordersIds");
			List<String> idList=new ArrayList<String>();
			if(ordersIds.indexOf(",")>-1){
				String []strArray=ordersIds.split(",");
				for (String idstr : strArray) {
					idList.add(idstr);
				}
			}else{
				idList.add(ordersIds);
			}
			logisticsOrderService.updateOrderStatus(idList);
		} catch (Exception e) {
			logger.error("VRPPath方法路径分析失败", e);
		}
		logger.info("任务名称 = [" + scheduleJob.getJobName() + "],耗时：" + taskEntity.getConsumeTime() + "秒，完成!");

	}
	private PathPlanCar createPathPlanCarByStringProperties(String propertiesString){
		String [] properties =propertiesString.split(",");
		PathPlanCar car=new PathPlanCar();
		car.setCost(Double.valueOf(properties[0].split("=")[1]));
		car.setLoadOrderNumber(Double.valueOf(properties[1].split("=")[1]));
		car.setCarLength(Double.valueOf(properties[2].split("=")[1]));
		car.setCarWidth(Double.valueOf(properties[3].split("=")[1]));
		car.setCarHeight(Double.valueOf(properties[4].split("=")[1]));
		car.setVolume(Double.valueOf(properties[5].split("=")[1]));
		car.setLoadWeight(Double.valueOf(properties[6].split("=")[1]));
		car.setCarOwner(properties[7].split("=")[1]);
		car.setPhone(properties[8].split("=")[1]);
		car.setCarPlate(properties[9].split("=")[1]);
		car.setCarType(properties[10].split("=")[1]);
		car.setCarId(properties[11].split("=")[1]);
		return car;
	}
}
