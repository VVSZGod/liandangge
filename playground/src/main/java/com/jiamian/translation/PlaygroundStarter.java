package com.jiamian.translation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author DingGuangHui
 * @date 2022/11/7
 */
@SpringBootApplication
@EnableScheduling
public class PlaygroundStarter {
	private static final Logger logger = LoggerFactory
			.getLogger(PlaygroundStarter.class);

	public static void main(String[] args) {
		SpringApplication.run(PlaygroundStarter.class, args);

		logger.info("---------------------web start----------------------");

	}
}
