package com.klbr184.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.klbr184.entity.Chat;
import com.klbr184.entity.UserChat;
import com.klbr184.vo.ChatVo;
import com.klbr184.vo.InfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-27 11:41:14
 */
@Mapper
public interface ChatMapper extends BaseMapper<Chat> {
    List<ChatVo> selectChatByUserID(Long userID);
    List<ChatVo> selectChatByChatID(Long chatID,Long userID);
    InfoVo selectChatInfoByChatID(Long chatID, Long userID);
}
