package com.jiamian.translation.config;

import java.util.List;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jiamian.translation.annotation.support.LoginUserHandlerMethodArgumentResolver;

@Configuration
public class WxWebMvcConfiguration implements WebMvcConfigurer {
	@Override
	public void addArgumentResolvers(
			List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new LoginUserHandlerMethodArgumentResolver());
	}

	@Bean
	public HttpMessageConverters customConverters() {
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MyHttpMessageConverter();
		mappingJackson2HttpMessageConverter.setPrettyPrint(true);
		return new HttpMessageConverters(mappingJackson2HttpMessageConverter);
	}
}
