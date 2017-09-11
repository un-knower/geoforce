package com.supermap.egispservice.base.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.supermap.egispservice.base.entity.PointExtcolValEntity;
import com.supermap.egispservice.base.entity.PointGroupEntity;
import com.supermap.egispservice.base.entity.PointStyleEntity;

public class NetPointInfoResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String address;
	private BigDecimal smx;
	private BigDecimal smy;
	private String dutyName;
	private String dutyPhone;
	private String areaId;
	private String areaName;
	private Date createTime;
	private Date updateTime;
	private Byte deleteFlag;
	private String userId;
	private String enterpriseId;
	private String departmentId;
	private String netPicPath;
	private String dutyPicPath;
	private String iconStyle;
	private int status;
	private PointExtcolValEntity pointExtcolValEntity;
	
	private String col1;
	private String col2;
	private String col3;
	private String col4;
	private String col5;
	private String col6;
	private String col7;
	private String col8;
	private String col9;
	private String col10;
	
	private PointStyleEntity styleid;
	private PointGroupEntity groupid;
	
	private String username;
	/**
	 * 部门编码
	 */
	private String dcode;
	
	
	public String getDcode() {
		return dcode;
	}
	public void setDcode(String dcode) {
		this.dcode = dcode;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public PointStyleEntity getStyleid() {
		return styleid;
	}
	public void setStyleid(PointStyleEntity styleid) {
		this.styleid = styleid;
	}
	public PointGroupEntity getGroupid() {
		return groupid;
	}
	public void setGroupid(PointGroupEntity groupid) {
		this.groupid = groupid;
	}
	public String getCol1() {
		return col1;
	}
	public void setCol1(String col1) {
		this.col1 = col1;
	}
	public String getCol2() {
		return col2;
	}
	public void setCol2(String col2) {
		this.col2 = col2;
	}
	public String getCol3() {
		return col3;
	}
	public void setCol3(String col3) {
		this.col3 = col3;
	}
	public String getCol4() {
		return col4;
	}
	public void setCol4(String col4) {
		this.col4 = col4;
	}
	public String getCol5() {
		return col5;
	}
	public void setCol5(String col5) {
		this.col5 = col5;
	}
	public String getCol6() {
		return col6;
	}
	public void setCol6(String col6) {
		this.col6 = col6;
	}
	public String getCol7() {
		return col7;
	}
	public void setCol7(String col7) {
		this.col7 = col7;
	}
	public String getCol8() {
		return col8;
	}
	public void setCol8(String col8) {
		this.col8 = col8;
	}
	public String getCol9() {
		return col9;
	}
	public void setCol9(String col9) {
		this.col9 = col9;
	}
	public String getCol10() {
		return col10;
	}
	public void setCol10(String col10) {
		this.col10 = col10;
	}
	public PointExtcolValEntity getPointExtcolValEntity() {
		return pointExtcolValEntity;
	}
	public void setPointExtcolValEntity(PointExtcolValEntity pointExtcolValEntity) {
		this.pointExtcolValEntity = pointExtcolValEntity;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getSmx() {
		return smx;
	}
	public void setSmx(BigDecimal smx) {
		this.smx = smx;
	}
	public BigDecimal getSmy() {
		return smy;
	}
	public void setSmy(BigDecimal smy) {
		this.smy = smy;
	}
	public String getDutyName() {
		return dutyName;
	}
	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}
	public String getDutyPhone() {
		return dutyPhone;
	}
	public void setDutyPhone(String dutyPhone) {
		this.dutyPhone = dutyPhone;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Byte getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(Byte deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(String enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public String getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getNetPicPath() {
		return netPicPath;
	}
	public void setNetPicPath(String netPicPath) {
		this.netPicPath = netPicPath;
	}
	public String getDutyPicPath() {
		return dutyPicPath;
	}
	public void setDutyPicPath(String dutyPicPath) {
		this.dutyPicPath = dutyPicPath;
	}
	public String getIconStyle() {
		return iconStyle;
	}
	public void setIconStyle(String iconStyle) {
		this.iconStyle = iconStyle;
	}
	
	

}
