package com.chaosting.geoforce.saas.bak.exception;

public class ParameterException extends BaseException {

	private static final long serialVersionUID = -1594457436407050224L;

	public ParameterException(String message) {
		super(message, 10001, "invalid params");
	}
	
}
