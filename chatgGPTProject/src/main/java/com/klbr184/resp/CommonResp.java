package com.klbr184.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-17 01:03:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResp<T> {
    private Integer code = 200;
    private String Message;
    private T content;
}
