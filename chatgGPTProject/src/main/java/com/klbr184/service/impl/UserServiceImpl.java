package com.klbr184.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.klbr184.entity.LoginUser;
import com.klbr184.entity.UserEntity;
import com.klbr184.mapper.UserMapper;
import com.klbr184.req.UserAuthReq;
import com.klbr184.req.UserSaveReq;
import com.klbr184.resp.CommonResp;
import com.klbr184.service.UserService;
import com.klbr184.utils.JwtUtil;
import com.klbr184.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-17 00:36:05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService{
    @Resource
    private UserMapper userMapper;
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    @Autowired
    private RedisCache redisCache;

    @Override
    public void register(UserSaveReq req) {
        UserEntity userEntity = BeanUtil.copyProperties(req, UserEntity.class);
        if (ObjectUtils.isEmpty(selectByUsername(userEntity.getUsername()))){
            userMapper.insert(userEntity);
        }
    }

    @Override
    public CommonResp<Map> login(UserAuthReq req){
        Authentication authenticate;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(req.getUsername(),req.getPassword());
            authenticate = authenticationConfiguration.getAuthenticationManager().authenticate(authenticationToken);
        } catch (Exception e) {
            throw new RuntimeException("登陆失败");
        }
        LoginUser user = (LoginUser) authenticate.getPrincipal();
        String userID = user.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userID);
        Map<String,String> map = new HashMap<>();
        map.put("token", jwt);
        redisCache.setCacheObject("login:"+userID, user);
        return new CommonResp<>(true, "登陆成功", map);

    }

    @Override
    public CommonResp logout() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser user = (LoginUser) authentication.getPrincipal();
        Long id = user.getUser().getId();
        redisCache.deleteObject("login:"+id);
        return new CommonResp<> (true, "注销成功!",null);
    }

    public UserEntity selectByUsername(String username){
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserEntity::getUsername,username);
        List<UserEntity> userEntityList = userMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(userEntityList)){
            return null;
        }else{
            return userEntityList.get(0);
        }
    }
}
