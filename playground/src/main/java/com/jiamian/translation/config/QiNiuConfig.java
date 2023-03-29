package com.jiamian.translation.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "qiniu", name = "bucket")
public class QiNiuConfig {
	@Value("${qiniu.access_key:4F9IoNkd6dYFYefon03WN-CyI7KMzd8mPJcL6M3p}")
	private String accessKey;

	@Value("${qiniu.bucket:ygjy-dev}")
	private String bucket;

	@Value("${qiniu.secret_key:Nb2hZ2sxtXPT28B0JttJUmzoqLJ4jig7jwZdyxiM}")
	private String secretKey;

	@Value("${qiniu.domain:fpcache.fotoplace.cc}")
	private String domain;

	@Value("${img.zip.ratio:0.25}")
	private String ZIP_RATIO;

	@Value("${qiniu.video.waterMarkUrl:http://78re52.com1.z0.glb.clouddn.com/resource%2Flogo.jpg}")
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
