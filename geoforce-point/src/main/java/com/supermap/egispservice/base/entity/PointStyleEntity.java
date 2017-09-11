package com.supermap.egispservice.base.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BIZ_POINT_STYLE")
public class PointStyleEntity   extends IdEntity implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String stylename;//样式名称
	//PointGroupEntity groupid;
	String userid;
	String eid;
	String dcode;
	String appearance;//外观
	String appsize;//大小
	String appcolor;//颜色
	String apppic;//图案
	String appcustom;//自定义
	Date creat_time;//创建时间
	String def1;//记录是不是子集，0表示不是，1表示是子集
	String def2;
	
	
	
	public PointStyleEntity() {
		// TODO Auto-generated constructor stub
	}
	public PointStyleEntity(String id,String stylename
			//, PointGroupEntity groupid
			, String userid,
			String eid, String dcode, String appearance, String appsize,
			String appcolor, String apppic, String appcustom, Date creat_time,
			String def1, String def2) {
		this.id=id;
		this.stylename = stylename;
		//this.groupid = groupid;
		this.userid = userid;
		this.eid = eid;
		this.dcode = dcode;
		this.appearance = appearance;
		this.appsize = appsize;
		this.appcolor = appcolor;
		this.apppic = apppic;
		this.appcustom = appcustom;
		this.creat_time = creat_time;
		this.def1 = def1;
		this.def2 = def2;
	}
	
	@Column(name = "stylename", length = 20)
	public String getStylename() {
		return stylename;
	}
	public void setStylename(String stylename) {
		this.stylename = stylename;
	}
	
	/*@OneToOne(cascade=CascadeType.MERGE)
	@JoinColumn(name="groupid")
	public PointGroupEntity getGroupid() {
		return groupid;
	}
	public void setGroupid(PointGroupEntity groupid) {
		this.groupid = groupid;
	}*/
	
	@Column(name = "userid", length = 32)
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	@Column(name = "eid", length = 32)
	public String getEid() {
		return eid;
	}
	public void setEid(String eid) {
		this.eid = eid;
	}
	
	@Column(name = "dcode", length = 50)
	public String getDcode() {
		return dcode;
	}
	public void setDcode(String dcode) {
		this.dcode = dcode;
	}
	
	@Column(name = "appearance", length = 50)
	public String getAppearance() {
		return appearance;
	}
	public void setAppearance(String appearance) {
		this.appearance = appearance;
	}
	
	@Column(name = "appsize", length = 50)
	public String getAppsize() {
		return appsize;
	}
	public void setAppsize(String appsize) {
		this.appsize = appsize;
	}
	
	@Column(name = "appcolor", length = 50)
	public String getAppcolor() {
		return appcolor;
	}
	public void setAppcolor(String appcolor) {
		this.appcolor = appcolor;
	}
	
	@Column(name = "apppic", length = 50)
	public String getApppic() {
		return apppic;
	}
	public void setApppic(String apppic) {
		this.apppic = apppic;
	}
	
	@Column(name = "appcustom", length = 100)
	public String getAppcustom() {
		return appcustom;
	}
	public void setAppcustom(String appcustom) {
		this.appcustom = appcustom;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creat_time")
	public Date getCreat_time() {
		return creat_time;
	}
	public void setCreat_time(Date creat_time) {
		this.creat_time = creat_time;
	}
	
	@Column(name = "def1", length = 100)
	public String getDef1() {
		return def1;
	}
	public void setDef1(String def1) {
		this.def1 = def1;
	}
	
	@Column(name = "def2", length = 100)
	public String getDef2() {
		return def2;
	}
	public void setDef2(String def2) {
		this.def2 = def2;
	}
	
	


}
