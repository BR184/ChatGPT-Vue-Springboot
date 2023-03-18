package com.klbr184.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-16 23:43:53
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthReq {
    private String username;
    private String password;
}
