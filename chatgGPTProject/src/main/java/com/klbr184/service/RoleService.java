package com.klbr184.service;

import com.klbr184.req.RoleUpdateReq;
import com.klbr184.resp.CommonResp;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-11 16:01:19
 */
public interface RoleService {
    CommonResp getRole(Integer page);

    CommonResp updateRole(RoleUpdateReq roleUpdateReq);

    CommonResp addRole(RoleUpdateReq roleUpdateReq);

    CommonResp deleteRole(Integer roleId);

    CommonResp getRoleIdByUserID(Long userID);
}
