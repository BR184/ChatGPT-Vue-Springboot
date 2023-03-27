package com.klbr184.entity;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * (Chat)表实体类
 *
 * @author makejava
 * @since 2023-03-27 11:40:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Chat implements Serializable{
    //发言ID
    @TableId(type = IdType.AUTO)
    private Long id;
    //聊天ID
    private Long chatId;
    //内容发送者
    private String chatSide;
    //聊天内容
    private String chatContent;
    //聊天发送时间
    private Date chatDate;
    //0正常1删除
    private String deleted;
}

