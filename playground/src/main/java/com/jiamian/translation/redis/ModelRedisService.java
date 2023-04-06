package com.jiamian.translation.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

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
		redisTemplate.expire(RedisCacheKey.MODEL_TYPE_PREFIX, 7, TimeUnit.DAYS);
	}

	public String getModelType() {
		Object o = redisTemplate.opsForValue()
				.get(RedisCacheKey.MODEL_TYPE_PREFIX);
		return o == null ? "" : o.toString();
	}

	public void setNotShowModelType(String value) {
		redisTemplate.opsForValue().set(RedisCacheKey.MODEL_TYPE_NOT_PREFIX,
				value);
		redisTemplate.expire(RedisCacheKey.MODEL_TYPE_NOT_PREFIX, 7,
				TimeUnit.DAYS);
	}

	public String getNotShowModelType() {
		Object o = redisTemplate.opsForValue()
				.get(RedisCacheKey.MODEL_TYPE_NOT_PREFIX);
		return o == null ? "" : o.toString();
	}

	public void addCollectionModelUser(Long targetUserId, Long modelId) {
		String key = RedisCacheKey.USER_COLLECTION_MODEL_LIST_PREFIX
				+ targetUserId;
		redisTemplate.opsForZSet().add(key, modelId,
				System.currentTimeMillis());

	}

	public void removeCollectionModelUser(Long targetUserId, Long modelId) {
		String key = RedisCacheKey.USER_COLLECTION_MODEL_LIST_PREFIX
				+ targetUserId;
		redisTemplate.opsForZSet().remove(key, modelId);

	}

	/**
	 * 是否已经收藏模型
	 *
	 * @param targetUserId
	 * @param modelId
	 * @return
	 */
	public Boolean isCollectionModelUser(Long targetUserId, Long modelId) {
		String key = RedisCacheKey.USER_COLLECTION_MODEL_LIST_PREFIX
				+ targetUserId;
		Double score = redisTemplate.opsForZSet().score(key, modelId);
		return score != null;
	}

	public Set<ZSetOperations.TypedTuple<String>> userCollectionModelList(
			Long targetUserId, Integer pageNo, Integer pageSize) {
		String key = RedisCacheKey.USER_COLLECTION_MODEL_LIST_PREFIX
				+ targetUserId;
		Set<ZSetOperations.TypedTuple<String>> set = redisTemplate.opsForZSet()
				.reverseRangeWithScores(key, pageNo * pageSize,
						(pageNo + 1) * pageSize - 1);
		return set;
	}

	/**
	 * 该用户收藏数量
	 *
	 * @param targetUserId
	 * @return
	 */
	public int userCollectionModelCount(Long targetUserId) {
		String key = RedisCacheKey.USER_COLLECTION_MODEL_LIST_PREFIX
				+ targetUserId;
		Long count = redisTemplate.opsForZSet().size(key);
		return count == null ? 0 : count.intValue();
	}

	private List<Long> transSet(Set<Integer> set) {
		if (set != null && set.size() > 0) {
			return set.stream().map(Integer::longValue)
					.collect(Collectors.toList());
		} else {
			return new ArrayList<>();
		}
	}
}
