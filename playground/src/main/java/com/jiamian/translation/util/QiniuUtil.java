package com.jiamian.translation.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;

/**
 * @author DingGuangHui
 * @date 2023/3/29
 */
public class QiniuUtil {

	/**
	 * {"size":1061127,"format":"png","width":768,"height":1024,"colorModel":"rgba"}
	 *
	 * @param url
	 * @return
	 */
	public static JSONObject getImgInfo(String url) {
		HttpResponse resp = HttpRequest.get(url + "?imageInfo").execute();
		if (resp.isOk()) {
			return JSONObject.parseObject(resp.body());
		}
		return new JSONObject();
	}
}
