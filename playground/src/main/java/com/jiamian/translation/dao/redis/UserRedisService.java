package com.jiamian.translation.dao.redis;

import com.jiamian.translation.redis.RedisCacheKey;
import com.jiamian.translation.redis.RedisDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserRedisService {
    private static Logger logger = LoggerFactory
            .getLogger(UserRedisService.class);

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisDao redisDao;

    /**
     * 返回自增用户id
     *
     * @return
     */
    public Long incrUserId() {
        return redisTemplate.opsForValue()
                .increment(RedisCacheKey.TRANSLATION_USER_INCR_KEY, 1);
    }
}
