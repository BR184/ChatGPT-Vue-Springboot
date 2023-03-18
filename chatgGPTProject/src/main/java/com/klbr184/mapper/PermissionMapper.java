package com.klbr184.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.klbr184.entity.Permission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-18 17:45:04
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    List<String> selectPermissionsByUserID(long userID);
}
