package com.klbr184.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-13 20:41:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String username;
    private String email;
    private String head;
    private Long coin;
    private String lastLogin;
    private String regTime;
    private String banMsg;
    private Byte state;
}
