package com.jiamian.translation.job;

import com.jiamian.translation.service.ModelApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author DingGuangHui
 * @date 2023/3/29
 */
@Component
@Slf4j
public class UploadJsonJob {
	@Autowired
	private ModelApiService modelApiService;

	@Scheduled(cron = "0 0 23 * * ?")
	public void uploadJsonJob(){
		log.info("=== uploadJson start ====");
//		modelApiService.uploadQiniuModelsJson();
		log.info("=== uploadJson end ====");
	}
}
