package com.jiamian.translation.entity.dto.api

import com.google.common.collect.Lists

/**
 * @author DingGuangHui
 * @date 2023/3/28
 */

data class ModelApiDTO(var id: Long) {

     var name = ""
     var description = ""
     var type = ""
     var poi = true
     var nsfw = false
     var allowCommercialUse = false
     var allowNoCredit = false
     var allowDerivatives = true
     var allowDifferentLicense = false
     var stats = StatusApiDTO()
     var creator = CreatorApiDTO()
     var tags: List<String> = Lists.newArrayList()
     var modelVersions: List<ModelVersionsApiDTO> = Lists.newArrayList()
}