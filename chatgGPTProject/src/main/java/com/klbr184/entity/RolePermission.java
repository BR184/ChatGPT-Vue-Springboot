package com.klbr184.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (RolePermission)表实体类
 *
 * @author makejava
 * @since 2023-03-18 17:41:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolePermission implements Serializable{
    //关联角色ID
    private Integer roleId;
    //关联权限ID
    private Integer permissionId;
}
