package com.xdja.web.configure.token.operator;

import com.xdja.web.configure.token.TokenConfig;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis会话操作
 *
 * @author lvhao
 * @version 1.0
 * @date 2019/4/4 18:05
 */
public class RedisTokenOperator extends AbstractTokenOperator {
    private RedisTemplate<String, String> redisTemplate;

    private String prefix = "ltap:token";

    /**
     * Constructors ： RedisTokenOperator.
     *
     * @param config
     */
    public RedisTokenOperator(TokenConfig config) {
        super(config);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String actualKey(String key) {
        return prefix + ":" + key;
    }

    @Override
    public String add(String value) {
        String key = config.getKeyGenerator().get();
        String actualKey = actualKey(key);
        redisTemplate.opsForValue().set(actualKey, value, config.getExpiredTimeInMinutes(), TimeUnit.MINUTES);
        return key;
    }

    @Override
    public String get(String token) {
        String actualKey = actualKey(token);
        if (config.isAutoDelay()) {
            redisTemplate.expire(actualKey, config.getExpiredTimeInMinutes(), TimeUnit.MINUTES);
        }
        return redisTemplate.opsForValue().get(actualKey);
    }

    @Override
    public List<String> values() {
        Set<String> set = redisTemplate.keys(prefix + "*");
        return redisTemplate.opsForValue().multiGet(set);
    }

    @Override
    public Map<String, String> all() {
        Set<String> set = redisTemplate.keys(prefix + "*");
        List<String> list = redisTemplate.opsForValue().multiGet(set);
        Map<String, String> map = new HashMap<>();

        Iterator<String> iterator = set.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            String key = iterator.next();
            map.put(key, list.get(i));
            i++;
        }
        return map;
    }

    @Override
    public boolean delay(String token) {
        String actualKey = actualKey(token);
        return redisTemplate.expire(actualKey, config.getExpiredTimeInMinutes(), TimeUnit.MINUTES);
    }

    @Override
    public boolean invalidate(String token) {
        String actualKey = actualKey(token);
        redisTemplate.delete(actualKey);
        return true;
    }
}

