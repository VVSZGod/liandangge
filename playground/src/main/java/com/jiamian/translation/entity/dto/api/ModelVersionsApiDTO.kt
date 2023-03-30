package com.jiamian.translation.entity.dto.api

import com.google.common.collect.Lists
import lombok.Data
import java.time.LocalDateTime

/**
 * @author DingGuangHui
 * @date 2023/3/28
 */
@Data
class ModelVersionsApiDTO {
    var id: Long = 0
    var modelId: Long = 0
    var name: String? = null
    var createAt: LocalDateTime? = null
    var updatedAt: LocalDateTime? = null
    var trainedWords: List<String> = Lists.newArrayList()
    var baseModel: String? = null
    var earlyAccessTimeFrame = 0
    var description = ""
    var downloadUrl = ""
    var files: List<FilesApiDTO>? = null
    var images: List<ImagesApiDTO>? = null
}