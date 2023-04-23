package com.jiamian.translation.entity.dto.api

import com.google.common.collect.Lists

/**
 * @author DingGuangHui
 * @date 2023/3/28
 */

data class ModelApiDTO(var id: Long) {

    var name: String? = ""
    var description: String? = ""
    var type: String? = ""
    var poi: Boolean? = true
    var nsfw: Boolean? = false
    var allowCommercialUse: Boolean? = false
    var allowNoCredit: Boolean? = false
    var allowDerivatives: Boolean? = true
    var allowDifferentLicense: Boolean? = false
    var stats = StatusApiDTO()
    var creator = CreatorApiDTO()
    var tags: List<String> = Lists.newArrayList()
    var modelVersions: List<ModelVersionsApiDTO> = Lists.newArrayList()
}