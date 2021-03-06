package com.supermap.egispservice.base.entity;

// Generated 2014-9-4 11:41:47 by Hibernate Tools 4.0.0

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * InfoDept generated by hbm2java
 */
@Entity
@Table(name = "INFO_DEPT")
public class InfoDeptEntity extends IdEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String headName;
	private String headPhone;
	private String address;
	private String phone;
	private String zipcode;
	private String parentId;
	//private InfoDeptEntity parentInfoDeptEntity;
	private byte type;
	private String code;
	private String createUserid;
	private Date operDate;

	public InfoDeptEntity() {
	}

	public InfoDeptEntity(String id, String name, String phone, byte type,
			String code, String createUserid, Date operDate) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.type = type;
		this.code = code;
		this.createUserid = createUserid;
		this.operDate = operDate;
	}

	public InfoDeptEntity(String name, String headName,
			String headPhone, String address, String phone, String zipcode,
			/*InfoDeptEntity parentInfoDeptEntity,*/ String parentId, byte type, String code,
			String createUserid, Date operDate) {
		this.name = name;
		this.headName = headName;
		this.headPhone = headPhone;
		this.address = address;
		this.phone = phone;
		this.zipcode = zipcode;
		//this.parentInfoDeptEntity = parentInfoDeptEntity;
		this.parentId = parentId;
		this.type = type;
		this.code = code;
		this.createUserid = createUserid;
		this.operDate = operDate;
	}

	@Column(name = "NAME", nullable = false, length = 48)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "HEAD_NAME", length = 16)
	public String getHeadName() {
		return this.headName;
	}

	public void setHeadName(String headName) {
		this.headName = headName;
	}

	@Column(name = "HEAD_PHONE", length = 30)
	public String getHeadPhone() {
		return this.headPhone;
	}

	public void setHeadPhone(String headPhone) {
		this.headPhone = headPhone;
	}

	@Column(name = "ADDRESS", length = 128)
	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "PHONE", nullable = false, length = 30)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "PARENT_ID", nullable = false, length = 32)
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/*public InfoDeptEntity getParentInfoDeptEntity() {
		return parentInfoDeptEntity;
	}

	public void setParentInfoDeptEntity(InfoDeptEntity parentInfoDeptEntity) {
		this.parentInfoDeptEntity = parentInfoDeptEntity;
	}*/

	@Column(name = "ZIPCODE", length = 8)
	public String getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	@Column(name = "TYPE", nullable = false)
	public byte getType() {
		return this.type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	@Column(name = "CODE", nullable = false, length = 128)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "CREATE_USERID", nullable = false, length = 32)
	public String getCreateUserid() {
		return this.createUserid;
	}

	public void setCreateUserid(String createUserid) {
		this.createUserid = createUserid;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OPER_DATE", nullable = false, length = 19)
	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

}
