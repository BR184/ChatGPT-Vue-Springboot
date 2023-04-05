package com.klbr184.controller;

import com.klbr184.req.SendMsgReq;
import com.klbr184.resp.CommonResp;
import com.klbr184.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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

    //获取所有聊天记录
    @PreAuthorize("hasAuthority('default')")
    @GetMapping("all")
    public CommonResp getChats() {
        return chatService.getChats();
    }
    //获取排除system的聊天记录
    @PreAuthorize("hasAuthority('default')")
    @PostMapping("all")
    public CommonResp getChatsExcludeSystem() {
        return chatService.getChatsExcludeSystem();
    }
    //获取聊天列表
    @PreAuthorize("hasAuthority('default')")
    @GetMapping("list")
    public CommonResp getChatsList() {
        return chatService.getChatsList();
    }
    //获取聊天参数
    @PreAuthorize("hasAuthority('default')")
    @GetMapping("info")
    public CommonResp getChatsInfoById(@RequestParam Long id) {
        return chatService.getChatsInfoById(id);
    }
    //获取聊天记录通过chatId
    @PreAuthorize("hasAuthority('default')")
    @GetMapping
    public CommonResp getChatsById(@RequestParam Long id) {
        return chatService.getChatsById(id);
    }
    //创建新聊天
    @PreAuthorize("hasAuthority('default')")
    @GetMapping("new")
    public CommonResp addNewChat(String title) {
        return chatService.addNewChat(title);
    }
    //发送消息
    @PreAuthorize("hasAuthority('default')")
    @PostMapping
    public CommonResp sendChat(@RequestBody SendMsgReq sendMsgReq) {
        return chatService.sendChat(sendMsgReq);
    }
    //删除聊天记录
    @PreAuthorize("hasAuthority('default')")
    @DeleteMapping
    public CommonResp deleteChat(@RequestParam Long id) {
        return chatService.deleteChat(id);
    }

}
