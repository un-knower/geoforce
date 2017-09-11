package com.supermap.egispservice.base.entity;

import java.util.Date;

/**
 * 
 * @author QiuChao
 * 
 */
public class InfoDeptVO implements java.io.Serializable {

	private static final long serialVersionUID = -7478693925702042917L;

	private String id;
	private String name;
	private String headName;
	private String headPhone;
	private String address;
	private String phone;
	private String zipcode;
	private String parentId;
	private String parentName;
	private byte type;
	private String code;
	private String createUserid;
	private Date operDate;

	/**
	 * 
	 * @return id
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 *            name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return headName
	 */
	public String getHeadName() {
		return headName;
	}

	/**
	 * 
	 * @param headName
	 *            headName
	 */
	public void setHeadName(String headName) {
		this.headName = headName;
	}

	/**
	 * 
	 * @return headPhone
	 */
	public String getHeadPhone() {
		return headPhone;
	}

	/**
	 * 
	 * @param headPhone
	 *            headPhone
	 */
	public void setHeadPhone(String headPhone) {
		this.headPhone = headPhone;
	}

	/**
	 * 
	 * @return address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 
	 * @param address
	 *            address
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 
	 * @return phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 
	 * @param phone
	 *            phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 
	 * @return zipcode
	 */
	public String getZipcode() {
		return zipcode;
	}

	/**
	 * 
	 * @param zipcode
	 *            zipcode
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	/**
	 * 
	 * @return parentId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * 
	 * @param parentId
	 *            parentId
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * 
	 * @return parentName
	 */
	public String getParentName() {
		return parentName;
	}

	/**
	 * 
	 * @param parentName
	 *            parentName
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	/**
	 * 
	 * @return type
	 */
	public byte getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 *            type
	 */
	public void setType(byte type) {
		this.type = type;
	}

	/**
	 * 
	 * @return code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 
	 * @param code
	 *            code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 
	 * @return createUserid
	 */
	public String getCreateUserid() {
		return createUserid;
	}

	/**
	 * 
	 * @param createUserid
	 *            createUserid
	 */
	public void setCreateUserid(String createUserid) {
		this.createUserid = createUserid;
	}

	/**
	 * 
	 * @return operDate
	 */
	public Date getOperDate() {
		return operDate;
	}

	/**
	 * 
	 * @param operDate
	 *            operDate
	 */
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

}
