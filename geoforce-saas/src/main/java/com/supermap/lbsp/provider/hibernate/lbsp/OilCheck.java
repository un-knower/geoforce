package com.supermap.lbsp.provider.hibernate.lbsp;


/**
 * CfgOilCheck entity. @author MyEclipse Persistence Tools
 */

public class OilCheck implements java.io.Serializable {

	// Fields

	private String id;
	private String carid;
	private Integer level;
	private Float gpsOil;
	private Float realOil;

	// Constructors

	/** default constructor */
	public OilCheck() {
	}

	/** minimal constructor */
	public OilCheck(String id) {
		this.id = id;
	}

	/** full constructor */
	public OilCheck(String id, String carid, Integer level, Float gpsOil,
			Float realOil) {
		this.id = id;
		this.carid = carid;
		this.level = level;
		this.gpsOil = gpsOil;
		this.realOil = realOil;
	}

	// Property accessors
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public String getCarid() {
		return this.carid;
	}

	public void setCarid(String carid) {
		this.carid = carid;
	}

	
	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	
	public Float getGpsOil() {
		return this.gpsOil;
	}

	public void setGpsOil(Float gpsOil) {
		this.gpsOil = gpsOil;
	}

	
	public Float getRealOil() {
		return this.realOil;
	}

	public void setRealOil(Float realOil) {
		this.realOil = realOil;
	}

}