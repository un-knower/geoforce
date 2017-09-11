package com.supermap.egispservice.base.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BIZ_POINT_PIC")
public class PointPicEntity   extends IdEntity implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String pointid; //网点id
	String filepath;//文件地址
	Date uploadtime;//上传时间
	int height;
	int width;
	
	public PointPicEntity() {
	}
	public PointPicEntity(String pointid, 
			String filepath, Date uploadtime,int height,int width) {
		this.pointid = pointid;
		this.filepath = filepath;
		this.uploadtime = uploadtime;
		this.height=height;
		this.width=width;
	}
	
	
	@Column(name = "pointid", length = 32)
	public String getPointid() {
		return pointid;
	}
	public void setPointid(String pointid) {
		this.pointid = pointid;
	}
	
	
	@Column(name = "filepath", length = 200)
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
