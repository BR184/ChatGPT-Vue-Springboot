package com.klbr184.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.klbr184.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-17 00:38:13
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {
}
