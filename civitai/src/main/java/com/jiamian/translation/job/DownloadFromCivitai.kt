package com.jiamian.translation.job

import cn.hutool.core.collection.CollectionUtil
import com.jiamian.translation.dao.repository.ModelRepository
import khttp.get
import org.json.JSONArray
import org.springframework.stereotype.Component

/**
 * @author DingGuangHui
 * @date 2023/4/3
 */
@Component
class DownloadFromCivitai {

    lateinit var modelRepository: ModelRepository

    var CIVITAI_PAGE_URL = "https://civitai.com/api/v1/models?page=%s&limit=100&period=Day"

    fun downloadFromCivitai() {
        var page = 1
        var items = JSONArray()
        do {
            val resp = get(CIVITAI_PAGE_URL.format(page)).jsonObject
            items = resp.getJSONArray("items")
            if (CollectionUtil.isNotEmpty(items)) {
                page += 1
                for (item in items) {
                    var needDownload = false
                    // 判断是否需要下载, 检查


                    if (needDownload) {

                    }

                }
            }


        } while (CollectionUtil.isNotEmpty(items))


    }
}