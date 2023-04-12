package com.klbr184.controller;

import com.klbr184.req.RoleUpdateReq;
import com.klbr184.resp.CommonResp;
import com.klbr184.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-11 15:48:39
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    //查询角色
    @PreAuthorize("hasAuthority('manage_role')")
    @GetMapping()
    public CommonResp getRole(@RequestParam Integer page) {
        return roleService.getRole(page);
    }

    @PreAuthorize("hasAuthority('manage_role')")
    @PutMapping()
    public CommonResp updateRole(@RequestBody RoleUpdateReq roleUpdateReq) {
        return roleService.updateRole(roleUpdateReq);
    }
}
