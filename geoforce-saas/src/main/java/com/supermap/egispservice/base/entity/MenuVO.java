package com.supermap.egispservice.base.entity;


/**
 * 页面菜单VO类，非数据库实体
 */
public class MenuVO implements java.io.Serializable {

	private String id;
	private String menuName;
	private String pid;
	private String iconUrl;
	private String url;
	private byte statusId;
	private String useTime;
	private String deadLine;
	private String funcName;
	private String days;
	private String isLogined;
	private String orderItemId;
	public MenuVO() {
	}
	
	public String getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}

	public String getIsLogined() {
		return isLogined;
	}

	public void setIsLogined(String isLogined) {
		this.isLogined = isLogined;
	}

	public String getFuncName() {
		return funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getUseTime() {
		return useTime;
	}

	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}

	public String getDeadLine() {
		return deadLine;
	}

	public void setDeadLine(String deadLine) {
		this.deadLine = deadLine;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public byte getStatusId() {
		return statusId;
	}
	public void setStatusId(byte statusId) {
		this.statusId = statusId;
	}

	
}
