package com.supermap.egispservice.lbs.pojo;
import java.util.Date;

public class CarGps implements java.io.Serializable{
	private static final long serialVersionUID = 22L;
	// Fields
	private String id;
	private String temCode;
	private String carId;
	private String deptCode;
	private Double lng;//超图国测局坐标
	private Double lat;
	private Double orgLng;//标准坐标
	private Double orgLat;
	private Double speed;
	private Double direction;
	private String addr;
	private Date sysDate;
	private Date gpsDate;
	private Double mile;
	private Double oil;
	private String picPath;
	private String others;
	private String zfTurn;
	private String alarm;//在当前记录发生的报警信息
	//以下是数据库中没有的扩展字段
	private String license;//车牌号
	private Double bdLng;//百度坐标
	private Double bdLat;
	private String directionStr;//车辆方向角
	private String gpsTime;//时间格式的字符串
	private String alarmStr;//报警信息解析后内容
	private int status;//车辆的GPS状态1为有正常的GPS数据2为有历史数据3表示没有上传过数据
    private String drivers;//车辆司机以“,”分割
    private String carBrand;//车辆品牌
    private String carType;//车辆类型
    private String carTypeName;//车辆类型名称
	// Constructors

	/** default constructor */
	public CarGps() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTemCode() {
		return temCode;
	}

	public void setTemCode(String temCode) {
		this.temCode = temCode;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getOrgLng() {
		return orgLng;
	}

	public void setOrgLng(Double orgLng) {
		this.orgLng = orgLng;
	}

	public Double getOrgLat() {
		return orgLat;
	}

	public void setOrgLat(Double orgLat) {
		this.orgLat = orgLat;
	}

	public Double getBdLng() {
		return bdLng;
	}

	public void setBdLng(Double bdLng) {
		this.bdLng = bdLng;
	}

	public Double getBdLat() {
		return bdLat;
	}

	public void setBdLat(Double bdLat) {
		this.bdLat = bdLat;
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
	
	public Date getSysDate() {
		return sysDate;
	}

	public void setSysDate(Date sysDate) {
		this.sysDate = sysDate;
	}

	public Date getGpsDate() {
		return gpsDate;
	}

	public void setGpsDate(Date gpsDate) {
		this.gpsDate = gpsDate;
	}

	public Double getMile() {
		return mile;
	}

	public void setMile(Double mile) {
		this.mile = mile;
	}

	public Double getOil() {
		return oil;
	}

	public void setOil(Double oil) {
		this.oil = oil;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}

	public String getZfTurn() {
		return zfTurn;
	}

	public void setZfTurn(String zfTurn) {
		this.zfTurn = zfTurn;
	}

	public String getAlarm() {
		return alarm;
	}

	public void setAlarm(String alarm) {
		this.alarm = alarm;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getDirectionStr() {
		return directionStr;
	}

	public void setDirectionStr(String directionStr) {
		this.directionStr = directionStr;
	}

	public String getGpsTime() {
		return gpsTime;
	}

	public void setGpsTime(String gpsTime) {
		this.gpsTime = gpsTime;
	}

	public String getAlarmStr() {
		return alarmStr;
	}

	public void setAlarmStr(String alarmStr) {
		this.alarmStr = alarmStr;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDrivers() {
		return drivers;
	}

	public void setDrivers(String drivers) {
		this.drivers = drivers;
	}

	public String getCarBrand() {
		return carBrand;
	}

	public void setCarBrand(String carBrand) {
		this.carBrand = carBrand;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}

	public String getCarTypeName() {
		return carTypeName;
	}

	public void setCarTypeName(String carTypeName) {
		this.carTypeName = carTypeName;
	}
	
	
}
