package com.klbr184.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.klbr184.entity.LoginUser;
import com.klbr184.entity.Role;
import com.klbr184.entity.UserEntity;
import com.klbr184.entity.UserRole;
import com.klbr184.exception.SystemException;
import com.klbr184.mapper.RoleMapper;
import com.klbr184.mapper.UserMapper;
import com.klbr184.mapper.UserRoleMapper;
import com.klbr184.req.UpdateUserInfoReq;
import com.klbr184.req.UserAuthReq;
import com.klbr184.req.UserSaveReq;
import com.klbr184.req.adminUpdateUserInfoReq;
import com.klbr184.resp.CommonResp;
import com.klbr184.service.RoleService;
import com.klbr184.service.UserService;
import com.klbr184.utils.JwtUtil;
import com.klbr184.utils.RedisCache;
import com.klbr184.utils.SecurityUtil;
import com.klbr184.vo.UserPageVo;
import com.klbr184.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-17 00:36:05
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
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
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleService roleService;
    @Value("${oss.bucketName}")
    private String bucketName;
    @Value("${oss.endpoint}")
    private String endpoint;

    @Override
    public CommonResp register(UserSaveReq req) {
        UserEntity userEntity = BeanUtil.copyProperties(req, UserEntity.class);
        //去除用户名前后空格
        userEntity.setUsername(userEntity.getUsername().trim());
        //去除邮箱前后空格
        userEntity.setEmail(userEntity.getEmail().trim());
        if (ObjectUtils.isEmpty(selectByUsername(userEntity.getUsername()))) {
            Long userId = IdUtil.getSnowflakeNextId();
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
            throw new RuntimeException("登陆失败,用户名或密码错误！");
        }
        LoginUser user = (LoginUser) authenticate.getPrincipal();
        String userID = user.getUser().getId().toString();
        UserEntity userEntity = user.getUser();
        //如果用户被封禁
        if (userEntity.getState() == 1) {
            throw new SystemException(400, "该用户已被封禁！理由：" + userEntity.getBanMsg()+"，如有疑问，请联系管理员！");
        }
        userEntity.setId(Long.valueOf(userID));
        userEntity.setLastLogin(DateUtil.date());
        userMapper.updateById(userEntity);
        Map<String, String> map = new HashMap<>();
        String jwt = JwtUtil.createJWT(userID);
        map.put("token", jwt);
        redisCache.setCacheObject("login:" + userID, user,3600*30, TimeUnit.SECONDS);
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
            if (passwordEncoder.matches(req.getPassword(), SecurityUtil.getPassword())) {
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
            if (passwordEncoder.matches(req.getPassword(), SecurityUtil.getPassword())) {
                //判断新密码是否与原密码一样
                if (passwordEncoder.matches(req.getNewPassword(), SecurityUtil.getPassword())) {
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
            if (passwordEncoder.matches(req.getPassword(), SecurityUtil.getPassword())) {
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

    @Override
    public CommonResp getAllUser(Integer page) {
        if (page == null || page < 1) {
            throw new SystemException(400, "页码错误");
        }
        IPage<UserEntity> userPage = new Page<>(page, 10);
        userMapper.selectPage(userPage, null);
        List<UserEntity> userEntityList = userPage.getRecords();
        List<UserVo> userVoList = new ArrayList<>();
        for (UserEntity userEntity : userEntityList) {
            UserVo userVo = new UserVo();
            BeanUtil.copyProperties(userEntity, userVo, "password");
            userVo.setHead(formatAvatar(userEntity.getHead()));
            userVoList.add(userVo);
        }
        UserPageVo userPageVo = new UserPageVo(page, (int)userPage.getPages(), userVoList);
        return new CommonResp(200, "操作成功", userPageVo);
    }

    @Override
    public CommonResp adminUpdateUserInfo(adminUpdateUserInfoReq req) {
        Role roleOfUser = roleMapper.selectById(2);
        if ((!req.getRoles().contains(roleOfUser.getId()))&&req.getState()!=1){
            throw new SystemException(400, "除了禁用用户，用户必须包含："+roleOfUser.getRoleName()+" 角色");
        }
        UserEntity user = userMapper.selectById(req.getId());
        if (user == null) {
            throw new SystemException(400, "用户不存在");
        }
        //增加或移除用户角色
        //获取用户角色
        CommonResp commonResp = roleService.getRoleIdByUserID(req.getId());
        List<Integer> userRoleIds = (List<Integer>) commonResp.getData();
        //获取所有角色
        List<Role> allRoles = roleMapper.selectList(null);
        List<Integer> allRoleIds = allRoles.stream().map(Role::getId).collect(Collectors.toList());
        //需要增加的角色和需要移除的角色
        List<Integer> addRoleIds = new ArrayList<>();
        List<Integer> removeRoleIds = new ArrayList<>();
        //遍历所有角色
        for (Integer allRoleId : allRoleIds) {
            //如果用户角色包含所有角色，但是请求中不包含所有角色，需要移除
            if (userRoleIds.contains(allRoleId)&&!req.getRoles().contains(allRoleId)){
                removeRoleIds.add(allRoleId);
            }
            //如果用户角色不包含所有角色，但是请求中包含所有角色，需要增加
            if (!userRoleIds.contains(allRoleId)&&req.getRoles().contains(allRoleId)){
                addRoleIds.add(allRoleId);
            }
        }
        //增加角色
        for (Integer addRoleId : addRoleIds) {
            UserRole newUserRole = new UserRole();
            newUserRole.setUserId(req.getId());
            newUserRole.setRoleId(addRoleId);
            userRoleMapper.insert(newUserRole);
        }
        //移除角色
        for (Integer removeRoleId : removeRoleIds) {
            QueryWrapper<UserRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id",req.getId());
            queryWrapper.eq("role_id",removeRoleId);
            userRoleMapper.delete(queryWrapper);
        }
        //更新用户信息
        UserEntity newUser = new UserEntity();
        BeanUtil.copyProperties(req, newUser,"username");
        userMapper.updateById(newUser);
        return new CommonResp(200, "操作成功", null);
    }

    @Override
    public CommonResp resetUsername(Long id) {
        UserEntity user = userMapper.selectById(id);
        if (user == null) {
            throw new SystemException(400, "用户不存在");
        }
        UserEntity newUser = new UserEntity();
        newUser.setId(id);
        newUser.setUsername("用户" + id);
        userMapper.updateById(newUser);
        //移除redis中的用户信息
        redisCache.deleteObject("login:" + id);
        return new CommonResp<String>(200, "操作成功", "用户" + id);
    }

    @Override
    public CommonResp resetAvatar(Long id) {
        UserEntity user = userMapper.selectById(id);
        if (user == null) {
            throw new SystemException(400, "用户不存在");
        }
        UserEntity newUser = new UserEntity();
        newUser.setId(id);
        newUser.setHead("default.png");
        userMapper.updateById(newUser);
        //移除redis中的用户信息
        redisCache.deleteObject("login:" + id);
        return new CommonResp<String>(200, "操作成功", formatAvatar("default.png"));
    }

    //根据用户名查询用户
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

    //格式化头像链接
    public String formatAvatar(String avatar) {
        if (avatar == null || avatar.equals("")) {
            return null;
        }
        if (avatar.startsWith("http://") || avatar.startsWith("https://")) {
            return avatar;
        } else {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("https://")
                    .append(bucketName)
                    .append(".")
                    .append(endpoint.replace("https://", ""))
                    .append("/avatar/")
                    .append(avatar);
            return stringBuffer.toString();
        }
    }
    //反格式化头像链接
    public String unformatAvatar(String avatar){
        if (avatar == null || avatar.equals("")) {
            return null;
        }
        if (avatar.startsWith("http://") || avatar.startsWith("https://")) {
            String s = avatar.split("/avatar/")[1];
            return s;
        } else {
            return avatar;
        }
    }
}
