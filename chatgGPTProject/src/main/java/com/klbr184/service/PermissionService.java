package com.klbr184.service;

import com.klbr184.resp.CommonResp;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-11 22:27:25
 */
public interface PermissionService {
    CommonResp getPermissionByRoleId(Integer roleId);

    CommonResp getAllPermission();
}
