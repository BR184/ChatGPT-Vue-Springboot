package com.klbr184.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.klbr184.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-17 01:41:30
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {
}