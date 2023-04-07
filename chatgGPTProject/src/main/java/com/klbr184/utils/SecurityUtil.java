package com.klbr184.utils;

import com.klbr184.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-21 16:23:12
 */
public class SecurityUtil {

    /**
     * 获取当前登录用户名
     *
     * @return 用户Id
     */
    public static Long getUserId() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser user = (LoginUser) authentication.getPrincipal();
        Long id = user.getUser().getId();
        return id;
    }
    /**
     * 获取当前登录用户加密后的密码
     *
     * @return 加密后的密码
     */
    public static String getPassword() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser user = (LoginUser) authentication.getPrincipal();
        String password = user.getUser().getPassword();
        return password;
    }


}
