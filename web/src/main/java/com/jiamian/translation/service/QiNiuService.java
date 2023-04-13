package com.jiamian.translation.service;

import com.jiamian.translation.exception.BOException;
import com.jiamian.translation.config.QiNiuConfig;
import com.qiniu.cdn.CdnManager;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

@Component
@ConditionalOnBean(QiNiuConfig.class)
@Slf4j
public class QiNiuService {

	private static final Logger logger = LoggerFactory
			.getLogger(QiNiuService.class);
	@Autowired
	private QiNiuConfig qiNiuConfig;

	public void uploadFile(InputStream inputStream, String fileKey) {
		Configuration cfg = new Configuration(Region.autoRegion());
		UploadManager uploadManager = new UploadManager(cfg);
		Auth auth = Auth.create(qiNiuConfig.getAccessKey(),
				qiNiuConfig.getSecretKey());
		String upToken = auth.uploadToken(qiNiuConfig.getBucket());

		try {
			Response uploadRes = uploadManager.put(inputStream, fileKey,
					upToken, null, null);
			logger.info("==== 七牛上传结果:{} ====", uploadRes);
		} catch (Exception ex) {
			throw new BOException(ex.getMessage());
		}
	}

	public String getEffectiveUrl(String fileName, long expireTimeStamp) {
		//链接过期时间
		long deadline = System.currentTimeMillis() / 1000 + expireTimeStamp;
		//签名密钥，从后台域名属性中获取
		String encryptKey = "xxx";
		String signedUrl = "";
		try {
			signedUrl = CdnManager.createTimestampAntiLeechUrl(qiNiuConfig.getDomain(), fileName,
					null, encryptKey, deadline);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return signedUrl;
	}

	public String uploadToken(String fileName) {
		Auth auth = Auth.create(qiNiuConfig.getAccessKey(),
				qiNiuConfig.getSecretKey());
		return auth.uploadToken(qiNiuConfig.getBucket(), fileName);
	}

	public String upload(InputStream inputStream,String fileKey, String extension) {

		this.uploadFile(inputStream, fileKey);

		return this.getHttpsFullResourceName(fileKey);
	}

	public String getHttpsFullResourceName(String resourceName) {
		String dm = qiNiuConfig.getDomain();
		if (StringUtils.isNotEmpty(dm)
				&& StringUtils.isNotEmpty(resourceName)) {
			if (resourceName.startsWith("http://")) {
				return resourceName;
			}
			if (resourceName.startsWith("https://")) {
				return resourceName;
			}
			if (!resourceName.startsWith("/")) {
				resourceName = "/" + resourceName;
			}
			if (!dm.startsWith("http://") && !dm.startsWith("https://")) {
				dm = "https://" + dm;
			}
			return dm + resourceName;
		}
		return "";
	}

	public String getDomain() {
		return qiNiuConfig.getDomain();
	}

	public static void main(String[] args) {
		String host = "http://video.example.com";
		String fileName = "基本概括.mp4";

//查询参数
		StringMap queryStringMap = new StringMap();
		queryStringMap.put("name", "七牛");
		queryStringMap.put("year", 2017);
		queryStringMap.put("年龄", 28);

//链接过期时间
		long deadline = System.currentTimeMillis() / 1000 + 3600;

//签名密钥，从后台域名属性中获取
		String encryptKey = "xxx";
		String signedUrl;
		try {
			signedUrl = CdnManager.createTimestampAntiLeechUrl(host, fileName,
					queryStringMap, encryptKey, deadline);
			System.out.println(signedUrl);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
