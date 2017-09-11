package com.supermap.lbsp.provider.hibernate.lbsp;
import java.util.Date;
import java.util.List;



public class PersonStore implements java.io.Serializable {

	// Fields

	private String id;
	private String eid;
	private String deptId;
	private String name;
	private String shopkeeperName;
	private String shopkeeperPhone;
	private double bdLat;
	private double bdLng;
	private double ctLat;
	private double ctLng;
	private String typeId;
	private Short source;
	private String address;
	private Date operDate;
	private String userId;
	private String ico;
	
	//非数据库字段
	private List<PersonPic> pics;
	// Constructors

	/** default constructor */
	public PersonStore() {
	}

	/** minimal constructor */
	public PersonStore(String id, String eid, String name,String deptId,
			String shopkeeperName, String shopkeeperPhone, double bdLat,
			double bdLng, double ctLat, double ctLng, String typeId,
			Short source, String address, String userId,String ico) {
		this.id = id;
		this.eid = eid;
		this.deptId = deptId;
		this.name = name;
		this.shopkeeperName = shopkeeperName;
		this.shopkeeperPhone = shopkeeperPhone;
		this.bdLat = bdLat;
		this.bdLng = bdLng;
		this.ctLat = ctLat;
		this.ctLng = ctLng;
		this.typeId = typeId;
		this.source = source;
		this.address = address;
		this.userId = userId;
		this.ico = ico;
	}

	/** full constructor */
	public PersonStore(String id, String eid, String name,String deptId,
			String shopkeeperName, String shopkeeperPhone, double bdLat,
			double bdLng, double ctLat, double ctLng, String typeId,
			Short source, String address, Date operDate, String userId,String ico) {
		this.id = id;
		this.eid = eid;
		this.deptId = deptId;
		this.name = name;
		this.shopkeeperName = shopkeeperName;
		this.shopkeeperPhone = shopkeeperPhone;
		this.bdLat = bdLat;
		this.bdLng = bdLng;
		this.ctLat = ctLat;
		this.ctLng = ctLng;
		this.typeId = typeId;
		this.source = source;
		this.address = address;
		this.operDate = operDate;
		this.userId = userId;
		this.ico = ico;
	}

	public String getIco() {
		return ico;
	}

	public void setIco(String ico) {
		this.ico = ico;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
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

	public String getShopkeeperName() {
		return this.shopkeeperName;
	}

	public void setShopkeeperName(String shopkeeperName) {
		this.shopkeeperName = shopkeeperName;
	}

	public String getShopkeeperPhone() {
		return this.shopkeeperPhone;
	}

	public void setShopkeeperPhone(String shopkeeperPhone) {
		this.shopkeeperPhone = shopkeeperPhone;
	}

	public double getBdLat() {
		return this.bdLat;
	}

	public void setBdLat(double bdLat) {
		this.bdLat = bdLat;
	}

	public double getBdLng() {
		return this.bdLng;
	}

	public void setBdLng(double bdLng) {
		this.bdLng = bdLng;
	}

	public double getCtLat() {
		return this.ctLat;
	}

	public void setCtLat(double ctLat) {
		this.ctLat = ctLat;
	}

	public double getCtLng() {
		return this.ctLng;
	}

	public void setCtLng(double ctLng) {
		this.ctLng = ctLng;
	}

	public String getTypeId() {
		return this.typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public Short getSource() {
		return this.source;
	}

	public void setSource(Short source) {
		this.source = source;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getOperDate() {
		return this.operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<PersonPic> getPics() {
		return pics;
	}

	public void setPics(List<PersonPic> pics) {
		this.pics = pics;
	}

}