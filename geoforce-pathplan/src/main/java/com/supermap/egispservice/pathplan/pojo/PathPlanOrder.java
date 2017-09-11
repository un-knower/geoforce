package com.supermap.egispservice.pathplan.pojo;

import java.io.Serializable;
import java.util.Date;

import com.supermap.data.Point2D;

/**
 * 
 * <p>Title: PathPlanOrder</p>
 * Description:	线路规划订单对象
 *
 * @author Huasong Huang
 * CreateTime: 2015-12-1 上午10:13:55
 */
public class PathPlanOrder implements Serializable,Comparable<PathPlanOrder> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String orderId;
	// 经纬度
	private Point2D pp;
	// 订单缓冲时间,分钟
	private int bufferMin = 15;
	// 卸货及停留固定消耗时间
	private String orderDate;
	
	private int carCost;
	
	
	private Date startSendTime;
	private Date endSendTime;
	// 所属分组
	private String group;
	
	private Date realSendTime;
	// 摩卡托
	private Point2D p;
	private boolean isFixedSend = false;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getStartSendTime() {
		return startSendTime;
	}
	public void setStartSendTime(Date startSendTime) {
		this.startSendTime = startSendTime;
	}
	public Date getEndSendTime() {
		return endSendTime;
	}
	public void setEndSendTime(Date endSendTime) {
		this.endSendTime = endSendTime;
	}
	
	public Point2D getP() {
		return p;
	}
	public void setP(Point2D p) {
		this.p = p;
	}
	public boolean isFixedSend() {
		return isFixedSend;
	}
	public void setFixedSend(boolean isFixedSend) {
		this.isFixedSend = isFixedSend;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public int getBufferMin() {
		return bufferMin;
	}
	public void setBufferMin(int bufferMin) {
		this.bufferMin = bufferMin;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	@Override
	public int compareTo(PathPlanOrder o) {
		if (this.endSendTime.before(o.getEndSendTime())) {
			return -1;
		} else if (this.endSendTime.after(o.getEndSendTime())) {
			return 1;
		} else {
			return 0;
		}
	}
	public Date getRealSendTime() {
		return realSendTime;
	}
	public void setRealSendTime(Date realSendTime) {
		this.realSendTime = realSendTime;
	}
	public Point2D getPp() {
		return pp;
	}
	public void setPp(Point2D pp) {
		this.pp = pp;
	}
	public int getCarCost() {
		return carCost;
	}
	public void setCarCost(int carCost) {
		this.carCost = carCost;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	
	
	
	
}
