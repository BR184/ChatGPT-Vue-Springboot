package com.klbr184.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.klbr184.entity.LoginUser;
import com.klbr184.entity.UserEntity;
import com.klbr184.entity.UserRole;
import com.klbr184.mapper.UserMapper;
import com.klbr184.mapper.UserRoleMapper;
import com.klbr184.req.UpdateUserInfoReq;
import com.klbr184.req.UserAuthReq;
import com.klbr184.req.UserSaveReq;
import com.klbr184.resp.CommonResp;
import com.klbr184.service.UserService;
import com.klbr184.utils.JwtUtil;
import com.klbr184.utils.RedisCache;
import com.klbr184.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-17 00:36:05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {
    @Resource
    private UserMapper userMapper;
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Override
    public CommonResp register(UserSaveReq req) {
        UserEntity userEntity = BeanUtil.copyProperties(req, UserEntity.class);
        if (ObjectUtils.isEmpty(selectByUsername(userEntity.getUsername()))) {
            Long userId =IdUtil.getSnowflakeNextId();
            userEntity.setId(userId);
            userEntity.setHead("default.png");
            userMapper.insert(userEntity);
            //注册成功后默认给用户添加一个普通用户的角色
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(2);
            userRoleMapper.insert(userRole);
        }
        return new CommonResp<>(200, "注册成功,请登录！", null);
    }

    //TODO 实现邮箱可以当做用户名登陆
    @Override
    public CommonResp<Map> login(UserAuthReq req) {
        Authentication authenticate;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword());
            authenticate = authenticationConfiguration.getAuthenticationManager().authenticate(authenticationToken);
        } catch (Exception e) {
            throw new RuntimeException("登陆失败");
        }
        LoginUser user = (LoginUser) authenticate.getPrincipal();
        String userID = user.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userID);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        redisCache.setCacheObject("login:" + userID, user);
        return new CommonResp<>(200, "登陆成功", map);
    }

    @Override
    public CommonResp logout() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser user = (LoginUser) authentication.getPrincipal();
        Long id = user.getUser().getId();
        redisCache.deleteObject("login:" + id);
        return new CommonResp<>(200, "注销成功!", null);
    }

    @Override
    public CommonResp getUser() {
        Long userId = SecurityUtil.getUserId();
        UserEntity user = userMapper.selectById(userId);
        user.setPassword(null);
        return new CommonResp(200, "查询成功", user);
    }

    @Override
    public CommonResp updateUserInfo(UpdateUserInfoReq req) {
        //获取当前用户
        UserEntity user = userMapper.selectById(SecurityUtil.getUserId());
        //如果是更改用户名
        if (req.getUsername() != null && !req.getUsername().equals("")) {
            //判断密码是否正确
            if (passwordEncoder.matches(req.getPassword(),SecurityUtil.getPassword())) {
                //判断用户名是否与原来的一样
                if (req.getUsername().equals(user.getUsername())) {
                    return new CommonResp(400, "用户名与原用户名一致！", null);
                } else {
                    //判断用户名是否已存在
                    if (Objects.nonNull(selectByUsername(req.getUsername()))) {
                        return new CommonResp(400, "用户名已存在", null);
                    } else {
                        UserEntity newUser = UserEntity.builder()
                                .id(SecurityUtil.getUserId())
                                .username(req.getUsername())
                                .build();
                        userMapper.updateById(newUser);
                        return new CommonResp(200, "用户名修改成功", null);
                    }
                }
            } else {
                return new CommonResp(400, "密码错误", null);
            }
        }
        //如果是更改密码
        else if (req.getNewPassword() != null && !req.getNewPassword().equals("")) {
            //判断密码是否正确
            if (passwordEncoder.matches(req.getPassword(),SecurityUtil.getPassword())) {
                //判断新密码是否与原密码一样
                if (passwordEncoder.matches(req.getNewPassword(),SecurityUtil.getPassword())) {
                    return new CommonResp(400, "新密码与原密码一致！", null);
                } else {
                    UserEntity newUser = UserEntity.builder()
                            .id(SecurityUtil.getUserId())
                            .password(passwordEncoder.encode(req.getNewPassword()))
                            .build();
                    userMapper.updateById(newUser);
                    //准备注销
                    //移除redis中的用户信息
                    redisCache.deleteObject("login:" + SecurityUtil.getUserId());
                    return new CommonResp(200, "密码修改成功,请重新登陆！", null);
                }
            } else {
                return new CommonResp(400, "密码错误", null);
            }
        }
        //如果是更改邮箱
        else if (req.getEmail() != null && !req.getEmail().equals("")) {
            //判断密码是否正确
            if (passwordEncoder.matches(req.getPassword(),SecurityUtil.getPassword())) {
                //判断邮箱是否与原来的一样
                if (req.getEmail().equals(user.getEmail())) {
                    return new CommonResp(400, "邮箱与原邮箱一致！", null);
                } else {
                    UserEntity newUser = UserEntity.builder()
                            .id(SecurityUtil.getUserId())
                            .email(req.getEmail())
                            .build();
                    userMapper.updateById(newUser);
                    return new CommonResp(200, "邮箱修改成功", null);
                }
            } else {
                return new CommonResp(400, "密码错误", null);
            }
        }
        return new CommonResp(400, "修改失败", null);
    }

    public UserEntity selectByUsername(String username) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserEntity::getUsername, username);
        List<UserEntity> userEntityList = userMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(userEntityList)) {
            return null;
        } else {
            return userEntityList.get(0);
        }
    }
}
