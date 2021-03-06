package com.supermap.egispservice.base.entity;

// Generated 2014-9-4 11:41:47 by Hibernate Tools 4.0.0

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.supermap.egispservice.base.constants.EbossStatusConstants;

/**
 * EgispRssOrder generated by hbm2java
 */
@Entity
@Table(name = "EGISP_RSS_ORDER")
public class OrderEntity extends IdEntity implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	// 订单状态
		
		private OrderStatusEntity status;
		private String eid;
		private Date submitTime = new Date();
		private Date updateTime;
		// 总金额
		private float totalPrice;
		private String remarks;
		// 折后价
		private float consultSum;
		private String funcNames;
		private int orderType = EbossStatusConstants.ORDER_TYPE_CUSTOM;
		private String auditRemarks;
		private UserEntity user;
		private List<OrderItemsEntity> orderItems = new ArrayList<OrderItemsEntity>();
		
		
		public void addOrderItem(OrderItemsEntity orderItem){
			orderItem.setOrderId(this);
			this.totalPrice +=orderItem.getConsultPrice().floatValue();
			this.consultSum = this.totalPrice;
			this.orderItems.add(orderItem);
		}
		public void removeOrderItemByID(String id){
			for(int i=0;i<this.orderItems.size();i++){
				if(id.equals(this.orderItems.get(i).getId())){
					this.orderItems.remove(i);
				}
			}
		}
		
		@Column(name="func_names",length=256)
		public String getFuncNames() {
			return funcNames;
		}
		public void setFuncNames(String funcNames) {
			this.funcNames = funcNames;
		}
		
		/**
		 * 
		 * <p>Title ：updateConsultPrice</p>
		 * Description：更新商谈总价
		 * Author：Huasong Huang
		 * CreateTime：2014-8-13 上午10:40:54
		 */
		public void updateConsultPrice(){
			float tempCosnultSum = 0;
			for(OrderItemsEntity item : orderItems){
				tempCosnultSum += item.getConsultPrice().floatValue();
			}
			this.consultSum = tempCosnultSum;
		}
		
		@OneToMany(cascade=CascadeType.ALL,mappedBy="orderId",fetch=FetchType.EAGER)
		public List<OrderItemsEntity> getOrderItems(){
			return this.orderItems;
		}
		
		
		@ManyToOne(cascade={CascadeType.REFRESH})
		@JoinColumn(name="user_id",nullable=false)
		@NotFound(action=NotFoundAction.IGNORE)
		public UserEntity getUser() {
			return user;
		}

		public void setUser(UserEntity user) {
			this.user = user;
		}

		
		@ManyToOne(cascade={CascadeType.REFRESH})
		@JoinColumn(name="status_id",nullable=false)
		public OrderStatusEntity getStatus() {
			return status;
		}
		public void setStatus(OrderStatusEntity status) {
			this.status = status;
		}
		// 提交时间
		@Column(name="submit_time")
		@Temporal(TemporalType.TIMESTAMP)
		public Date getSubmitTime() {
			return submitTime;
		}

		public void setSubmitTime(Date submitTime) {
			this.submitTime = submitTime;
		}

		// 更新时间
		@Column(name="update_time")
		@Temporal(TemporalType.TIMESTAMP)
		public Date getUpdateTime() {
			return updateTime;
		}

		public void setUpdateTime(Date updateTime) {
			this.updateTime = updateTime;
		}

		@Column(name="total_sum",precision=10,scale=2)
		public float getTotalPrice() {
			return totalPrice;
		}

		public void setTotalPrice(float totalPrice) {
			this.totalPrice = totalPrice;
		}
		@Column(name="remark",length=256)
		public String getRemarks() {
			return remarks;
		}

		public void setRemarks(String remarks) {
			this.remarks = remarks;
		}
		@Column(name="consult_sum",precision=10,scale=2)
		public float getConsultSum() {
			return consultSum;
		}

		public void setConsultSum(float consultSum) {
			this.consultSum = consultSum;
		}

		public void setOrderItems(List<OrderItemsEntity> orderItems) {
			this.orderItems = orderItems;
		}
		@Column(name="audit_remark",length=256)
		public String getAuditRemarks() {
			return auditRemarks;
		}
		public void setAuditRemarks(String auditRemarks) {
			this.auditRemarks = auditRemarks;
		}
		@Column(name="eid",length=32)
		public String getEid() {
			return eid;
		}
		public void setEid(String eid) {
			this.eid = eid;
		}
		
		public void setOrderType(int orderType) {
			this.orderType = orderType;
		}
		@Column(name="order_type",nullable=false)
		public int getOrderType() {
			return orderType;
		}

		private String mainModuleId;
		@Column(name="mainModuleId",length=32)
		public String getMainModuleId() {
			return mainModuleId;
		}

		public void setMainModuleId(String mainModuleId) {
			this.mainModuleId = mainModuleId;
		}
}
