package com.jiamian.translation.entity;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.jiamian.translation.exception.ErrorCodeEnum;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JsonResult<T extends Object> implements Serializable {
    /**
     * 成功码
     */
    public static final Integer SUCCESS_CODE = 1;

    /**
     * 结果码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message = "";

    /**
     * 数据
     */
    private T data;

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(this.code);
    }

    public JsonResult() {
    }

    private JsonResult(int code) {
        this.code = code;
    }

    public static JsonResult succResult() {
        return new JsonResult(SUCCESS_CODE);
    }

    public static <T> JsonResult<T> succResult(T data) {
        JsonResult jsonResult = new JsonResult(SUCCESS_CODE);
        jsonResult.setData(data);
        return jsonResult;
    }

    public static JsonResult succResult(String key, Object value) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put(key, value);
        return succResult(dataMap);
    }

    public static JsonResult succResult(Map<String, Object> dataMap) {
        JsonResult jsonResult = new JsonResult(SUCCESS_CODE);
        jsonResult.setData(dataMap);
        return jsonResult;
    }

    public static JsonResult errorResult() {
        return new JsonResult(ErrorCodeEnum.ERROR_UNKNOW.getCode());
    }

    public static JsonResult errorResult(String message) {
        JsonResult jsonResult = new JsonResult(ErrorCodeEnum.ERROR_UNKNOW.getCode());
        jsonResult.setMessage(message);
        return jsonResult;
    }

    public static JsonResult errorResult(String message, ErrorCodeEnum errorCodeEnum) {
        JsonResult jsonResult = new JsonResult(errorCodeEnum.getCode());
        jsonResult.setMessage(message);
        return jsonResult;
    }

    public static JsonResult errorResult(ErrorCodeEnum errorCodeEnum) {
        JsonResult jsonResult = new JsonResult(errorCodeEnum.getCode());
        jsonResult.setMessage(errorCodeEnum.getMsg());
        jsonResult.setCode(errorCodeEnum.getCode());
        return jsonResult;
    }

    public static JsonResult errorResult(Map<String, Object> dataMap, ErrorCodeEnum errorCodeEnum) {
        JsonResult jsonResult = new JsonResult(SUCCESS_CODE);
        jsonResult.setData(dataMap);
        jsonResult.setMessage(errorCodeEnum.getMsg());
        jsonResult.setCode(errorCodeEnum.getCode());
        return jsonResult;
    }

    public static JsonResult errorResult(Map<String, Object> dataMap, String message, ErrorCodeEnum errorCodeEnum) {
        JsonResult jsonResult = new JsonResult(SUCCESS_CODE);
        jsonResult.setData(dataMap);
        jsonResult.setMessage(message);
        jsonResult.setCode(errorCodeEnum.getCode());
        return jsonResult;
    }

    public Map<String, Object> fetchDataMap() {
        return (Map<String, Object>) data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

    // *************************************************************************//
    // ***************************** 以下为Get/Set方法 **************************//
    // *************************************************************************//

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}