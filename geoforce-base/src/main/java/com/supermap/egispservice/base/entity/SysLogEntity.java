package com.supermap.egispservice.base.entity;

// Generated 2014-9-4 11:41:47 by Hibernate Tools 4.0.0

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SysLog generated by hbm2java
 */
@Entity
@Table(name = "SYS_LOG")
public class SysLogEntity extends IdEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String moduleId;//操作模块
	private String operDesc;//具体操作内容
	private Date operTime;//操作时间
	private UserEntity userId;//用户
	private String enterpriseId;//企业id、
	private String departmentId;//部门id
	private String ipaddr;//IP 地址

	public SysLogEntity() {
	}

	public SysLogEntity(String id, String moduleId, String detail, Date operateTime, UserEntity userId, String enterpriseId, String departmentId
			,String ipaddr) {
		this.id = id;
		this.moduleId = moduleId;
		this.operDesc = detail;
		this.operTime = operateTime;
		this.userId = userId;
		this.enterpriseId = enterpriseId;
		this.departmentId = departmentId;
		this.ipaddr=ipaddr;
	}

	@Column(name = "module_id", length = 32)
	public String getModuleId() {
		return this.moduleId;
	}

	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	@Column(name = "oper_desc", length = 128)
	public String getOperDesc() {
		return this.operDesc;
	}

	public void setOperDesc(String detail) {
		this.operDesc = detail;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "oper_time", length = 19)
	public Date getOperTime() {
		return this.operTime;
	}

	public void setOperTime(Date operateTime) {
		this.operTime = operateTime;
	}

	@ManyToOne
	@JoinColumn(name = "user_id", columnDefinition = "varchar(32)", nullable = false)
	public UserEntity getUserId() {
		return this.userId;
	}

	public void setUserId(UserEntity userId) {
		this.userId = userId;
	}

	@Column(name = "enterprise_id", length = 32)
	public String getEnterpriseId() {
		return this.enterpriseId;
	}

	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	@Column(name = "department_id", length = 32)
	public String getDepartmentId() {
		return this.departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	
	@Column(name = "ipaddr", length = 20)
	public String getIpaddr() {
		return ipaddr;
	}

	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}
	
	private String dataDesc; //具体操作内容

	@Column(name = "data_desc", length = 1000)
	public String getDataDesc() {
		return dataDesc;
	}

	public void setDataDesc(String dataDesc) {
		this.dataDesc = dataDesc;
	}
	

}
