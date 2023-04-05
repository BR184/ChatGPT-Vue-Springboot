package com.klbr184.handler.security;

import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.klbr184.resp.CommonResp;
import com.klbr184.utils.WebUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理401
 * @author KL
 * @version 1.0
 * @since 2023-03-19 01:43:33
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        CommonResp commonResp = new CommonResp(HttpStatus.HTTP_UNAUTHORIZED,"用户认证失败，请重新登录！",null);
        WebUtils.renderString(response, JSON.toJSONString(commonResp));
    }
}
