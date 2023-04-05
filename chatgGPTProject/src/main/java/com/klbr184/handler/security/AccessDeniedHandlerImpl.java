package com.klbr184.handler.security;

import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.klbr184.resp.CommonResp;
import com.klbr184.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 权限不足异常403
 * @author KL
 * @version 1.0
 * @since 2023-03-19 01:50:03
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        CommonResp commonResp = new CommonResp(HttpStatus.HTTP_FORBIDDEN,"您没有执行该操作的权限",null);
        WebUtils.renderString(response, JSON.toJSONString(commonResp));
    }
}
