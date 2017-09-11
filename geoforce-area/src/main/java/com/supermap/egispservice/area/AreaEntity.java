package com.supermap.egispservice.area;

import java.io.Serializable;


public class AreaEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String areaNumber;
	private String net_Id;
	private String create_time;
	private String update_time;
	// 删除标识，1：删除，0：未删除，默认为未删除
	private int delete_flag = 0;
	private String user_id;
	private String enterprise_id;
	private String dcode;
	private Point2D center;
	private String admincode;
	
	private Point2D[] points = null;
	private int[] parts;
	
	/**
	 * 区划状态  ：0 正常；1 停用
	 */
	private int area_status=0; 
	
	/**
	 * 关联的区划id
	 */
	private String relation_areaid;
	
	/**
	 * 关联的区划名称
	 */
	private String relation_areaname;
	
	/**
	 * 关联的区划编号
	 */
	private String relation_areanumber;
	
	/**
	 * 网格组名称
	 */
	private String wgzName;
	/**
	 * 网格组编码
	 */
	private String wgzCode;
	/**
	 * 线路编码
	 */
	private String lineCode;
	/**
	 * 线路名称
	 */
	private String lineName;
	
	
	public AreaEntity() {
		// TODO Auto-generated constructor stub
	}
	
	public AreaEntity(String id, String name, String areaNumber, String net_id, String create_time, String update_time,
			int delete_flag, String user_id, String enterprise_id, String dcode) {
		this.id = id;
		this.name = name;
		this.areaNumber = areaNumber;
		this.net_Id = net_id;
		this.create_time = create_time;
		this.update_time = update_time;
		this.delete_flag = delete_flag;
		this.user_id = user_id;
		this.enterprise_id = enterprise_id;
		this.dcode = dcode;
		
	}
	
	
	
	public String getWgzName() {
		return wgzName;
	}

	public void setWgzName(String wgzName) {
		this.wgzName = wgzName;
	}

	public String getWgzCode() {
		return wgzCode;
	}

	public void setWgzCode(String wgzCode) {
		this.wgzCode = wgzCode;
	}

	public String getLineCode() {
		return lineCode;
	}

	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
	}

	public String getLineName() {
		return lineName;
	}

	public void setLineName(String lineName) {
		this.lineName = lineName;
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
	public String getAreaNumber() {
		return areaNumber;
	}
	public void setAreaNumber(String areaNumber) {
		this.areaNumber = areaNumber;
	}
	public String getNet_Id() {
		return net_Id;
	}
	public void setNet_Id(String netId) {
		net_Id = netId;
	}
	
	
	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String createTime) {
		create_time = createTime;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String updateTime) {
		update_time = updateTime;
	}

	public int getDelete_flag() {
		return delete_flag;
	}

	public void setDelete_flag(int deleteFlag) {
		delete_flag = deleteFlag;
	}

	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String userId) {
		user_id = userId;
	}
	public String getEnterprise_id() {
		return enterprise_id;
	}
	public void setEnterprise_id(String enterpriseId) {
		enterprise_id = enterpriseId;
	}

	public String getDcode() {
		return dcode;
	}

	public void setDcode(String dcode) {
		this.dcode = dcode;
	}

	public Point2D[] getPoints() {
		return points;
	}

	public void setPoints(Point2D[] points) {
		this.points = points;
	}

	public Point2D getCenter() {
		return center;
	}

	public void setCenter(Point2D center) {
		this.center = center;
	}

	public int[] getParts() {
		return parts;
	}

	public void setParts(int[] parts) {
		this.parts = parts;
	}

	public String getAdmincode() {
		return admincode;
	}

	public void setAdmincode(String admincode) {
		this.admincode = admincode;
	}

	/**
	 * 区划状态  ：0 正常；1 停用
	 * @return
	 * @Author Juannyoh
	 * 2016-12-19上午9:35:39
	 */
	public int getArea_status() {
		return area_status;
	}

	public void setArea_status(int area_status) {
		this.area_status = area_status;
	}

	/**
	 * 关联的区划id，只有停用状态的才能关联另一个区划id
	 * @return
	 * @Author Juannyoh
	 * 2016-12-19上午9:35:45
	 */
	public String getRelation_areaid() {
		return relation_areaid;
	}

	public void setRelation_areaid(String relation_areaid) {
		this.relation_areaid = relation_areaid;
	}

	public String getRelation_areaname() {
		return relation_areaname;
	}

	public void setRelation_areaname(String relation_areaname) {
		this.relation_areaname = relation_areaname;
	}

	public String getRelation_areanumber() {
		return relation_areanumber;
	}

	public void setRelation_areanumber(String relation_areanumber) {
		this.relation_areanumber = relation_areanumber;
	}
	
	
}
