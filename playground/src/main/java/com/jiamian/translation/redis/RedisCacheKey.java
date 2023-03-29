package com.jiamian.translation.redis;

public class RedisCacheKey {

    /**
     * 订单重复点击上锁
     */
    public static String ORDER_REPEAT_LOCK = "order:repeatLock:%s%s:%s";

    /**
     * 自增用户id
     */
    public static final String TRANSLATION_USER_INCR_KEY = "translation:user:incr:";

    /**
     * 模型已经上传数量
     */
    public static String MODEL_TOTAL_UPLOADED_COUNT_PREFIX = "model:total:count:upload:";

    /**
     * 模型总数量
     */
    public static String MODEL_TOTAL_COUNT_PREFIX = "model:total:count:";


    /**
     * 模型类型
     */
    public static String MODEL_TYPE_PREFIX = "model:type:";
}
