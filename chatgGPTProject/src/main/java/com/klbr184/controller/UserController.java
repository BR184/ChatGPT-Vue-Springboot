package com.klbr184.controller;

import cn.hutool.core.date.DateUtil;
import com.klbr184.req.UpdateUserInfoReq;
import com.klbr184.req.UserAuthReq;
import com.klbr184.req.UserSaveReq;
import com.klbr184.req.adminUpdateUserInfoReq;
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
    //获取用户信息
    @PreAuthorize("hasAuthority('default')")
    @GetMapping("info")
    public CommonResp getUser(){
        return userService.getUser();
    }
    //注册
    @PostMapping("register")
    public CommonResp register(@RequestBody UserSaveReq req){
        req.setPassword(passwordEncoder.encode(req.getPassword()));
        req.setRegTime(DateUtil.date());
        return userService.register(req);
    }
    //登录
    @PostMapping("login")
    public CommonResp login(@RequestBody UserAuthReq req){
        return userService.login(req);
    }
    //登出
    @PreAuthorize("hasAuthority('default')")
    @GetMapping ("logout")
    public CommonResp logout(){
        return userService.logout();
    }
    //用户修改用户信息
    @PreAuthorize("hasAuthority('default')")
    @PutMapping()
    public CommonResp update(@RequestBody UpdateUserInfoReq req){
        System.out.println(req);
        return userService.updateUserInfo(req);
    }
    //管理员修改用户信息
    @PreAuthorize("hasAuthority('manage_user')")
    @PutMapping("manage")
    public CommonResp updateByAdmin(@RequestBody adminUpdateUserInfoReq req){
        return userService.adminUpdateUserInfo(req);
    }
    //获取所有用户信息
    @PreAuthorize("hasAuthority('manage_user')")
    @GetMapping("all")
    public CommonResp getAllUser(@RequestParam Integer page){
        return userService.getAllUser(page);
    }
    //重置用户名
    @PreAuthorize("hasAuthority('manage_user')")
    @GetMapping("reset_username")
    public CommonResp resetUsername(@RequestParam Long id){
        return userService.resetUsername(id);
    }
    //重置头像
    @PreAuthorize("hasAuthority('manage_user')")
    @GetMapping("reset_avatar")
    public CommonResp resetAvatar(@RequestParam Long id){
        return userService.resetAvatar(id);
    }
}
