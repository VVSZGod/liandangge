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
	 * 用户关注列表前缀
	 */
	public static String USER_FOLLOW_LIST_PREFIX = "translation:user:followList:";

	/**
	 * 用户关注列表前缀
	 */
	public static String USER_FANS_LIST_PREFIX = "translation:user:fansList:";

	/**
	 * 用户收藏列表前缀
	 */
	public static String USER_COLLECTION_STYLE_LIST_PREFIX = "translation:user:collectionStyleList:";

}
