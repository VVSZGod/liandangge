package com.jiamian.translation.controller

import com.alibaba.fastjson.JSONObject
import com.jiamian.translation.service.ModelApiService
import io.swagger.annotations.Api
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
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
     open fun pageModel(@RequestParam(value = "page") page: Int, @RequestParam(value = "limit") pageSize: Int): String {
        return JSONObject.toJSONString(modelApiService.pageModelApi(pageSize, page))
    }
}