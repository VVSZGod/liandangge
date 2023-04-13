package com.jiamian.translation.redis;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class RedisDao {

	private static Logger logger = LoggerFactory.getLogger(RedisDao.class);

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 指定缓存失效时间
	 *
	 * @param key
	 *            键
	 * @param time
	 *            时间(秒)
	 * @return
	 */
	public boolean expire(String key, long time) {
		try {
			if (time > 0) {
				return stringRedisTemplate.expire(key, time, TimeUnit.SECONDS);
			}
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
		return false;
	}

	public Long increment(String key, long delta) {
		try {
			return redisTemplate.opsForValue().increment(key, delta);
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
		return null;
	}

	public Long incrementForStringValue(String key, long delta) {
		try {
			return stringRedisTemplate.opsForValue().increment(key, delta);
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
		return null;
	}

	/**
	 * 根据key 获取过期时间
	 *
	 * @param key
	 *            键 不能为null
	 * @return 时间(秒) 返回0代表为永久有效
	 */
	public Long getExpire(String key) {
		try {
			return redisTemplate.getExpire(key, TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
		return null;
	}

	public boolean setIfAbsent(String key, String value) {
		if (StringUtils.isAnyBlank(key, value)) {
			return false;
		}
		try {
			return stringRedisTemplate.opsForValue().setIfAbsent(key, value);
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
		return false;
	}

	public boolean lock(String key) {
		if (StringUtils.isAnyBlank(key)) {
			return false;
		}
		boolean result = false;
		try {
			result = stringRedisTemplate.opsForValue().setIfAbsent(key,
					String.valueOf(System.currentTimeMillis()));
			if (!result) {
				String time = stringRedisTemplate.opsForValue().get(key);
				if (null != time) {
					Long diff = System.currentTimeMillis() - Long.valueOf(time);
					if (diff.compareTo(4000L) > 0) {
						return true;
					}
				} else {
					return true;
				}
			} else {
				stringRedisTemplate.expire(key, 3, TimeUnit.SECONDS);
			}
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
		return result;
	}

	public boolean lock(String key, long seconds) {
		if (StringUtils.isAnyBlank(key)) {
			return false;
		}
		boolean result = false;
		try {
			result = stringRedisTemplate.opsForValue().setIfAbsent(key,
					String.valueOf(System.currentTimeMillis()));
			if (!result) {
				String time = stringRedisTemplate.opsForValue().get(key);
				if (null != time) {
					Long diff = System.currentTimeMillis() - Long.valueOf(time);
					if (diff.compareTo(36000000L) > 0) {
						return true;
					}
				} else {
					return true;
				}
			} else {
				stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
			}
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
		return result;
	}

	/**
	 * 判断key是否存在
	 *
	 * @param key
	 *            键
	 * @return true 存在 false不存在
	 */
	public boolean hasKey(String key) {
		try {
			if (StringUtils.isNotBlank(key)) {
				return redisTemplate.hasKey(key);
			}
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
		return false;
	}

	/**
	 * 删除缓存
	 *
	 * @param key
	 *            可以传一个值 或多个
	 */
	@SuppressWarnings("unchecked")
	public void del(String... key) {
		try {
			if (key != null && key.length > 0) {
				if (key.length == 1) {
					redisTemplate.delete(key[0]);
				} else {
					redisTemplate.delete(CollectionUtils.arrayToList(key));
				}
			}
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
	}

	public boolean delString(String key) {
		boolean flag = false;
		try {
			flag = stringRedisTemplate.delete(key);
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
		return flag;
	}

	/**
	 * 普通缓存获取
	 *
	 * @param key
	 *            键
	 * @return 值
	 */
	public Object get(String key) {
		try {
			return key == null ? null : redisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			logger.error("redis fail", e);
			return null;
		}

	}

	/**
	 * 普通缓存放入-无限期
	 *
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @return true成功 false失败
	 */
	public boolean set(String key, Object value) {
		try {
			redisTemplate.opsForValue().set(key, value);
			return true;
		} catch (Exception e) {
			logger.error("redis fail", e);
			return false;
		}

	}

	/**
	 * 普通缓存放入并设置时间
	 *
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param time
	 *            时间(秒) time要大于0 如果time小于等于0 将设置无限期
	 * @return true成功 false 失败
	 */
	public boolean set(String key, Object value, long time) {
		try {
			if (time > 0) {
				redisTemplate.opsForValue().set(key, value, time,
						TimeUnit.SECONDS);
			} else {
				set(key, value);
			}
			return true;
		} catch (Exception e) {
			logger.error("redis fail", e);
			return false;
		}
	}

	public boolean hasStringKey(String key) {
		try {
			if (StringUtils.isNotBlank(key)) {
				return stringRedisTemplate.hasKey(key);
			}
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
		return false;
	}

	public String getString(String key) {
		try {
			return key == null ? null
					: stringRedisTemplate.opsForValue().get(key);
		} catch (Exception e) {
			logger.error("redis fail", e);
			return null;
		}
	}

	public boolean setString(String key, String value, Long time,
			TimeUnit timeUnit) {
		if (StringUtils.isAnyBlank(key, value)) {
			return false;
		}
		try {
			if (time > 0) {
				stringRedisTemplate.opsForValue().set(key, value, time,
						timeUnit);
			} else {
				stringRedisTemplate.opsForValue().set(key, value);
			}
			return true;
		} catch (Exception e) {
			logger.error("redis fail", e);
			return false;
		}
	}

	public boolean setString(String key, String value, long time) {
		return setString(key, value, time, TimeUnit.SECONDS);
	}

	public void rightPush(String key, String value) {
		if (StringUtils.isAnyBlank(key, value)) {
			return;
		}
		try {
			stringRedisTemplate.opsForList().rightPush(key, value);
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
	}

	public void rightPushAll(String key, List<String> values) {
		if (Objects.isNull(key) || CollectionUtils.isEmpty(values)) {
			return;
		}
		try {
			stringRedisTemplate.opsForList().rightPushAll(key, values);
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
	}

	public String leftPop(String key) {
		if (Objects.isNull(key)) {
			return null;
		}
		try {
			return stringRedisTemplate.opsForList().leftPop(key);
		} catch (Exception e) {
			logger.error("redis fail", e);
			return null;
		}
	}

	public String leftPopBlock(String key) {
		return leftPopBlock(key, 30L);
	}

	public String leftPopBlock(String key, long time) {
		if (Objects.isNull(key)) {
			return null;
		}
		try {
			return stringRedisTemplate.opsForList().leftPop(key, time,
					TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error("redis fail", e);
			return null;
		}
	}

	public String rightPop(String key) {
		if (Objects.isNull(key)) {
			return null;
		}
		try {
			return stringRedisTemplate.opsForList().rightPop(key);
		} catch (Exception e) {
			logger.error("redis fail", e);
			return null;
		}
	}

	public String rightPopBlock(String key) {
		return rightPopBlock(key, 15L);
	}

	public String rightPopBlock(String key, long time) {
		if (Objects.isNull(key)) {
			return null;
		}
		try {
			return stringRedisTemplate.opsForList().rightPop(key, time,
					TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.error("redis fail", e);
			return null;
		}
	}

	public Long listSize(String key) {
		if (Objects.isNull(key)) {
			return 0L;
		}
		try {
			return stringRedisTemplate.opsForList().size(key);
		} catch (Exception e) {
			logger.error("redis fail", e);
			return 0L;
		}
	}

	public List<String> range(String key, Integer start, Integer end) {
		if (Objects.isNull(key)) {
			return null;
		}
		try {
			return stringRedisTemplate.opsForList().range(key, start, end);
		} catch (Exception e) {
			logger.error("redis fail", e);
			return null;
		}
	}

	public void putAllHash(String key, Map<String, String> map, Long time,
			TimeUnit timeUnit) {
		if (Objects.isNull(key) || CollectionUtils.isEmpty(map)) {
			return;
		}
		try {
			stringRedisTemplate.opsForHash().putAll(key, map);
			stringRedisTemplate.expire(key, time, timeUnit);
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
	}

	public void putHash(String key, String hashKey, String hashValue) {
		if (StringUtils.isAnyBlank(key, hashKey, hashValue)) {
			return;
		}
		try {
			stringRedisTemplate.opsForHash().put(key, hashKey, hashValue);
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
	}

	public String getHash(String key, String hashKey) {
		if (StringUtils.isAnyBlank(key, hashKey)) {
			return null;
		}
		try {
			return (String) stringRedisTemplate.opsForHash().get(key, hashKey);
		} catch (Exception e) {
			logger.error("redis fail", e);
			return null;
		}
	}

	public void addSet(String key, String value) {
		if (Objects.isNull(key) || Objects.isNull(value)) {
			return;
		}
		try {
			stringRedisTemplate.boundSetOps(key).add(value);
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
	}

	public boolean setContians(String key, String value) {
		if (Objects.isNull(key) || Objects.isNull(value)) {
			return false;
		}
		try {
			return stringRedisTemplate.boundSetOps(key).isMember(value);
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
		return false;
	}

	public Set<String> getSet(String key) {
		if (Objects.isNull(key)) {
			return Collections.emptySet();
		}
		try {
			return stringRedisTemplate.boundSetOps(key).members();
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
		return Collections.emptySet();
	}

	public Double incrementScoreZSet(String key, String value) {
		return incrementScoreZSet(key, value, 1);
	}

	public Double incrementScoreZSet(String key, String value, double delta) {
		if (Objects.isNull(key) || Objects.isNull(value)) {
			return null;
		}
		try {
			return stringRedisTemplate.boundZSetOps(key).incrementScore(value,
					delta);
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
		return null;
	}

	public Double getScoreZSet(String key, String value) {
		if (Objects.isNull(key) || Objects.isNull(value)) {
			return null;
		}
		try {
			return redisTemplate.opsForZSet().score(key,value);
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
		return null;
	}

	// 新增为true,更新为false
	public Boolean setScoreZSet(String key, String value, Double score) {
		if (Objects.isNull(key) || Objects.isNull(value)) {
			return null;
		}
		try {
			return stringRedisTemplate.boundZSetOps(key).add(value, score);
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
		return null;
	}

	public Long getRankZSet(String key, String value, boolean desc) {
		if (Objects.isNull(key) || Objects.isNull(value)) {
			return null;
		}
		try {
			if (desc) {
				return stringRedisTemplate.boundZSetOps(key).reverseRank(value);
			} else {
				return stringRedisTemplate.boundZSetOps(key).rank(value);
			}
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
		return null;
	}

	public Set<ZSetOperations.TypedTuple<String>> getReverseRangeWithScoresZSet(
			String key, Long start, Long end) {
		if (Objects.isNull(key) || Objects.isNull(start)
				|| Objects.isNull(end)) {
			return null;
		}
		try {
			return stringRedisTemplate.boundZSetOps(key)
					.reverseRangeWithScores(start, end);
		} catch (Exception e) {
			logger.error("redis fail", e);
		}
		return null;
	}

	public Double getReverseRangeWithScoresZSet(String key, Long index) {
		Double zero = 0d;
		Set<ZSetOperations.TypedTuple<String>> tuples = getReverseRangeWithScoresZSet(
				key, index - 1, index - 1);
		if (CollectionUtils.isEmpty(tuples)) {
			return zero;
		} else {
			for (ZSetOperations.TypedTuple<String> tuple : tuples) {
				Double score = tuple.getScore();
				return Objects.isNull(score) ? zero : score;
			}
		}
		return zero;
	}

	/**
	 * @param key
	 * @param value
	 * @param timeOut
	 *            过期时间 (秒)
	 */
	public boolean setNx(String key, Object value, long timeOut) {
		Boolean result = (Boolean) redisTemplate
				.execute((RedisCallback<Boolean>) connection -> {
					RedisSerializer valueSerializer = redisTemplate
							.getValueSerializer();
					RedisSerializer keySerializer = redisTemplate
							.getKeySerializer();
					Object obj = connection.execute("set",
							keySerializer.serialize(key),
							valueSerializer.serialize(value),
							"NX".getBytes(StandardCharsets.UTF_8),
							"EX".getBytes(StandardCharsets.UTF_8),
							String.valueOf(timeOut)
									.getBytes(StandardCharsets.UTF_8));
					return obj != null;
				});
		return result;
	}

	/**
	 * hash递增 如果不存在,就会创建一个 并把新增后的值返回
	 *
	 * @param key
	 *            键
	 * @param item
	 *            项
	 * @param by
	 *            要增加几(大于0)
	 * @return
	 */
	public double hincr(String key, String item, double by) {
		return redisTemplate.opsForHash().increment(key, item, by);
	}

	public void addZset(String key, String value, long score) {
		redisTemplate.opsForZSet().add(key, value, score);
	}
}
