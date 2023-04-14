package com.klbr184.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * (AiSetting)表实体类
 *
 * @author makejava
 * @since 2023-04-07 03:34:23
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class AiSetting implements Serializable{
    //系统设定ID
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    //保存者用户ID
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    //系统设定简介
    private String intro;
    //系统设定内容
    private String value;
    //0:私密 1:共享 2:拷贝
    private Integer shared;
    //收藏数
    private Integer fav;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
    //0:正常 1:删除
    private Integer deleted;
}

