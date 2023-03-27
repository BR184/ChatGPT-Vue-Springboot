package com.klbr184.controller;

import com.klbr184.resp.CommonResp;
import com.klbr184.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-27 11:48:40
 */

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private ChatService chatService;

    @PreAuthorize("hasAuthority('default')")
    @GetMapping("all")
    public CommonResp getChats(){
        return chatService.getChats();
    }

    @PreAuthorize("hasAuthority('default')")
    @GetMapping("new")
    public CommonResp addNewChat(){
        return  chatService.addNewChat();
    }
}
