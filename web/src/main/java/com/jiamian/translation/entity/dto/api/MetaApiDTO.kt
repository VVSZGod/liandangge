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
    var Size = ""
    var seed: Long = 0

    @JSONField(name = "Model")
    var Model = ""
    var steps = 0
    var prompt = ""
    var sampler = ""
    var cfgScale = 0.0
    var resources: List<*> = Lists.newArrayList<Any>()
    var negativePrompt = ""
    var needsReview = false
    var scannedAt = ""
}