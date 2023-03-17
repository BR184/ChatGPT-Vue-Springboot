package com.klbr184.req;

import lombok.Data;

import java.util.Date;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-17 01:00:57
 */
@Data
public class UserSaveReq {
    private String username;
    private String password;
    private String email;
    private Date regTime;
}
