package com.supermap.egispservice.pathplan.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermap.egispservice.base.entity.GroupType;
import com.supermap.egispservice.base.entity.RouteTaskEntity;
import com.supermap.egispservice.base.entity.WeightNameType;
import com.supermap.egispservice.pathplan.pojo.RouteTaskConstants;
import com.supermap.egispservice.pathplan.util.ScheduleJob;

@Transactional
@Service
public class PathPlanJobServiceImpl implements IPathPlanJobService {
	private static Map<String, ScheduleJob> jobMap = new HashMap<String, ScheduleJob>();
	private final Logger logger = Logger.getLogger(PathPlanJobServiceImpl.class);
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.supermap.egispservice.pathplan.job.PathPlanJobService#addJob(com.supermap.egispservice.pathplan.entity.RouteTaskEntity)
	 */
	@Override
	public boolean addJob(RouteTaskEntity routeTaskEntity,GroupType groupType,WeightNameType weightNameType,Class clasz ) throws Exception {
		// 非刚创建，非结束的任务才能执行
		if (routeTaskEntity.getTaskStatusId() == RouteTaskConstants.TASK_STATUS_TIMING_SETTING || routeTaskEntity.getTaskStatusId() == RouteTaskConstants.TASK_STATUS_RUNNING) {
			ScheduleJob job = new ScheduleJob();
			job.setJobId(routeTaskEntity.getId());
			job.setJobName(routeTaskEntity.getId());
			job.setJobGroup("pathplan");
			job.setJobStatus(RouteTaskConstants.JOB_STATUS_ENABLE);
//			Date planTime = routeTaskEntity.getPlanTime();
//			DateTime planDateTime = new DateTime(planTime);
//			if (planDateTime.isBeforeNow()) {
//				planTime = DateTime.now().plusSeconds(10).toDate();
//			}
			Date planTime = DateTime.now().plusSeconds(10).toDate();
			job.setCronExpression(getCron(planTime));
			job.setDesc("pathplan job " + routeTaskEntity.getId());
			job.setGroupType(groupType);
			job.setWeightNameType(weightNameType);
			logger.info(job.toString());
//			jobMap.put(job.getJobGroup() + "_" + job.getJobName(), job);
			activeJob(job,clasz);
			return true;
		}
		return false;

	}
	
	@Override
	public void deleteJob(String jobId) throws Exception{
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(jobId, "pathplan");
		scheduler.deleteJob(jobKey);
	}
	/**
	 * 激活线路规划任务
	 * 
	 * @throws Exception
	 */
	public void activeJob(ScheduleJob job,Class classz) throws Exception {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());

		// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

		// 不存在，创建一个
		if (null == trigger) {
			JobDetail jobDetail = JobBuilder.newJob(classz).withIdentity(job.getJobName(), job.getJobGroup()).build();
			jobDetail.getJobDataMap().put("scheduleJob", job);

			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

			// 按新的cronExpression表达式构建一个新的trigger
			trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();

			scheduler.scheduleJob(jobDetail, trigger);
		} else {
			// Trigger已存在，那么更新相应的定时设置
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		}
	}

	/**
	 * 激活线路规划任务
	 * 
	 * @throws Exception
	 */
	public void activeJobs(List<ScheduleJob> jobList,Class classz) throws Exception {
		for (ScheduleJob job : jobList) {
			activeJob(job,classz);
		}
	}

	/***
	 * 
	 * @param date
	 * @param dateFormat
	 *            : e.g:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String formatDateByPattern(Date date, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String formatTimeStr = null;
		if (date != null) {
			formatTimeStr = sdf.format(date);
		}
		return formatTimeStr;
	}

	/***
	 * convert Date to cron ,eg. "0 06 10 15 1 ? 2014"
	 * 
	 * @param date
	 *            : 时间点
	 * @return
	 */
	public static String getCron(java.util.Date date) {
		String dateFormat = "ss mm HH dd MM ? yyyy";
		return formatDateByPattern(date, dateFormat);
	}

}
