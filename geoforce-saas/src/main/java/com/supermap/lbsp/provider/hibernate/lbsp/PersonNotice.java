package com.supermap.lbsp.provider.hibernate.lbsp;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * LbsPersonNotice entity. @author MyEclipse Persistence Tools
 */

public class PersonNotice implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	
	private String id;
	private String deptId;
	private String pubUser;
	private Date pubDate;
	private String content;
	private String title;
	private Short pubWay;
	private Short type;

	private String pubUserName;//发送人名称
	private Long personNum;//发送人数
	// Constructors
	/** default constructor */
	public PersonNotice() {
	}
	public String getPubUserName() {
		return pubUserName;
	}

	
	/** full constructor */
	public PersonNotice(String id, String deptId, String pubUser,
			Date pubDate, String content, String title, Short pubWay,
			Short type) {
		this.id = id;
		this.deptId = deptId;
		this.pubUser = pubUser;
		this.pubDate = pubDate;
		this.content = content;
		this.title = title;
		this.pubWay = pubWay;
		this.type = type;
	}

	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getPubUser() {
		return this.pubUser;
	}

	public void setPubUser(String pubUser) {
		this.pubUser = pubUser;
	}

	public Date getPubDate() {
		return this.pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Short getPubWay() {
		return this.pubWay;
	}

	public void setPubWay(Short pubWay) {
		this.pubWay = pubWay;
	}

	public Short getType() {
		return this.type;
	}

	public void setType(Short type) {
		this.type = type;
	}
	public void setPubUserName(String pubUserName) {
		this.pubUserName = pubUserName;
	}

	public Long getPersonNum() {
		return personNum;
	}

	public void setPersonNum(Long personNum) {
		this.personNum = personNum;
	}

	

}