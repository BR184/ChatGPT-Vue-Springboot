package com.klbr184.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (UserRole)表实体类
 *
 * @author makejava
 * @since 2023-03-18 17:41:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRole implements Serializable{
    //关联用户的ID

    private Long userId;
    //关联角色的ID
    private Integer roleId;
}

