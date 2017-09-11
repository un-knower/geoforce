package com.supermap.lbsp.provider.bean;
/**
 * 里程统计bean
* @ClassName: MileReportBean
* @author WangShuang
* @date 2013-4-18 上午09:46:48
 */
public class MileReport implements java.io.Serializable{

	/**
	 * 
	 */
	private String carId;
	private String license;
	private String deptCode;
	private String deptId;
	private String deptName;
	private String startGpsTime;
	private String endGpsTime;
	private Double startMile;
	private Double endMile;
	private Double diffMile;
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getStartGpsTime() {
		return startGpsTime;
	}
	public void setStartGpsTime(String startGpsTime) {
		this.startGpsTime = startGpsTime;
	}
	public String getEndGpsTime() {
		return endGpsTime;
	}
	public void setEndGpsTime(String endGpsTime) {
		this.endGpsTime = endGpsTime;
	}
	public Double getStartMile() {
		return startMile;
	}
	public void setStartMile(Double startMile) {
		this.startMile = startMile;
	}
	public Double getEndMile() {
		return endMile;
	}
	public void setEndMile(Double endMile) {
		this.endMile = endMile;
	}
	public Double getDiffMile() {
		return diffMile;
	}
	public void setDiffMile(Double diffMile) {
		this.diffMile = diffMile;
	}
	
	
}
