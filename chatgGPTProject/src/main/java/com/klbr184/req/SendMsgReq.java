package com.klbr184.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-31 20:58:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMsgReq {
    private Long id;
    private String system;
    private String message;
    private Float temperature;
    private Float top_p;
    private Float presence_penalty;
    private Float frequency_penalty;
}
