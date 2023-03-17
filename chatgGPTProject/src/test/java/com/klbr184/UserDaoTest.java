package com.klbr184;

import com.klbr184.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-17 20:20:24
 */
@SpringBootTest
public class UserDaoTest {

    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testUserDao(){
        System.out.println(userDao.selectList(null));
    }
    @Test
    public void testBCryptPasswordEncoder(){
        System.out.println(passwordEncoder.encode("asdasd"));
    }
}
