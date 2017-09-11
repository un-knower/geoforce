package com.supermap.egispservice.base.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BIZ_POINT_STYLE_CUSTOM")
public class PointStyleCustomEntity   extends IdEntity implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String userid; 
	String eid;
	String dcode;
	String filepath;//文件地址
	Date uploadtime;//上传时间
	int height;
	int width;
	
	public PointStyleCustomEntity() {
	}
	public PointStyleCustomEntity(String userid, String eid, String dcode,
			String filepath, Date uploadtime,int height,int width) {
		this.userid = userid;
		this.eid = eid;
		this.dcode = dcode;
		this.filepath = filepath;
		this.uploadtime = uploadtime;
		this.height=height;
		this.width=width;
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
	
	@Column(name = "filepath", length = 100)
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "uploadtime")
	public Date getUploadtime() {
		return uploadtime;
	}
	public void setUploadtime(Date uploadtime) {
		this.uploadtime = uploadtime;
	}
	
	@Column(name = "height", length = 8)
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	
	@Column(name = "width", length = 8)
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}

	
}
