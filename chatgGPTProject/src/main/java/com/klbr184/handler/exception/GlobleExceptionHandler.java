package com.klbr184.handler.exception;

import com.klbr184.exception.SystemException;
import com.klbr184.resp.CommonResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-06 00:39:02
 */

@RestControllerAdvice
@Slf4j
public class GlobleExceptionHandler {
    //经过统一处理过的异常
    @ExceptionHandler(SystemException.class)
    public CommonResp<SystemException> systemException(SystemException e){
        log.error("系统出现已知异常! " + e.getMessage());
        return new CommonResp<>(e.getCode(),e.getMsg(),null);
    }
    //未经过统一处理过的异常
    @ExceptionHandler(Exception.class)
    public CommonResp<String> exception(Exception e){
        log.error("系统出现未知异常! " + e.getMessage());
        return new CommonResp<>(500,"系统异常,操作失败！ "+e.getMessage(),null);
    }
}
