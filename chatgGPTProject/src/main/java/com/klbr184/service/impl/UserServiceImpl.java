package com.klbr184.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.klbr184.entity.UserEntity;
import com.klbr184.dao.UserDao;
import com.klbr184.req.UserSaveReq;
import com.klbr184.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-17 00:36:05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService{
    @Resource
    private UserDao userDao;

    @Override
    public void register(UserSaveReq req) {
        UserEntity userEntity = BeanUtil.copyProperties(req, UserEntity.class);
        if (ObjectUtils.isEmpty(selectByUsername(userEntity.getUsername()))){
            userDao.insert(userEntity);
        }
    }

    public UserEntity selectByUsername(String username){
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserEntity::getUsername,username);
        List<UserEntity> userEntityList = userDao.selectList(wrapper);
        if(CollectionUtils.isEmpty(userEntityList)){
            return null;
        }else{
            return userEntityList.get(0);
        }
    }
}
