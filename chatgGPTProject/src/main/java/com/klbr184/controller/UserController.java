package com.klbr184.controller;

import cn.hutool.core.date.DateUtil;
import com.klbr184.req.UserAuthReq;
import com.klbr184.req.UserSaveReq;
import com.klbr184.resp.CommonResp;
import com.klbr184.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-17 00:35:16
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PreAuthorize("hasAuthority('default')")
    @GetMapping("info")
    public CommonResp getUser(){
        return userService.getUser();
    }
    @PostMapping("register")
    public CommonResp register(@RequestBody UserSaveReq req){
        req.setPassword(passwordEncoder.encode(req.getPassword()));
        req.setRegTime(DateUtil.date());
        CommonResp resp = new CommonResp<>();
        userService.register(req);
        return resp;
    }
    @PostMapping("login")
    public CommonResp login(@RequestBody UserAuthReq req){
        return userService.login(req);
    }
    @GetMapping ("logout")
    public CommonResp logout(){
        return userService.logout();
    }
    @RequestMapping("test")
    @PreAuthorize("hasAuthority('default')")
    public CommonResp test(){
        return new CommonResp<>(200,"Hi!",null);
    }
}
