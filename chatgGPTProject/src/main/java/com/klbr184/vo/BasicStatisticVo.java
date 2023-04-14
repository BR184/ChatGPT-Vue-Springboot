package com.klbr184.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-15 01:48:45
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicStatisticVo {
    //今日新增用户
    private Integer todayNewUser;
    //今日新增设定数
    private Integer todayNewAiSetting;
    //今日新增聊天数
    private Integer todayNewChat;
    //已登陆用户数
    private Integer loginUsers;
}
