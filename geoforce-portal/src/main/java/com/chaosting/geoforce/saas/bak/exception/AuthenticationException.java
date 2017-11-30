package com.chaosting.geoforce.saas.bak.exception;

public class AuthenticationException extends BaseException {

	private static final long serialVersionUID = 6438515745556619243L;

	public AuthenticationException(String message) {
		super(message, 10002, "Authentication failed");
	}

}
