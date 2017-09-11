package com.supermap.egisp.addressmatch.beans;

import java.io.Serializable;

/**
 * 
 * <p>Title: POIQueryFilterInfo</p>
 * Description:		POI 搜索的过滤条件信息
 *
 * @author Huasong Huang
 * CreateTime: 2015-8-19 上午11:09:00
 */
public class POIQueryFilterInfo implements Serializable{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 查询类型：NAME:关键词查询，CLASS:分类查询 
	 */
	private String type;
	/**
	 * 关键字：在示例中：name like '%超市%' ...  在解析结束之后，将提取出：likeKeyword=超市
	 */
	private String likeKeyword;
	
	/**
	 * 分类代码
	 */
	private String classWord;
	
	/**
	 * Admincode左值
	 */
	private int leftAdmincode = -1;
	/**
	 * Admincode右值
	 */
	private int rightAdmincode = -1;
	/**
	 * 矩形范围的左下角X坐标
	 */
	private double leftDownX = -1;
	/**
	 * 矩形范围左下角Y坐标
	 */
	private double leftDownY = -1;
	/**
	 * 矩形范围右上角坐标X
	 */
	private double rightUpX = -1;
	/**
	 * 矩形范围右上角坐标Y
	 */
	private double rightUpY = -1;
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLikeKeyword() {
		return likeKeyword;
	}
	public void setLikeKeyword(String likeKeyword) {
		this.likeKeyword = likeKeyword;
	}
	public int getLeftAdmincode() {
		return leftAdmincode;
	}
	public void setLeftAdmincode(int leftAdmincode) {
		this.leftAdmincode = leftAdmincode;
	}
	public int getRightAdmincode() {
		return rightAdmincode;
	}
	public void setRightAdmincode(int rightAdmincode) {
		this.rightAdmincode = rightAdmincode;
	}
	public double getLeftDownX() {
		return leftDownX;
	}
	public void setLeftDownX(double leftDownX) {
		this.leftDownX = leftDownX;
	}
	public double getLeftDownY() {
		return leftDownY;
	}
	public void setLeftDownY(double leftDownY) {
		this.leftDownY = leftDownY;
	}
	public double getRightUpX() {
		return rightUpX;
	}
	public void setRightUpX(double rightUpX) {
		this.rightUpX = rightUpX;
	}
	public double getRightUpY() {
		return rightUpY;
	}
	public void setRightUpY(double rightUpY) {
		this.rightUpY = rightUpY;
	}
	public String getClassWord() {
		return classWord;
	}
	public void setClassWord(String classWord) {
		this.classWord = classWord;
	}
}
