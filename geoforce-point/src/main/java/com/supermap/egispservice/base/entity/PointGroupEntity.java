package com.supermap.egispservice.base.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BIZ_POINT_GROUP")
public class PointGroupEntity   extends IdEntity implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String groupname; //分组名称
	private String userid;//用户id
	private String eid;//企业id
	private String dcode;//部门code
	private Date creatTime;//创建时间
	
	private PointStyleEntity styleid;
	private String def1;
	private String def2;
	
	
	
	public PointGroupEntity() {
	}
	public PointGroupEntity(String groupname, String userid, String eid,
			String dcode, Date creatTime
			,PointStyleEntity styleid
			) {
		this.groupname = groupname;
		this.userid = userid;
		this.eid = eid;
		this.dcode = dcode;
		this.creatTime = creatTime;
		this.styleid=styleid;
	}
	
	@Column(name = "groupname", length = 20)
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	
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
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "creat_time")
	public Date getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	
	@OneToOne(cascade=CascadeType.MERGE)
	@JoinColumn(name="styleid")
	public PointStyleEntity getStyleid() {
		return styleid;
	}
	public void setStyleid(PointStyleEntity styleid) {
		this.styleid = styleid;
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
