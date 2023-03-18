package com.klbr184.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Permission)表实体类
 *
 * @author makejava
 * @since 2023-03-18 17:41:00
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Permission implements Serializable{
    //权限ID
    private Integer id;
    //权限名字(前端显示)
    private String permissionName;
    //权限名字(后端调用)
    private String permissionKey;
    //启用:0,禁用:1
    private String state;
}

