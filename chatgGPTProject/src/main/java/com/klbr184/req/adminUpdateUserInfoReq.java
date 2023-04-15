package com.klbr184.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-13 22:58:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class adminUpdateUserInfoReq {
    private Long id;
    private List<Integer> roles;
    private Long coin;
    private String banMsg;
    private Byte state;
}
