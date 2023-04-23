package com.jiamian.translation.entity.dto.api

import com.alibaba.fastjson.annotation.JSONField
import com.google.common.collect.Lists
import lombok.Data

/**
 * @author DingGuangHui
 * @date 2023/3/28
 */
@Data
class MetaApiDTO {
    @JSONField(name = "Size")
    var Size: String? = ""
    var seed: Long = 0

    @JSONField(name = "Model")
    var Model: String? = ""
    var steps: Long? = 0
    var prompt: String? = ""
    var sampler: String? = ""
    var cfgScale: Double? = 0.0
    var resources: List<*> = Lists.newArrayList<Any>()
    var negativePrompt: String? = ""
    var needsReview: Boolean? = false
    var scannedAt: String? = ""
}