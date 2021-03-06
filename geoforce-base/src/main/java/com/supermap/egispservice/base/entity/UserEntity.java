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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * EgispRssUser generated by hbm2java
 */
@Entity
@Table(name = "EGISP_RSS_USER")
public class UserEntity extends IdEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String realname;
	private String mobilephone;
	private String email;
	private String telephone;
	private String fax;
	private Character sex = '1';
	private String address;
	private String zipCode;
	private String remark;
	private Byte stratusId;
	private String createUser;
	private Date createTime = new Date();
	private Date updateTime;
	private ComEntity eid;
	private InfoDeptEntity deptId;
	private Byte sourceId = 1;
	private String pid;
	private String isLogined;//记录默认城市升级后是否登录过
	private String qq;
	
	private String tempLogined;

	public UserEntity() {
	}

	public UserEntity(String username, String password, String realname, String mobilephone, String email, String telephone, String fax, Character sex,
			String address, String zipCode, String remark, Byte stratusId, String createUser, Date createTime, Date updateTime, ComEntity eid,
			InfoDeptEntity deptId, Byte sourceId, String pid) {
		this.username = username;
		this.password = password;
		this.realname = realname;
		this.mobilephone = mobilephone;
		this.email = email;
		this.telephone = telephone;
		this.fax = fax;
		this.sex = sex;
		this.address = address;
		this.zipCode = zipCode;
		this.remark = remark;
		this.stratusId = stratusId;
		this.createUser = createUser;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.eid = eid;
		this.deptId = deptId;
		this.sourceId = sourceId;
		this.pid = pid;
	}
	
	@Column(name = "qq", length = 16)
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}
	
	@Column(name = "isLogined", length = 1)
	public String getIsLogined() {
		return isLogined;
	}

	public void setIsLogined(String isLogined) {
		this.isLogined = isLogined;
	}

	@Column(name = "username", length = 255)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", length = 64)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "realname", length = 8)
	public String getRealname() {
		return this.realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	@Column(name = "mobilephone", length = 30)
	public String getMobilephone() {
		return this.mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	@Column(name = "email", length = 255)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "telephone", length = 30)
	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Column(name = "fax", length = 16)
	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name = "sex", length = 1)
	public Character getSex() {
		return this.sex;
	}

	public void setSex(Character sex) {
		this.sex = sex;
	}

	@Column(name = "address", length = 32)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "zip_code", length = 8)
	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Column(name = "remark", length = 256)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "status_id")
	public Byte getStratusId() {
		return this.stratusId;
	}

	public void setStratusId(Byte stratusId) {
		this.stratusId = stratusId;
	}

	@Column(name = "create_user", length = 32)
	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time", length = 19)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	@ManyToOne
	@JoinColumn(name = "eid", columnDefinition = "varchar(32)", nullable = false)
	@NotFound(action=NotFoundAction.IGNORE)
	public ComEntity getEid() {
		return eid;
	}

	public void setEid(ComEntity eid) {
		this.eid = eid;
	}
	
	@ManyToOne
	@JoinColumn(name = "dept_id", columnDefinition = "varchar(32)", nullable = false)
	@NotFound(action=NotFoundAction.IGNORE)
	public InfoDeptEntity getDeptId() {
		return deptId;
	}

	public void setDeptId(InfoDeptEntity deptId) {
		this.deptId = deptId;
	}

	@Column(name = "source_id")
	public Byte getSourceId() {
		return this.sourceId;
	}

	public void setSourceId(Byte sourceId) {
		this.sourceId = sourceId;
	}

	@Column(name = "pid", length = 32)
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	@Column(name = "tempLogined", length = 1)
	public String getTempLogined() {
		return tempLogined;
	}

	public void setTempLogined(String tempLogined) {
		this.tempLogined = tempLogined;
	}
	
	// 首次登录时间
    private Date firstLogin ;

    @Column(name="first_login")
    @Temporal(TemporalType.TIMESTAMP)
	public Date getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(Date firstLogin) {
		this.firstLogin = firstLogin;
	}
    

}
