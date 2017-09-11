package com.supermap.lbsp.provider.hibernate.gps;


import java.util.Date;


/**
 * CfgCarAlerm entity. @author MyEclipse Persistence Tools
 */

public class CarAlarm implements java.io.Serializable {

	// Fields

	private String id;
	private String name;
	private Short status;
	private Date alarmDate;
	private Date dealDate;
	private String opinion;
	private String typeId;
	private String userId;
	private String carId;
	private String temCode;
	private Double longitude;
	private Double latitude;
	private Double speed;
	private Double direction;
	private String foreignId;
	private String addr;
	private Date lastDate;// 每次报警最后时间
	private Long difTime;// 报警持续时间 单位分钟  lastDate-alarmDate lastTimeStr以后不用
	private String others;//预留字段 其它内容

	private String stautsName;
	private String typeName;
	private String carLicense;
	private String userName;
	private String alarmDateStr;
	private String dealDateStr;
	private String deptCode;
	private String deptName;
	private String lastTimeStr;// 报警持续时间 lastDate-alarmDate

	// Constructors

	/** default constructor */
	public CarAlarm() {
	}

	/** full constructor */
	public CarAlarm(String name, Short status, Date alarmDate, Date dealDate,
			String opinion, String typeId, String userId, String carId,
			Double longitude, Double latitud, Double speed, Double direction,
			String foreignId, String addr) {
		this.name = name;
		this.status = status;
		this.alarmDate = alarmDate;
		this.dealDate = dealDate;
		this.opinion = opinion;
		this.typeId = typeId;
		this.userId = userId;
		this.carId = carId;
		this.longitude = longitude;
		this.latitude = latitud;
		this.speed = speed;
		this.direction = direction;
		this.foreignId = foreignId;
		this.addr = addr;
	}

	// Property accessors
	
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public Short getStatus() {
		return this.status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	
	public Date getAlarmDate() {
		return alarmDate;
	}

	public void setAlarmDate(Date alarmDate) {
		this.alarmDate = alarmDate;
	}

	
	public Date getDealDate() {
		return this.dealDate;
	}

	public void setDealDate(Date dealDate) {
		this.dealDate = dealDate;
	}

	
	public String getOpinion() {
		return this.opinion;
	}

	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}

	
	public String getTypeId() {
		return this.typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	
	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public Double getLongitude() {
		return longitude;
	}

	
	public String getForeignId() {
		return foreignId;
	}

	public void setForeignId(String foreignId) {
		this.foreignId = foreignId;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	
	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	
	public Double getDirection() {
		return direction;
	}

	public void setDirection(Double direction) {
		this.direction = direction;
	}

	
	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	
	public String getStautsName() {
		return stautsName;
	}

	public void setStautsName(String stautsName) {
		this.stautsName = stautsName;
	}

	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	
	public String getCarLicense() {
		return carLicense;
	}

	public void setCarLicense(String carLicense) {
		this.carLicense = carLicense;
	}

	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	
	public String getAlarmDateStr() {
		return alarmDateStr;
	}

	public void setAlarmDateStr(String alarmDateStr) {
		this.alarmDateStr = alarmDateStr;
	}

	
	public String getDealDateStr() {
		return dealDateStr;
	}

	public void setDealDateStr(String dealDateStr) {
		this.dealDateStr = dealDateStr;
	}

	
	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getLastTimeStr() {
		return lastTimeStr;
	}

	public void setLastTimeStr(String lastTimeStr) {
		this.lastTimeStr = lastTimeStr;
	}

	public Long getDifTime() {
		return difTime;
	}

	public void setDifTime(Long difTime) {
		this.difTime = difTime;
	}

	public String getTemCode() {
		return temCode;
	}

	public void setTemCode(String temCode) {
		this.temCode = temCode;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	


}