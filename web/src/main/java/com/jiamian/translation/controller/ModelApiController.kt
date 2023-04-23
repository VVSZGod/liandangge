package com.jiamian.translation.controller

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.jiamian.translation.exception.BOException
import com.jiamian.translation.service.ModelApiService
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @author  DingGuangHui
 * @date 2023/3/30
 */
@RestController
@RequestMapping("/api")

@Api(tags = ["公开api"])
open class ModelApiController {

    @Autowired
    private lateinit var modelApiService: ModelApiService;


    @GetMapping("/models")
    open fun pageModel(@RequestParam(value = "page", defaultValue = "1", required = false) page: Int, @RequestParam
    (value = "limit", defaultValue = "20", required = false)
    pageSize: Int):
            JSONObject {
        if (pageSize > 100 || page < 1) {
            throw BOException("max pageSize limit 100 or page must greater than 0")
        }
        val pageModelApi = modelApiService.pageModelApi(pageSize, page)
        return JSONObject.parseObject(JSON.toJSONString(pageModelApi))
    }
}