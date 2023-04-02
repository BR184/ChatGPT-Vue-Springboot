package com.klbr184;

import com.klbr184.mapper.PermissionMapper;
import com.klbr184.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-17 20:20:24
 */
@SpringBootTest
public class MapperTest {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PermissionMapper permissionMapper;

    @Test
    public void testUserDao(){
        System.out.println(userMapper.selectList(null));
    }
    @Test
    public void testBCryptPasswordEncoder(){
        System.out.println(passwordEncoder.encode("asdasd"));
    }
    @Test
    public void TestPermissionMapper(){
        System.out.println(permissionMapper.selectPermissionsByUserID(1636751065036918786L).toString());
    }
    //测试接口

}
