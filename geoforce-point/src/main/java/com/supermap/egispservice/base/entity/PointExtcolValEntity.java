package com.supermap.egispservice.base.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="biz_point_extcolval")
public class PointExtcolValEntity  implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pointid;
	private String userid;
	public String col1;
	public String col2;
	public String col3;
	public String col4;
	public String col5;
	public String col6;
	public String col7;
	public String col8;
	public String col9;
	public String col10;
	
	private PointEntity pointEntity;
	
	
	
	@OneToOne(cascade=CascadeType.REFRESH,optional=false)
	@JoinColumn(name = "pointid",referencedColumnName="id", unique = true)
	public PointEntity getPointEntity() {
		return pointEntity;
	}


	public void setPointEntity(PointEntity pointEntity) {
		this.pointEntity = pointEntity;
	}


	public PointExtcolValEntity() {
	}


	public PointExtcolValEntity(
			String pointid,
			String userid, String col1, String col2,
			String col3, String col4, String col5, String col6, String col7,
			String col8, String col9, String col10) {
		this.pointid=pointid;
		this.userid=userid;
		this.col1 = col1;
		this.col2 = col2;
		this.col3 = col3;
		this.col4 = col4;
		this.col5 = col5;
		this.col6 = col6;
		this.col7 = col7;
		this.col8 = col8;
		this.col9 = col9;
		this.col10 = col10;
	}


	@Id
	@Column(name = "pointid", length = 32)
	public String getPointid() {
		return pointid;
	}


	public void setPointid(String pointid) {
		this.pointid = pointid;
	}


	@Column(name = "userid", length = 32)
	public String getUserid() {
		return userid;
	}


	public void setUserid(String userid) {
		this.userid = userid;
	}


	@Column(name = "col1", length = 100)
	public String getCol1() {
		return col1;
	}


	public void setCol1(String col1) {
		this.col1 = col1;
	}

	@Column(name = "col2", length = 100)
	public String getCol2() {
		return col2;
	}


	public void setCol2(String col2) {
		this.col2 = col2;
	}

	@Column(name = "col3", length = 100)
	public String getCol3() {
		return col3;
	}


	public void setCol3(String col3) {
		this.col3 = col3;
	}

	@Column(name = "col4", length = 100)
	public String getCol4() {
		return col4;
	}


	public void setCol4(String col4) {
		this.col4 = col4;
	}

	@Column(name = "col5", length = 100)
	public String getCol5() {
		return col5;
	}


	public void setCol5(String col5) {
		this.col5 = col5;
	}

	@Column(name = "col6", length = 100)
	public String getCol6() {
		return col6;
	}


	public void setCol6(String col6) {
		this.col6 = col6;
	}

	@Column(name = "col7", length = 100)
	public String getCol7() {
		return col7;
	}


	public void setCol7(String col7) {
		this.col7 = col7;
	}

	@Column(name = "col8", length = 100)
	public String getCol8() {
		return col8;
	}


	public void setCol8(String col8) {
		this.col8 = col8;
	}

	@Column(name = "col9", length = 100)
	public String getCol9() {
		return col9;
	}


	public void setCol9(String col9) {
		this.col9 = col9;
	}

	@Column(name = "col10", length = 100)
	public String getCol10() {
		return col10;
	}


	public void setCol10(String col10) {
		this.col10 = col10;
	}
	
	
	/*private PointEntity pointEntity;



	@OneToOne(mappedBy = "pointExtcolValEntity")
	@JoinColumn(name="pointid")
	//@OneToOne(cascade=CascadeType.ALL)
	//@JoinColumn(name="pointid")
	public PointEntity getPointEntity() {
		return pointEntity;
	}

	public void setPointEntity(PointEntity pointEntity) {
		this.pointEntity = pointEntity;
	}*/
	
	
}
