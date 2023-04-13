/*
 * @(#) SignatureUtils.java 2016年2月2日
 *
 * Copyright 2010 NetEase.com, Inc. All rights reserved.
 */
package com.jiamian.translation.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;

/**
 * 生成及验证签名信息工具类
 *
 * @author hzgaomin
 * @version 2016年2月2日
 */
public class SignatureUtils {
	private static final Logger logger = LoggerFactory
			.getLogger(SignatureUtils.class);

	/**
	 * 生成签名信息
	 *
	 * @param secretKey
	 *            产品私钥
	 * @param params
	 *            接口请求参数名和参数值map，不包括signature参数名
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getSignature(String secretKey,
			Map<String, String> params) {
		// 1. 参数名按照ASCII码表升序排序
		try {
			String[] keys = params.keySet().toArray(new String[0]);
			Arrays.sort(keys);

			// 2. 按照排序拼接参数名与参数值
			StringBuffer paramBuffer = new StringBuffer();
			for (String key : keys) {
				paramBuffer.append(key).append(params.get(key));
			}
			// 3. 将secretKey拼接到最后
			paramBuffer.append(secretKey);

			// 4.
			// MD5是128位长度的摘要算法，用16进制表示，一个十六进制的字符能表示4个位，所以签名后的字符串长度固定为32个十六进制字符。
			return DigestUtils.md5Hex(paramBuffer.toString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// e.printStackTrace();
			logger.error("=== 获取签名信息异常 ,{}===", e.toString());
			return "";
		}
	}


	public static String getSign(Map<String, String> requestMap,
			String appKey) {
		return hmacSHA256Encrypt(requestMap2Str(requestMap), appKey);
	}

	private static String hmacSHA256Encrypt(String encryptText,
			String encryptKey) {
		byte[] result = null;
		try {
			// 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
			SecretKeySpec signinKey = new SecretKeySpec(
					encryptKey.getBytes("UTF-8"), "HmacSHA256");
			// 生成一个指定 Mac 算法 的 Mac 对象
			Mac mac = Mac.getInstance("HmacSHA256");
			// 用给定密钥初始化 Mac 对象
			mac.init(signinKey);
			// 完成 Mac 操作
			byte[] rawHmac = mac.doFinal(encryptText.getBytes("UTF-8"));
			return ByteFormat.bytesToHexString(rawHmac);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String requestMap2Str(Map<String, String> requestMap) {
		String[] keys = requestMap.keySet().toArray(new String[0]);
		Arrays.sort(keys);
		StringBuilder stringBuilder = new StringBuilder();
		for (String str : keys) {
			if (!str.equals("sign")) {
				stringBuilder.append(str).append(requestMap.get(str));
			}
		}
		return stringBuilder.toString();
	}
}
