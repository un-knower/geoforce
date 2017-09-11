package com.supermap.egispweb.pojo.netpoint;

import com.supermap.egispservice.base.entity.PointExtcolEntity;
import com.supermap.egispservice.base.entity.PointExtcolValEntity;
import com.supermap.egispservice.base.entity.PointGroupEntity;

/**
 * 
 * <p>Title: NetPointBean</p>
 * Description:		网点导入Bean
 *
 * @author Huasong Huang
 * CreateTime: 2014-10-23 上午10:05:51
 */
public class NetPointBean {

	private String name;
	private String address;
	private double x;
	private double y;
	private String manager;
	private String phone;
	
	private PointExtcolEntity pointExtcolEntity;
	private PointExtcolValEntity pointExtcolValEntity;
	
	private PointGroupEntity groupid;
	
	
	
	
	
	public PointGroupEntity getGroupid() {
		return groupid;
	}
	public void setGroupid(PointGroupEntity groupid) {
		this.groupid = groupid;
	}
	public PointExtcolEntity getPointExtcolEntity() {
		return pointExtcolEntity;
	}
	public void setPointExtcolEntity(PointExtcolEntity pointExtcolEntity) {
		this.pointExtcolEntity = pointExtcolEntity;
	}
	public PointExtcolValEntity getPointExtcolValEntity() {
		return pointExtcolValEntity;
	}
	public void setPointExtcolValEntity(PointExtcolValEntity pointExtcolValEntity) {
		this.pointExtcolValEntity = pointExtcolValEntity;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
	
}
