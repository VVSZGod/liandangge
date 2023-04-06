package com.jiamian.translation.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisService {
    private static Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisDao redisDao;


    /**
     * 获取锁
     *
     * @param key
     * @param seconds
     * @return
     */
    public boolean lock(String key, long seconds) {
        return redisDao.lock(key, seconds);
    }

}
