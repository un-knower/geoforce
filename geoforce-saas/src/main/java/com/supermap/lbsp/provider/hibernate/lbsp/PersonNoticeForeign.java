package com.supermap.lbsp.provider.hibernate.lbsp;



/**
 * LbsPersonNoticeForeign entity. @author MyEclipse Persistence Tools
 */

public class PersonNoticeForeign implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	
	private String id;
	private String noticeId;
	private String personId;
	private Short status;

	// Constructors

	/** default constructor */
	public PersonNoticeForeign() {
	}

	/** full constructor */
	public PersonNoticeForeign(String id, String noticeId, String personId,
			Short status) {
		this.id = id;
		this.noticeId = noticeId;
		this.personId = personId;
		this.status = status;
	}

	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNoticeId() {
		return this.noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}

	public String getPersonId() {
		return this.personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

}