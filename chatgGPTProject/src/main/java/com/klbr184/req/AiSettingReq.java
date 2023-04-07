package com.klbr184.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-07 16:03:26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiSettingReq {
    //系统设定ID
    private Long id;
    //系统设定简介
    private String intro;
    //系统设定内容
    private String value;
    //0:私密 1:共享
    private Integer shared;
}
