package com.klbr184.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-16 23:43:53
 */
@TableName("user")
@Data
public class UserEntity {
    private long id;
    private String username;
    private String password;
    private String email;
    private String head;
    private long coin;
    private long chatForm;
    private Date lastLogin;
    private Date regTime;
    private byte deleted;
}
