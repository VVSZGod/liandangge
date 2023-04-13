package com.jiamian.translation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author DingGuangHui
 * @date 2023/4/6
 */
@SpringBootApplication
@EnableScheduling
public class CivitaiStarter {

	private static final Logger logger = LoggerFactory
			.getLogger(CivitaiStarter.class);

	public static void main(String[] args) {
		SpringApplication.run(CivitaiStarter.class, args);

		logger.info("---------------------CivitaiStarter start----------------------");

	}
}
