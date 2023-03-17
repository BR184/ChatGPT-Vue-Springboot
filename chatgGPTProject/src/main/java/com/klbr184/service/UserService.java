package com.klbr184.service;

import com.klbr184.req.UserSaveReq;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-17 00:36:15
 */
public interface UserService {
    void register(UserSaveReq req);
}
