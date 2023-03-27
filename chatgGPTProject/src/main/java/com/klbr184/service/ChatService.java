package com.klbr184.service;

import com.klbr184.resp.CommonResp;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-27 11:57:18
 */
public interface ChatService {
    CommonResp getChats();

    CommonResp addNewChat();
}
