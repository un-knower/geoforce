package com.supermap.lbsp.provider.hibernate.gps;

import java.io.Serializable;
import java.util.Date;

import com.supermap.lbsp.provider.hibernate.lbsp.PersonSign;

/**
 * 
* ClassName：PersonGps   
* 类描述：   巡店人员当前位置信息bean
* 操作人：wangshuang   
* 操作时间：2014-11-27 下午02:49:48     
* @version 1.0
 */
public class PersonGps implements Serializable{
	private static final long serialVersionUID = 2211L;
	
	private String id;
	private String termCode;//终端号 举例手机对应IME码
	private String mobile;//手机号
	private String personId;
	private String deptCode;
	private Double lng;//超图国测局坐标
	private Double lat;
	private Double bdLng;//百度坐标
	private Double bdLat;
	private Double direction;
	private String addr;
	private Date sysDate;
	private Date gpsDate;
	private String others;
	private String picId;//在当前位置拍照的照片id、
	//以下是数据库中没有的扩展字段
	private String personName;//人员姓名
	private String directionStr;//方向角
	private String gpsTime;//时间格式的字符串
	private int status;//车辆的GPS状态1为有正常的GPS数据2为有历史数据3表示没有上传过数据
	private PersonSign sign;//该员工最新的巡店工作结果
	private String storeName;//巡店工作结果对应的门店名称
	private String signInfo;//巡店情况
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTermCode() {
		return termCode;
	}
	public void setTermCode(String termCode) {
		this.termCode = termCode;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
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
	public String getOthers() {
		return others;
	}
	public void setOthers(String others) {
		this.others = others;
	}
	public String getPicId() {
		return picId;
	}
	public void setPicId(String picId) {
		this.picId = picId;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
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
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public PersonSign getSign() {
		return sign;
	}
	public void setSign(PersonSign sign) {
		this.sign = sign;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getSignInfo() {
		return signInfo;
	}
	public void setSignInfo(String signInfo) {
		this.signInfo = signInfo;
	}
    
	
}