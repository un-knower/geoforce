package com.supermap.lbsp.provider.hibernate.lbsp;


import java.util.Date;




public class Person implements java.io.Serializable {
	
	// Fields

	private String id;
	private String eid;
	private String name;
	private String nickname;
	private String password;
	private String deptId;
	private Integer age;
	private Short sex;
	private String email;
	private String termtype;
	private String termcode;
	private String phone;
	private String position;
	private String userId;
	private Date operDate;
	private Short online;
	//扩展字段
	private String deptName;
	private String StoreNames;
	private String termName;

	// Constructors


	

	

	/** default constructor */
	public Person() {
	}

	/** minimal constructor */
	public Person(String id, String eid, String name, String password,
			String deptId, String email, String termcode, String userId) {
		this.id = id;
		this.eid = eid;
		this.name = name;
		this.password = password;
		this.deptId = deptId;
		this.email = email;
		this.termcode = termcode;
		this.userId = userId;
	}

	/** full constructor */
	public Person(String id, String eid, String name, String nickname,
			String password, String deptId, Integer age, Short sex,
			String email, String termtype, String termcode, String phone,
			String position,Short online, String userId, Date operDate) {
		this.id = id;
		this.eid = eid;
		this.name = name;
		this.nickname = nickname;
		this.password = password;
		this.deptId = deptId;
		this.age = age;
		this.sex = sex;
		this.email = email;
		this.termtype = termtype;
		this.termcode = termcode;
		this.phone = phone;
		this.position = position;
		this.userId = userId;
		this.operDate = operDate;
		this.online = online;
	}

	public String getTermName() {
		return termName;
	}

	public void setTermName(String termName) {
		this.termName = termName;
	}
	public Short getOnline() {
		return online;
	}

	public void setOnline(Short online) {
		this.online = online;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEid() {
		return this.eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDeptId() {
		return this.deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public Integer getAge() {
		return this.age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Short getSex() {
		return this.sex;
	}

	public void setSex(Short sex) {
		this.sex = sex;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTermtype() {
		return this.termtype;
	}

	public void setTermtype(String termtype) {
		this.termtype = termtype;
	}

	public String getTermcode() {
		return this.termcode;
	}

	public void setTermcode(String termcode) {
		this.termcode = termcode;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPosition() {
		return this.position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getStoreNames() {
		return StoreNames;
	}

	public void setStoreNames(String storeNames) {
		StoreNames = storeNames;
	}
}