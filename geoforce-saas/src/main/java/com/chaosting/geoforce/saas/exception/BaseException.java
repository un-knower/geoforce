package com.chaosting.geoforce.saas.exception;

public class BaseException extends RuntimeException {

	private static final long serialVersionUID = 5945327811334339788L;

	private Integer status;

	private String info;

	public BaseException(String message, Integer status, String info) {
		super(message);
		this.status = status;
		this.info = info;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
