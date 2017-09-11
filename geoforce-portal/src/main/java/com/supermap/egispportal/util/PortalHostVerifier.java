package com.supermap.egispportal.util;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import org.apache.log4j.Logger;

public class PortalHostVerifier implements HostnameVerifier {

	private static final Logger LOGGER = Logger.getLogger(PortalHostVerifier.class);
	
	@Override
	public boolean verify(String hostname, SSLSession session) {
		LOGGER.info("## verify the hostname, hostname : "+hostname);
		return true;
	}

}
