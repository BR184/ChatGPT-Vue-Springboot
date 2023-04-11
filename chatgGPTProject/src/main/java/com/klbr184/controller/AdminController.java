package com.klbr184.controller;

import com.klbr184.resp.CommonResp;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-11 12:51:13
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    //是否有基础权限
    @PreAuthorize("hasAuthority('default_admin')")
    @GetMapping("is_admin")
    public CommonResp isAdmin() {
        return new CommonResp<Boolean>(200, "success", true);
    }
}
