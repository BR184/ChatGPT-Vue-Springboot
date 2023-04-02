package com.klbr184.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-27 11:45:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatVo{
    //内容发送者
    private String chatSide;
    //聊天内容
    private String chatContent;
    //聊天发送时间
    private Date chatDate;

    private int enable;
}