package com.jiamian.translation.entity.dto.api

/**
 * @author  DingGuangHui
 * @date 2023/3/30
 */
data class MetaDataApiDTO(var totalItems: Long, var currentPage: Int, var pageSize: Int, var totalPages: Int) {

    constructor() : this(0, 0, 0, 0) {

    }
}