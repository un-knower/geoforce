package com.supermap.egispweb.common;
/**
 * 
* ClassName：AjaxResult   
* 类描述：  ajax返回前台信息bean
* 操作人：wangshuang   
* 操作时间：2014-9-23 下午03:04:29     
* @version 1.0
 */
public class AjaxResult {

	private Short status;//状态 0失败 1成功
	private String code;//错误编码 status为0时有效
	private Object info;//详情 status=0错误描述信息 status=1返回结果值
	public AjaxResult(){
		
	}
	public AjaxResult(Short status,Object info){
		this.status = status;
		this.info = info;
	}
	public AjaxResult(Short status,String code,Object info){
		this.status = status;
		this.code = code;
		this.info = info;
	}
	public Short getStatus() {
		return status;
	}
	public void setStatus(Short status) {
		this.status = status;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Object getInfo() {
		return info;
	}
	public void setInfo(Object info) {
		this.info = info;
	}
	
	
}
