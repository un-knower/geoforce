package com.supermap.egispapi.pojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 
 * <p>Title: ResponseCode</p>
 * Description:		响应代码Bean
 *
 * @author Huasong Huang
 * CreateTime: 2015-4-8 下午03:16:34
 */
public class ResponseCode implements Serializable{
	private String code;
	private String info;
	public ResponseCode(){}
	
	public ResponseCode(String code,String info){
		this.code = code;
		this.info = info;
	}
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	
	
	
}
