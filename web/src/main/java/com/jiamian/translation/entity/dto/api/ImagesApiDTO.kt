package com.jiamian.translation.entity.dto.api

import com.google.common.collect.Lists
import lombok.Data

/**
 * @author DingGuangHui
 * @date 2023/3/28
 */
@Data
class ImagesApiDTO {
    var url: String? = ""
    var nsfw: Boolean? = false
    var width = 0
    var height = 0
    var hash: String? = ""
    var userId: Long? = 0
    var generationProcess: String? = ""
    var needsReview: Boolean? = false
    var scannedAt: String? = ""
    var tags: List<String> = Lists.newArrayList()
    var meta: MetaApiDTO? = null
}