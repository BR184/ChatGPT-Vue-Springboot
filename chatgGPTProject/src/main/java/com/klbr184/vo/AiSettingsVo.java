package com.klbr184.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-07 20:45:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiSettingsVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    private String username;
    private String intro;
    private String value;
    private Integer shared;
    private Integer fav;
    private Date createTime;
    private Date updateTime;
}
