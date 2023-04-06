package com.jiamian.translation.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "lxt", name = "account")
public class LxtConfig {
	@Value("${lxt.sms.url}")
	private String BASE_URL;

	@Value("${lxt.account}")
	private String un;

	@Value("${lxt.password}")
	private String pw;

	@Value("${lxt.token}")
	private String token;

	@Value("${lxt.templateid}")
	private String templateid;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTemplateid() {
		return templateid;
	}

	public void setTemplateid(String templateid) {
		this.templateid = templateid;
	}

	public String getBASE_URL() {
		return BASE_URL;
	}

	public void setBASE_URL(String BASE_URL) {
		this.BASE_URL = BASE_URL;
	}

	public String getUn() {
		return un;
	}

	public void setUn(String un) {
		this.un = un;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}
}
