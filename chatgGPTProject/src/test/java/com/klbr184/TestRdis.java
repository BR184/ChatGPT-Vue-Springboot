package com.klbr184;

import com.klbr184.utils.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.util.Set;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-15 01:05:40
 */
@SpringBootTest
public class TestRdis {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test(){
        int size = redisTemplate.keys("*").size();
        System.out.println(size);
    }
    @Test
    public void test2(){
        String pattern = "login*";
        ScanOptions options = ScanOptions.scanOptions().match(pattern).build();
        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(options);
        int size = 0;
        while (cursor.hasNext()) {
            cursor.next();
            size++;
        }
        System.out.println(size);
    }
    @Test
    public void test3(){
        Integer login = RedisUtil.countKeysStartWith("login");
        System.out.println(login);
    }
}
