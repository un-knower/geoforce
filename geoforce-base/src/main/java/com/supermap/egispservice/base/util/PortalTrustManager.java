package com.supermap.egispservice.base.util;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

public class PortalTrustManager implements X509TrustManager {

	private static final Logger LOGGER = Logger.getLogger(PortalTrustManager.class);
	@Override
	public void checkClientTrusted(X509Certificate[] certificates, String arg1) throws CertificateException {
		LOGGER.info("## check client trusted...");
	}

	@Override
	public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		LOGGER.info("## check server trusted...");
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[]{};
	}

	
}
