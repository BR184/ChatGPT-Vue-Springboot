package com.klbr184.service;

import com.klbr184.req.SendMsgReq;
import com.klbr184.resp.CommonResp;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-27 11:57:18
 */
public interface ChatService {
    CommonResp getChats();

    CommonResp addNewChat();

    CommonResp sendChat(SendMsgReq sendMsgReq);

    CommonResp getChatsById(Long id);

    CommonResp getChatsInfoById(Long id);
}