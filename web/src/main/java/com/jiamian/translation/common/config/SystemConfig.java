package com.jiamian.translation.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SystemConfig {

	private static SystemConfig config;

	@PostConstruct
	public void init() {
		config = this;
	}

	@Value("${sys.env.isPro:false}")
	private boolean isPro;
	@Value("${sys.token.secret}")
	private String secret;


	public static String getSecret() {
		return config.secret;
	}

	public static boolean isPro() {
		return config.isPro;
	}
}
