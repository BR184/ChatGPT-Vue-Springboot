package com.klbr184.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.klbr184.entity.UserEntity;
import com.klbr184.dao.UserDao;
import com.klbr184.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-17 00:36:05
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService{
}
