package com.jiamian.translation.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by chenjunyu on 2019/3/21.
 */
@Slf4j
public class MyHttpMessageConverter
		extends MappingJackson2HttpMessageConverter {

	@Override
	protected void writeInternal(Object obj, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		super.writeInternal(obj, outputMessage);
		HttpHeaders headers = outputMessage.getHeaders();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
		log.info(headers + "\n" + obj);
	}
}
