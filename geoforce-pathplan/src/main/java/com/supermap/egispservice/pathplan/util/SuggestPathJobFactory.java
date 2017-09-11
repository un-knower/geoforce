package com.supermap.egispservice.pathplan.util;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.data.Point2D;
import com.supermap.egispservice.base.entity.Point;
import com.supermap.egispservice.base.entity.RouteTaskEntity;
import com.supermap.egispservice.base.service.ILogisticsOrderService;
import com.supermap.egispservice.pathplan.pojo.RouteTaskConstants;
import com.supermap.egispservice.pathplan.service.IDataGenerateService;
import com.supermap.egispservice.pathplan.service.IRouteTaskService;

@DisallowConcurrentExecution
public class SuggestPathJobFactory implements Job {
	private final Logger LOGGER = Logger.getLogger(QuartzJobFactory.class);
	
	@Autowired
	private IDataGenerateService dataGenerateService;
	@Autowired
	private IRouteTaskService routeTaskService;
	@Autowired
	private ILogisticsOrderService logisticsOrderService;
	
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOGGER.info("## start to run the job of suggest path plan ....");
		
		ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get("scheduleJob");	
		// 更改任务状态：7：正在运行
		RouteTaskEntity taskEntity = routeTaskService.findById(scheduleJob.getJobId());
		taskEntity.setTaskStatusId(RouteTaskConstants.TASK_STATUS_RUNNING);
		routeTaskService.save(taskEntity);
		
		Map<String, Object> m = null;
		long jobStartTime = System.currentTimeMillis();
		int pathCount = 0;
		try {
			// 从文件中获取路径分析的订单及网点信息
			m = routeTaskService.getNetsAndOrdersObj(scheduleJob.getJobId());
			List<Point2D> netPoints = (List<Point2D>) m.get(RouteTaskConstants.PARAM_FIELD_NET_COORD);
			List<Point2D> orderPoints = (List<Point2D>) m.get(RouteTaskConstants.PARAM_FIELD_ORDER_COORDS);
			List<String> orderIds = (List<String>) m.get(RouteTaskConstants.PARAM_FIELD_ORDER_IDS);
			List<String> timeRanges = (List<String>) m.get(RouteTaskConstants.PARAM_FIELD_ORDER_TIME_RANGE);
			int carLoad = (Integer) m.get(RouteTaskConstants.PARAM_FIELD_ORDER_CAR_LOAD);
			int fixedMin = (Integer)m.get(RouteTaskConstants.PARAM_FIELD_ORDER_FIXED_MIN);
			long start = (Long) m.get(RouteTaskConstants.PARAM_FIELD_ORDER_BATCH_TIME_START);
			long end =(Long) m.get(RouteTaskConstants.PARAM_FIELD_ORDER_BATCH_TIME_END);
			pathCount = dataGenerateService.generateVRPPathWithTimeFixed(netPoints, orderPoints, orderIds, timeRanges, carLoad,
					fixedMin, scheduleJob.getJobName(), start, end);
			// 更新订单状态为已规划
//			this.logisticsOrderService.updateOrderStatus(orderIds);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}finally{
			long jobEndTime = System.currentTimeMillis();
			taskEntity.setConsumeTime(Integer.valueOf(((jobEndTime - jobStartTime) / 1000) + ""));
			taskEntity.setResultPath(scheduleJob.getJobId() + File.separator + "path.json");
			taskEntity.setTaskStatusId(RouteTaskConstants.TASK_STATUS_FINISHED);
			taskEntity.setPathCount(pathCount);
			routeTaskService.save(taskEntity);
		}
		
		
		
	}

}
