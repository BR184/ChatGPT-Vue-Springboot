package com.klbr184.vo;

import com.klbr184.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-11 18:32:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleBeanVo {
    private Integer page;
    private Integer totalpage;
    private List<Role> data;
}
