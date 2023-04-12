package com.klbr184.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.klbr184.entity.Permission;
import com.klbr184.entity.Role;
import com.klbr184.entity.RolePermission;
import com.klbr184.exception.SystemException;
import com.klbr184.mapper.PermissionMapper;
import com.klbr184.mapper.RoleMapper;
import com.klbr184.mapper.RolePermissionMapper;
import com.klbr184.req.RoleUpdateReq;
import com.klbr184.resp.CommonResp;
import com.klbr184.service.RoleService;
import com.klbr184.vo.RoleBeanVo;
import io.netty.util.ResourceLeakDetector;
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
    private PermissionMapper PermissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Override
    public CommonResp getRole(Integer page) {
        if (page == null) {
            return new CommonResp<>(400, "参数错误", null);
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
        BeanUtil.copyProperties(roleUpdateReq, role,"rolePermissions");
        //如果是超级管理员
        if (role.getId()==1){
            if(!role.getRoleKey().equals("super_administrator")){
                throw new SystemException(400,role.getRoleName()+"超级管理员的key不能修改");
            }
            if(!role.getState().equals("0")){
                throw new SystemException(400,role.getRoleName()+"超级管理员的状态不能修改");
            }
        }
        //如果是默认用户
        else if (role.getId()==2){
            if(!role.getRoleKey().equals("user")){
                throw new SystemException(400,role.getRoleName()+"默认用户的key不能修改");
            }
            if(!role.getState().equals("0")){
                throw new SystemException(400,role.getRoleName()+"默认用户的状态不能修改");
            }
        }
        //遍历权限id
        Integer roleId = roleUpdateReq.getId();
        //获取当前角色已经拥有的权限
        List<Permission> permissions = PermissionMapper.selectPermissionsByRoleId(roleId);
        //获取当前角色已经拥有的权限id
        List<Integer> ownedPermissionIds = permissions.stream().map(Permission::getId).collect(Collectors.toList());
        //获取全部的权限
        List<Permission> allPermission = PermissionMapper.selectList(null);
        //获取全部的权限id
        List<Integer> allPermissionIds = allPermission.stream().map(Permission::getId).collect(Collectors.toList());
        //记录当前角色需要新增的权限id
        List<Integer> addPermissionIds = new ArrayList<>();
        //记录当前角色需要删除的权限id
        List<Integer> deletePermissionIds = new ArrayList<>();
        //获取当前角色需要新增和删除的权限id
        for (Integer permissionId : roleUpdateReq.getRolePermissions()) {
            Permission permission = PermissionMapper.selectById(permissionId);
            if (permission == null) {
                return new CommonResp<>(400, "参数错误", null);
            }
            //如果当前角色未拥有该权限，则需要新增
            if(!ownedPermissionIds.contains(permissionId)){
                addPermissionIds.add(permissionId);
            }
        }
        for (Integer allPermissionId : allPermissionIds) {
            if(ownedPermissionIds.contains(allPermissionId)&&!roleUpdateReq.getRolePermissions().contains(allPermissionId)){
                deletePermissionIds.add(allPermissionId);
            }
        }
        //处理需要新增的权限
        if (addPermissionIds.size() > 0) {
            System.out.println(addPermissionIds);
            for (Integer addPermissionId : addPermissionIds) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(roleId).setPermissionId(addPermissionId);
                rolePermissionMapper.insert(rolePermission);
            }
        }
        //处理需要删除的权限
        if (deletePermissionIds.size() > 0) {
            System.out.println(deletePermissionIds);
            for (Integer deletePermissionId : deletePermissionIds) {
                Permission permission = PermissionMapper.selectById(deletePermissionId);

                //防止没有基础权限
                if(permission.getPermissionKey().equals("default")||permission.getPermissionKey().equals("default_admin")||permission.getPermissionKey().equals("manage_role")){
                    //user不能没有default权限
                    if(roleUpdateReq.getRoleKey().equals("user")&&permission.getPermissionKey().equals("default")){
                        throw new SystemException(400, roleUpdateReq.getRoleName()+"不能没有"+permission.getPermissionName()+"权限!");
                    }
                    //super_administrator不能没有default权限和default_admin权限
                    if(roleUpdateReq.getRoleKey().equals("super_administrator")&&permission.getPermissionKey().equals("default")){
                        throw new SystemException(400, roleUpdateReq.getRoleName()+"不能没有"+permission.getPermissionName()+"权限!");
                    }
                    if(roleUpdateReq.getRoleKey().equals("super_administrator")&&permission.getPermissionKey().equals("default_admin")){
                        throw new SystemException(400, roleUpdateReq.getRoleName()+"不能没有"+permission.getPermissionName()+"权限!");
                    }
                    if(roleUpdateReq.getRoleKey().equals("super_administrator")&&permission.getPermissionKey().equals("manage_role")){
                        throw new SystemException(400, roleUpdateReq.getRoleName()+"不能没有"+permission.getPermissionName()+"权限!");
                    }
                }
                Map<String,Object> rolePermissionMap = new HashMap<>();
                rolePermissionMap.put("role_id",roleId);
                rolePermissionMap.put("permission_id",deletePermissionId);
                rolePermissionMapper.deleteByMap(rolePermissionMap);
            }
        }
        role.setUpdateTime(DateUtil.date());
        roleMapper.updateById(role);
        return new CommonResp<>(200, "操作成功", null);
    }
}
