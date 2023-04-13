package com.jiamian.translation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "qiniu", name = "bucket")
public class QiNiuConfig {
	@Value("${qiniu.access_key}")
	private String accessKey;

	@Value("${qiniu.bucket}")
	private String bucket;

	@Value("${qiniu.secret_key}")
	private String secretKey;

	@Value("${qiniu.domain}")
	private String domain;

	@Value("${img.zip.ratio}")
	private String ZIP_RATIO;

	@Value("${qiniu.video.waterMarkUrl}")
	private String videoWMUrl;

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getZIP_RATIO() {
		return ZIP_RATIO;
	}

	public void setZIP_RATIO(String ZIP_RATIO) {
		this.ZIP_RATIO = ZIP_RATIO;
	}

	public String getVideoWMUrl() {
		return videoWMUrl;
	}

	public void setVideoWMUrl(String videoWMUrl) {
		this.videoWMUrl = videoWMUrl;
	}
}
