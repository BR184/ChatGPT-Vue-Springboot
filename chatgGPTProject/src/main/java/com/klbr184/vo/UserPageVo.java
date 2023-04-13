package com.klbr184.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-13 21:31:55
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPageVo {
    private Integer page;
    private Integer totalpage;
    private List<UserVo> data;
}
