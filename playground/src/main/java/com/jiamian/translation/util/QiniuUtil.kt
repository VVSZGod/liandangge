package com.jiamian.translation.util

import cn.hutool.http.HttpRequest
import com.alibaba.fastjson.JSONObject

/**
 * @author DingGuangHui
 * @date 2023/3/29
 */
object QiniuUtil {
    /**
     * {"size":1061127,"format":"png","width":768,"height":1024,"colorModel":"rgba"}
     *
     * @param url
     * @return
     */
	@JvmStatic
	fun getImgInfo(url: String): JSONObject {
        val resp = HttpRequest.get("$url?imageInfo").execute()
        return if (resp.isOk) {
            JSONObject.parseObject(resp.body())
        } else JSONObject()
    }
}