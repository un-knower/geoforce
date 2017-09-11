package com.supermap.egispservice.pathplan.util;

import com.supermap.egispservice.base.entity.GroupType;
import com.supermap.egispservice.base.entity.WeightNameType;

/**
 * 
 * @description 计划任务信息
 * @author CaoBin mailto:caobin@supermap.com
 * @company SuperMap Software Co., Ltd.
 * @createDate 2014-10-20
 * @version 1.0
 */
public class ScheduleJob {
	/** 任务id */
	private String jobId;

	/** 任务名称 */
	private String jobName;

	/** 任务分组 */
	private String jobGroup;

	/** 任务状态 0禁用 1启用 2删除 */
	private String jobStatus;

	/** 任务运行时间表达式 */
	private String cronExpression;

	/** 任务描述 */
	private String desc;
	/**
	 * 常规模式，还是放射模式
	 */
	private GroupType groupType;
	/**
	 * 权重类型，距离还是时间
	 */
	private WeightNameType weightNameType;

	public ScheduleJob() {
		super();
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public GroupType getGroupType() {
		return groupType;
	}

	public void setGroupType(GroupType groupType) {
		this.groupType = groupType;
	}

	public WeightNameType getWeightNameType() {
		return weightNameType;
	}

	public void setWeightNameType(WeightNameType weightNameType) {
		this.weightNameType = weightNameType;
	}

	@Override
	public String toString() {
		return "ScheduleJob [jobId=" + jobId + ", jobName=" + jobName + ", jobGroup=" + jobGroup + ", jobStatus=" + jobStatus + ", cronExpression="
				+ cronExpression + ", desc=" + desc + ", groupType=" + groupType + ", weightNameType=" + weightNameType + "]";
	}

}
