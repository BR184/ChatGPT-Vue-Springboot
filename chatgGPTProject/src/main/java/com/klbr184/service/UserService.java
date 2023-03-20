package com.klbr184.service;

import com.klbr184.entity.UserEntity;
import com.klbr184.req.UserAuthReq;
import com.klbr184.req.UserSaveReq;
import com.klbr184.resp.CommonResp;

import java.util.Map;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-17 00:36:15
 */
public interface UserService {
    void register(UserSaveReq req);

    CommonResp<Map> login(UserAuthReq req);

    CommonResp logout();

    UserEntity getUser();
}
