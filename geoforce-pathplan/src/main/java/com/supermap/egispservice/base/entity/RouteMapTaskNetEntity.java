package com.supermap.egispservice.base.entity;

// Generated 2014-9-4 11:41:47 by Hibernate Tools 4.0.0

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * RouteMapTaskCar generated by hbm2java
 */
@Entity
@Table(name = "ROUTE_MAP_TASK_NET", catalog = "egisp_dev")
public class RouteMapTaskNetEntity extends IdEntity implements java.io.Serializable {

	private String taskId;
	private String netId;
	private String netName;

	public RouteMapTaskNetEntity() {
	}

	public RouteMapTaskNetEntity(String id, String taskId, String netId, String netName) {
		super();
		this.id = id;
		this.taskId = taskId;
		this.netId = netId;
		this.netName = netName;
	}

	@Column(name = "task_id", length = 32)
	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Column(name = "net_id", length = 32)
	public String getNetId() {
		return netId;
	}

	public void setNetId(String netId) {
		this.netId = netId;
	}

	@Column(name = "net_name", length = 100)
	public String getNetName() {
		return netName;
	}

	public void setNetName(String netName) {
		this.netName = netName;
	}

}