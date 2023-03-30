package com.jiamian.translation.common.service;

import com.alibaba.fastjson.JSONObject;
import com.jiamian.translation.common.config.LxtConfig;
import com.jiamian.translation.common.entity.dto.ShortMessage;
import com.jiamian.translation.util.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.Map;
import java.util.TreeMap;

@Component
@ConditionalOnBean(LxtConfig.class)
@Slf4j
public class LxtService {

	@Autowired
	private LxtConfig lxtConfig;

	public boolean sendMessage(ShortMessage shortMessage) {
		long timestamp = System.currentTimeMillis();
		String sign = "action=sendtemplate&username=" + lxtConfig.getUn()
				+ "&password=" + getMD5String(lxtConfig.getPw()) + "&token="
				+ lxtConfig.getToken() + "&timestamp=" + timestamp;
		TreeMap<String, String> map = new TreeMap<>();
		map.put("action", "sendtemplate");
		map.put("username", lxtConfig.getUn());
		map.put("password", getMD5String(lxtConfig.getPw()));
		map.put("token", lxtConfig.getToken());
		map.put("templateid", lxtConfig.getTemplateid());
		map.put("timestamp", String.valueOf(timestamp));
		map.put("rece", "json");
		map.put("param",
				shortMessage.getMobileNo() + "|" + shortMessage.getContent());
		map.put("sign", getMD5String(sign));
		JSONObject result = OkHttpUtil.post(lxtConfig.getBASE_URL(), map);
		log.info("短信发送结果==={}", result);
		String code = result.get("code").toString();
		return code.equals("0");
	}

	public static String getMD5String(String rawString) { // 用来计算MD5的函数
		String[] hexArray = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
				"A", "B", "C", "D", "E", "F" };
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(rawString.getBytes());
			byte[] rawBit = md.digest();
			String outputMD5 = " ";
			for (int i = 0; i < 16; i++) {
				outputMD5 = outputMD5 + hexArray[rawBit[i] >>> 4 & 0x0f];
				outputMD5 = outputMD5 + hexArray[rawBit[i] & 0x0f];
			}
			return outputMD5.trim();
		} catch (Exception e) {
			System.out.println("计算MD5值发生错误");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成秘钥
	 *
	 * @param tm
	 * @param key
	 * @return
	 */
	public static String createSign(TreeMap<String, String> tm, String key) {
		StringBuffer buf = new StringBuffer(key);
		for (Map.Entry<String, String> en : tm.entrySet()) {
			String name = en.getKey();
			String value = en.getValue();
			if (!"sign".equals(name) && !"param".equals(name)
					&& !"rece".equals(name) && value != null
					&& value.length() > 0 && !"null".equals(value)) {
				buf.append(name).append('=').append(value).append('&');
			}
		}
		String _buf = buf.toString();
		return _buf.substring(0, _buf.length() - 1);
	}

	/**
	 * 将文件转成base64 字符串
	 * 
	 * @return *
	 * @throws Exception
	 */

	public static String encodeBase64File(String path) throws Exception {
		File file = new File(path);
		;
		FileInputStream inputFile = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		inputFile.read(buffer);
		inputFile.close();
		return "";
	}
}
