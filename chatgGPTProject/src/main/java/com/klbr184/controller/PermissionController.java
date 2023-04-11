package com.klbr184.controller;

import com.klbr184.resp.CommonResp;
import com.klbr184.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-11 22:24:45
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;
    @PreAuthorize("hasAuthority('manage_role')")
    @GetMapping()
    public CommonResp getPermissionByRoleId(@RequestParam Integer roleId) {
        return permissionService.getPermissionByRoleId(roleId);
    }
    @PreAuthorize("hasAuthority('manage_role')")
    @GetMapping("/all")
    public CommonResp getAllPermission() {
        return permissionService.getAllPermission();
    }

}
