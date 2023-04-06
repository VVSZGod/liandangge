package com.jiamian.translation.util;

import javax.net.ssl.X509TrustManager;

/**
 * @description:
 * @author: alonwang
 * @create: 2019-10-31 17:01
 **/
public class TrustAnyX509TrustManager implements X509TrustManager {
	@Override
	public void checkClientTrusted(
			java.security.cert.X509Certificate[] x509Certificates, String s)
			throws java.security.cert.CertificateException {
	}

	@Override
	public void checkServerTrusted(
			java.security.cert.X509Certificate[] x509Certificates, String s)
			throws java.security.cert.CertificateException {
	}

	// 不能为null
	@Override
	public java.security.cert.X509Certificate[] getAcceptedIssuers() {
		return new java.security.cert.X509Certificate[] {};
	}
}
