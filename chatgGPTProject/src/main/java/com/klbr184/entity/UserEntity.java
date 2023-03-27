package com.klbr184.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-16 23:43:53
 */
@TableName("user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String head;
    private Long coin;
    private Long chatForm;
    private Date lastLogin;
    private Date regTime;
    private Byte deleted;
}
