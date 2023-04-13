package com.jiamian.translation.entity.dto.api

import com.google.common.collect.Lists
import lombok.Data

/**
 * @author DingGuangHui
 * @date 2023/3/28
 */
@Data
class ImagesApiDTO {
    var url = ""
    var nsfw = false
    var width = 0
    var height = 0
    var hash = ""
    var userId = 0
    var generationProcess = ""
    var needsReview = false
    var scannedAt = ""
    var tags: List<String> = Lists.newArrayList()
    var meta: MetaApiDTO? = null
}