package com.klbr184.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.klbr184.entity.Permission;
import com.klbr184.entity.Role;
import com.klbr184.entity.RolePermission;
import com.klbr184.entity.UserRole;
import com.klbr184.exception.SystemException;
import com.klbr184.mapper.PermissionMapper;
import com.klbr184.mapper.RoleMapper;
import com.klbr184.mapper.RolePermissionMapper;
import com.klbr184.mapper.UserRoleMapper;
import com.klbr184.req.RoleUpdateReq;
import com.klbr184.resp.CommonResp;
import com.klbr184.service.RoleService;
import com.klbr184.vo.RoleBeanVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-11 16:02:17
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public CommonResp getRole(Integer page) {
        if (page == null || page < 1) {
            throw new SystemException(400, "页码错误");
        }
        IPage<Role> iPage = new Page<>(page, 10);
        roleMapper.selectPage(iPage, null);
        List<Role> records = iPage.getRecords();
        if (records == null || records.size() == 0) {
            return new CommonResp<>(200, "操作成功", null);
        }
        RoleBeanVo roleBeanVo = new RoleBeanVo();
        roleBeanVo.setData(records);
        roleBeanVo.setPage((int) iPage.getCurrent());
        roleBeanVo.setTotalpage((int) iPage.getPages());
        return new CommonResp<>(200, "操作成功", roleBeanVo);
    }

    @Override
    public CommonResp updateRole(RoleUpdateReq roleUpdateReq) {
        if (roleUpdateReq == null || roleUpdateReq.getRoleKey() == null || roleUpdateReq.getRoleName() == null) {
            return new CommonResp<>(400, "参数错误", null);
        }

        //TODO name和key不能重复
        //更新角色
        Role role = new Role();
        role.setId(roleUpdateReq.getId());
        BeanUtil.copyProperties(roleUpdateReq, role, "rolePermissions");
        //RoleName和RoleKey不能为
        if (role.getRoleName().equals("") || role.getRoleName() == null) {
            throw new SystemException(400, "角色名不能为空!");
        }
        if (role.getRoleKey().equals("") || role.getRoleKey() == null) {
            throw new SystemException(400, "角色key不能为空!");
        }
        //如果是超级管理员
        if (role.getId() == 1) {
            if (!role.getRoleKey().equals("super_administrator")) {
                throw new SystemException(400, role.getRoleName() + "超级管理员的key不能修改");
            }
            if (!role.getState().equals("0")) {
                throw new SystemException(400, role.getRoleName() + "超级管理员的状态不能修改");
            }
        }
        //如果是默认用户
        else if (role.getId() == 2) {
            if (!role.getRoleKey().equals("user")) {
                throw new SystemException(400, role.getRoleName() + "默认用户的key不能修改");
            }
            if (!role.getState().equals("0")) {
                throw new SystemException(400, role.getRoleName() + "默认用户的状态不能修改");
            }
        }
        //获取全部角色以做重复判断
        List<Role> roles = roleMapper.selectList(null);
        //遍历角色
        for (Role role1 : roles) {
            //如果角色名重复
            if (role1.getRoleName().equals(role.getRoleName()) && !role1.getId().equals(role.getId())) {
                throw new SystemException(400, "角色名重复!");
            }
            //如果角色key重复
            if (role1.getRoleKey().equals(role.getRoleKey()) && !role1.getId().equals(role.getId())) {
                throw new SystemException(400, "角色key重复!");
            }
        }
        //遍历权限id
        Integer roleId = roleUpdateReq.getId();
        //获取当前角色已经拥有的权限
        List<Permission> permissions = permissionMapper.selectPermissionsByRoleId(roleId);
        //获取当前角色已经拥有的权限id
        List<Integer> ownedPermissionIds = permissions.stream().map(Permission::getId).collect(Collectors.toList());
        //获取全部的权限
        List<Permission> allPermission = permissionMapper.selectList(null);
        //获取全部的权限id
        List<Integer> allPermissionIds = allPermission.stream().map(Permission::getId).collect(Collectors.toList());
        //记录当前角色需要新增的权限id
        List<Integer> addPermissionIds = new ArrayList<>();
        //记录当前角色需要删除的权限id
        List<Integer> deletePermissionIds = new ArrayList<>();
        //处理当前角色需要新增和删除的权限id
        for (Integer permissionId : roleUpdateReq.getRolePermissions()) {
            if (!allPermissionIds.contains(permissionId)) {
                return new CommonResp<>(400, "参数错误", null);
            }
            //如果当前角色未拥有该权限，则需要新增
            if (!ownedPermissionIds.contains(permissionId)) {
                addPermissionIds.add(permissionId);
            }
        }
        for (Integer allPermissionId : allPermissionIds) {
            if (ownedPermissionIds.contains(allPermissionId) && !roleUpdateReq.getRolePermissions().contains(allPermissionId)) {
                deletePermissionIds.add(allPermissionId);
            }
        }
        //处理需要新增的权限
        if (addPermissionIds.size() > 0) {
            for (Integer addPermissionId : addPermissionIds) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(roleId).setPermissionId(addPermissionId);
                rolePermissionMapper.insert(rolePermission);
            }
        }
        //处理需要删除的权限
        if (deletePermissionIds.size() > 0) {
            for (Integer deletePermissionId : deletePermissionIds) {
                Permission permission = permissionMapper.selectById(deletePermissionId);

                //防止没有基础权限
                if (permission.getPermissionKey().equals("default") || permission.getPermissionKey().equals("default_admin") || permission.getPermissionKey().equals("manage_role")) {
                    //user不能没有default权限
                    if (roleUpdateReq.getRoleKey().equals("user") && permission.getPermissionKey().equals("default")) {
                        throw new SystemException(400, roleUpdateReq.getRoleName() + "不能没有" + permission.getPermissionName() + "权限!");
                    }
                    //super_administrator不能没有default权限和default_admin权限
                    if (roleUpdateReq.getRoleKey().equals("super_administrator") && permission.getPermissionKey().equals("default")) {
                        throw new SystemException(400, roleUpdateReq.getRoleName() + "不能没有" + permission.getPermissionName() + "权限!");
                    }
                    if (roleUpdateReq.getRoleKey().equals("super_administrator") && permission.getPermissionKey().equals("default_admin")) {
                        throw new SystemException(400, roleUpdateReq.getRoleName() + "不能没有" + permission.getPermissionName() + "权限!");
                    }
                    if (roleUpdateReq.getRoleKey().equals("super_administrator") && permission.getPermissionKey().equals("manage_role")) {
                        throw new SystemException(400, roleUpdateReq.getRoleName() + "不能没有" + permission.getPermissionName() + "权限!");
                    }
                }
                Map<String, Object> rolePermissionMap = new HashMap<>();
                rolePermissionMap.put("role_id", roleId);
                rolePermissionMap.put("permission_id", deletePermissionId);
                rolePermissionMapper.deleteByMap(rolePermissionMap);
            }
        }
        role.setUpdateTime(DateUtil.date());
        roleMapper.updateById(role);
        return new CommonResp<>(200, "操作成功", null);
    }

    @Override
    public CommonResp addRole(RoleUpdateReq roleUpdateReq) {
        if (roleUpdateReq == null || roleUpdateReq.getRoleKey() == null || roleUpdateReq.getRoleName() == null) {
            return new CommonResp<>(400, "参数错误", null);
        }
        //添加角色
        Role role = new Role();
        BeanUtil.copyProperties(roleUpdateReq, role, "rolePermissions");
        //RoleName和RoleKey不能为
        if (role.getRoleName().equals("") || role.getRoleName() == null) {
            throw new SystemException(400, "角色名不能为空!");
        }
        if (role.getRoleKey().equals("") || role.getRoleKey() == null) {
            throw new SystemException(400, "角色key不能为空!");
        }
        //获取全部角色以做重复判断
        List<Role> roles = roleMapper.selectList(null);
        //遍历角色
        for (Role role1 : roles) {
            //如果角色名重复
            if (role1.getRoleName().equals(role.getRoleName())) {
                throw new SystemException(400, "角色名重复!");
            }
            //如果角色key重复
            if (role1.getRoleKey().equals(role.getRoleKey())) {
                throw new SystemException(400, "角色key重复!");
            }
        }
        //添加角色
        role.setId(null);
        role.setCreateTime(DateUtil.date());
        roleMapper.insert(role);
        Integer roleId = role.getId();
        //如果id为空，则添加失败
        if (roleId == null) {
            return new CommonResp<>(400, "添加失败", null);
        }
        //获取全部的权限
        List<Permission> allPermission = permissionMapper.selectList(null);
        //获取全部的权限id
        List<Integer> allPermissionIds = allPermission.stream().map(Permission::getId).collect(Collectors.toList());
        //记录当前角色需要新增的权限id
        List<Integer> addPermissionIds = new ArrayList<>();
        //处理当前角色需要新增和删除的权限id
        Boolean basePer = false;
        for (Integer permissionId : roleUpdateReq.getRolePermissions()) {
            if (!allPermissionIds.contains(permissionId)) {
                return new CommonResp<>(400, "参数错误", null);
            }
            if (!basePer) {
                Permission tempPermission = permissionMapper.selectById(permissionId);
                if (tempPermission.getPermissionKey().equals("default")) {
                    basePer = true;
                }
            }
            addPermissionIds.add(permissionId);
        }
        //如果没有基础权限，则不能添加
        if (!basePer) {
            throw new SystemException(400, "角色必须拥有正常用户默认权限!");
        }
        //处理需要新增的权限
        if (addPermissionIds.size() > 0) {
            for (Integer addPermissionId : addPermissionIds) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(roleId).setPermissionId(addPermissionId);
                rolePermissionMapper.insert(rolePermission);
            }
        }
        return new CommonResp<>(200, "操作成功", null);
    }
    //删除角色
    @Override
    public CommonResp deleteRole(Integer roleId) {
        roleMapper.selectById(roleId);
        //如果角色不存在，则返回错误
        if (roleMapper.selectById(roleId) == null) {
            throw new SystemException(400, "角色不存在");
        }
        //如果角色为超级管理员或默认用户，则不能删除
        if (roleMapper.selectById(roleId).getRoleKey().equals("super_admin") || roleMapper.selectById(roleId).getRoleKey().equals("user")) {
            throw new SystemException(400, "不能删除默认角色");
        }
        //先删除角色的权限
        Map<String, Object> rolePermissionMap = new HashMap<>();
        rolePermissionMap.put("role_id", roleId);
        rolePermissionMapper.deleteByMap(rolePermissionMap);
        //在设置用户的角色为默认用户
        Map<String, Object> userRoleMap = new HashMap<>();
        userRoleMap.put("role_id", roleId);
        List<UserRole> userRoles = userRoleMapper.selectByMap(userRoleMap);
        for (UserRole userRole : userRoles) {
            userRole.setRoleId(2);
            userRoleMapper.updateById(userRole);
        }
        //最后删除角色
        roleMapper.deleteById(roleId);
        return new CommonResp<>(200, "操作成功", null);
    }
}
