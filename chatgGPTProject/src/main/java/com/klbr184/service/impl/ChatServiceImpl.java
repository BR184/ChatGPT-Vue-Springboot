package com.klbr184.service.impl;

import cn.hutool.Hutool;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.klbr184.entity.Chat;
import com.klbr184.entity.UserChat;
import com.klbr184.mapper.ChatMapper;
import com.klbr184.mapper.UserChatMapper;
import com.klbr184.resp.CommonResp;
import com.klbr184.service.ChatService;
import com.klbr184.utils.SecurityUtil;
import com.klbr184.vo.ChatVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-27 11:57:42
 */
@Service
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    private UserChatMapper userChatMapper;
    @Override
    public CommonResp getChats() {
        List<ChatVo> list = chatMapper.selectChatByUserID(SecurityUtil.getUserId());
        return new CommonResp<List>(200,"操作成功",list);
    }

    @Override
    public CommonResp addNewChat() {
        Long chatID = IdUtil.getSnowflakeNextId();
        UserChat userChat = new UserChat();
        userChat.setUserId(SecurityUtil.getUserId());
        userChat.setChatId(chatID);
        userChatMapper.insert(userChat);
        Chat chat = new Chat();
        chat.setChatId(chatID)
                .setChatSide("system")
                .setChatContent("You are a AI helper")
                .setChatDate(DateUtil.date());
        chatMapper.insert(chat);
        return new CommonResp<>(200,"操作成功",null);
    }
}
