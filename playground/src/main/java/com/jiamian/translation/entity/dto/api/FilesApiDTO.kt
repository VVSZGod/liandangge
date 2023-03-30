package com.jiamian.translation.entity.dto.api

import com.google.common.collect.Maps
import lombok.Data

/**
 * @author DingGuangHui
 * @date 2023/3/28
 */
class FilesApiDTO {
    var name = ""
    var id = 0
    var sizeKB = 0.0
    var type = ""
    var format: String? = null
    var pickleScanResult: String? = null
    var pickleScanMessage: String? = null
    var virusScanResult: String? = null
    var scannedAt: String? = null
    var downloadUrl: String? = null
    var primary = true
    var hashes: Map<*, *> = Maps.newHashMap<Any, Any>()
}