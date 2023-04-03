package com.jiamian.translation.util;

import cn.hutool.core.codec.Base64;
import com.jiamian.translation.PlaygroundStarter;
import com.jiamian.translation.common.exception.BOException;
import com.jiamian.translation.common.exception.ErrorMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Description: AES加密解密方法
 */

public class AESUtils {
	private static final Logger logger = LoggerFactory
			.getLogger(PlaygroundStarter.class);
	private static final String charset = "UTF-8";

	private static final String password = "d48ef57027ab4a2991ok6d37dd1278ad";

	/**
	 * Description: AES解密 d48ef57027ab4a2991ok6d37dd1278ad
	 */
	public static String decrypt(String sSrc) {
		try {
			if (StringUtils.isEmpty(sSrc)) {
				return "";
			}
			byte[] raw = password.getBytes(charset);
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] encrypted1 = (new BASE64Decoder()).decodeBuffer(sSrc);
			byte[] original = cipher.doFinal(encrypted1);
			String rs = new String(original, charset);
			logger.info("[AES解密:输入{},输出{}]", sSrc, rs);
			return rs;
		} catch (Exception e) {
			throw new BOException(ErrorMsg.ENCODE_ERROR);
		}

	}

	public static void main(String[] args) {
		System.out.println(encrypt(""));
		// decrypt("==");
	}

	/**
	 * AES加密字符串
	 *
	 * @param content
	 *            需要被加密的字符串 加密需要的密码
	 * @return 密文
	 */
	public static String encrypt(String content) {
		if (StringUtils.isNotEmpty(content)) {
			try {
				SecretKeySpec aes = new SecretKeySpec(password.getBytes(),
						"AES");
				Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
				cipher.init(Cipher.ENCRYPT_MODE, aes);
				// 将加密并编码后的内容解码成字节数组
				String rs = Base64.encode(cipher.doFinal(content.getBytes()));
				logger.info("[AES加密:输入{},输出{}]", content, rs);
				return rs;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}
