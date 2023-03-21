package com.klbr184.utils;

import com.klbr184.entity.LoginUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-21 16:23:12
 */
public class SecurityUtil {
    public static Long getUserId(){
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser user = (LoginUser) authentication.getPrincipal();
        Long id = user.getUser().getId();
        return id;
    }
}
