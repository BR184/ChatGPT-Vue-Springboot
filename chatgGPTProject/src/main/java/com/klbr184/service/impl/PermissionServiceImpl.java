package com.klbr184.service.impl;

import com.klbr184.entity.Permission;
import com.klbr184.mapper.PermissionMapper;
import com.klbr184.resp.CommonResp;
import com.klbr184.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-11 22:27:52
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Override
    public CommonResp getPermissionByRoleId(Integer roleId) {
        if (roleId == null ) {
            return new CommonResp<>(400, "参数错误", null);
        }
        List<Permission> perList = permissionMapper.selectPermissionsRoleId(roleId);
        if (perList == null || perList.size() == 0) {
            return new CommonResp<>(200, "操作成功", null);
        }
        perList.sort(new Comparator<Permission>() {
            @Override
            public int compare(Permission o1, Permission o2) {
                //按照id升序排列
                return o1.getId().compareTo(o2.getId());
            }
        });
        return new CommonResp<>(200, "操作成功", perList);
    }

    @Override
    public CommonResp getAllPermission() {
        List<Permission> perList = permissionMapper.selectList(null);
        if (perList == null || perList.size() == 0) {
            return new CommonResp<>(200, "操作成功", null);
        }
        perList.sort(new Comparator<Permission>() {
            @Override
            public int compare(Permission o1, Permission o2) {
                //按照id升序排列
                return o1.getId().compareTo(o2.getId());
            }
        });
        return new CommonResp<>(200, "操作成功", perList);
    }
}
