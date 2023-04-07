package com.klbr184.vo;

import com.klbr184.entity.AiSetting;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-08 01:46:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AiSettingsListVo {
    //当前页
    private Integer page;
    //总页数
    private Integer totalPage;
    //内容
    private List<?> list;
}
