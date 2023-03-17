package com.klbr184.resp;

import lombok.Data;
import lombok.ToString;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-17 01:03:20
 */
@Data
public class CommonResp<T> {
    private Boolean success = true;
    private String Message;
    private T content;
}
