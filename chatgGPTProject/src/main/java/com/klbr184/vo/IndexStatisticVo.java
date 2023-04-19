package com.klbr184.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-19 01:15:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndexStatisticVo {
    //今日新增设定数
    private Integer totalAiSettingNum;
    //今日新增聊天数a
    private Integer totalNewChatNum;
    //今日新增分享数
    private Integer totalNewShareNum;
}
