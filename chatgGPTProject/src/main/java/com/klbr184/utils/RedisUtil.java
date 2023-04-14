package com.klbr184.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-15 01:10:53
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    private static RedisTemplate staticRedisTemplate;

    @PostConstruct
    public void init() {
        this.staticRedisTemplate = redisTemplate;
    }


    public static Integer countKeysStartWith(String pattern){
        ScanOptions options = ScanOptions.scanOptions().match(pattern+"*").build();
        Cursor<byte[]> cursor = staticRedisTemplate.getConnectionFactory().getConnection().scan(options);
        int size = 0;
        while (cursor.hasNext()) {
            cursor.next();
            size++;
        }
        return size;
    }
}
