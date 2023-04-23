package com.jiamian.translation.entity.dto.api

import com.google.common.collect.Maps

/**
 * @author DingGuangHui
 * @date 2023/3/28
 */
class FilesApiDTO {
    var name: String? = ""
    var id: Long? = 0
    var sizeKB: Double? = 0.0
    var type: String? = ""
    var format: String? = null
    var pickleScanResult: String? = null
    var pickleScanMessage: String? = null
    var virusScanResult: String? = null
    var scannedAt: String? = null
    var downloadUrl: String? = null
    var primary: Boolean? = true
    var hashes: Map<*, *> = Maps.newHashMap<Any, Any>()
}