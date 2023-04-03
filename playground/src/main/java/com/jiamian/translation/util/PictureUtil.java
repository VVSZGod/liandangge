package com.jiamian.translation.util;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

import cn.hutool.http.HttpUtil;

/**
 * @author Ding
 * @date 2022/12/16
 */
public class PictureUtil {
	private static Logger logger = LoggerFactory.getLogger(PictureUtil.class);

	/**
	 * 获取电视时长， 向下取整的秒数
	 *
	 * @param url
	 * @return
	 */
	public static JSONObject getImageInfo(String url) {
		if (StringUtils.isNotEmpty(url)) {
			try {
				String s = HttpUtil.get(url + "?imageInfo");
				return JSONObject.parseObject(s);
			} catch (Exception e) {
				logger.error("获取图片大小异常，剧集url：{}", url, e);
				return null;
			}
		} else {
			return null;
		}
	}
}
