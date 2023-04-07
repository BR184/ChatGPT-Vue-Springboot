package com.klbr184.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.klbr184.entity.AiSetting;
import com.klbr184.vo.AiSettingsVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-07 16:46:15
 */
@Mapper
public interface AiSettingMapper extends BaseMapper<AiSetting> {
    List<AiSettingsVo> selectListByUserId(Long userId);
}
