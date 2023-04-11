package com.klbr184.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Role)表实体类
 *
 * @author makejava
 * @since 2023-03-18 17:41:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role implements Serializable{
    //角色ID
    private Integer id;
    //角色名字(前端显示)
    private String roleName;
    //角色名字(代码调用)
    private String roleKey;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //启用:0,禁用:1
    private String state;
}

