package com.jiamian.translation.dao.redis;

import com.jiamian.translation.entity.response.ModelTypeResponse;
import com.jiamian.translation.redis.RedisCacheKey;
import com.jiamian.translation.redis.RedisDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class ModelRedisService {

	private static Logger logger = LoggerFactory
			.getLogger(ModelRedisService.class);

	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private RedisDao redisDao;

	public void setModelUploadCount(int count) {
		redisTemplate.opsForValue()
				.set(RedisCacheKey.MODEL_TOTAL_UPLOADED_COUNT_PREFIX, count);
		redisTemplate.expire(RedisCacheKey.MODEL_TOTAL_UPLOADED_COUNT_PREFIX, 1,
				TimeUnit.HOURS);
	}

	public int getModelUploadCount() {
		Object o = redisTemplate.opsForValue()
				.get(RedisCacheKey.MODEL_TOTAL_UPLOADED_COUNT_PREFIX);
		return o == null ? 0 : Integer.parseInt(o.toString());
	}

	public void setModelTotalCount(int count) {
		redisTemplate.opsForValue().set(RedisCacheKey.MODEL_TOTAL_COUNT_PREFIX,
				count);
		redisTemplate.expire(RedisCacheKey.MODEL_TOTAL_COUNT_PREFIX, 1,
				TimeUnit.HOURS);
	}

	public int getModelTotalCount() {
		Object o = redisTemplate.opsForValue()
				.get(RedisCacheKey.MODEL_TOTAL_COUNT_PREFIX);
		return o == null ? 0 : Integer.parseInt(o.toString());
	}

	public void setModelType(String value) {
		redisTemplate.opsForValue().set(RedisCacheKey.MODEL_TYPE_PREFIX, value);
		redisTemplate.expire(RedisCacheKey.MODEL_TYPE_PREFIX, 7,
				TimeUnit.DAYS);
	}

	public String getModelType() {
		Object o = redisTemplate.opsForValue()
				.get(RedisCacheKey.MODEL_TYPE_PREFIX);
		return o == null ? "" : o.toString();
	}

	public void setNotShowModelType(String value) {
		redisTemplate.opsForValue().set(RedisCacheKey.MODEL_TYPE_NOT_PREFIX, value);
		redisTemplate.expire(RedisCacheKey.MODEL_TYPE_NOT_PREFIX, 7,
				TimeUnit.DAYS);
	}

	public String getNotShowModelType() {
		Object o = redisTemplate.opsForValue()
				.get(RedisCacheKey.MODEL_TYPE_NOT_PREFIX);
		return o == null ? "" : o.toString();
	}
}
