package com.jiamian.translation.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration()
public class CorsConfig {
	@Bean()
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", buildConfig()); // 4
		FilterRegistrationBean bean = new FilterRegistrationBean(
				new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}

	private CorsConfiguration buildConfig() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("*"); // 1允许任何域名使用
		corsConfiguration.addAllowedHeader("*"); // 2允许任何头
		corsConfiguration.addAllowedMethod("*"); // 3允许任何方法（post、get等）
		return corsConfiguration;
	}
}
