package com.klbr184.exception;

import cn.hutool.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-06 00:42:34
 */
public class SystemException extends RuntimeException{
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }

    public SystemException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
