package com.klbr184.utils;

import com.klbr184.mapper.ChatMapper;
import com.klbr184.vo.ChatVo;
import com.unfbx.chatgpt.entity.chat.Message;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-02 01:36:58
 */
public class MessageBuilderUtil {

    public static List<Message> buildMessage(List<ChatVo> chatVos,String newMessage){
        Collections.sort(chatVos, new Comparator<ChatVo>() {
            @Override
            public int compare(ChatVo o1, ChatVo o2) {
                return o1.getChatDate().compareTo(o2.getChatDate());
            }
        });
        //构建messageList
        List<Message> messageList = new ArrayList<>();
        //根据chatVos构建message
        for (ChatVo chatVo : chatVos) {
            //如果是用户发送的消息
            if (chatVo.getChatSide().equals("user")){
                Message message = Message.builder().role(Message.Role.USER).content(chatVo.getChatContent()).build();
                messageList.add(message);
            }
            //如果是机器人发送的消息
            else if (chatVo.getChatSide().equals("gpt")){
                Message message = Message.builder().role(Message.Role.ASSISTANT).content(chatVo.getChatContent()).build();
                messageList.add(message);
            }
            //如果是系统发送的消息
            else if (chatVo.getChatSide().equals("system")){
                Message message = Message.builder().role(Message.Role.SYSTEM).content(chatVo.getChatContent()).build();
                messageList.add(message);
            }

        }
        //添加新的消息
        Message message = new Message();
        message.setRole("user");
        message.setContent(newMessage);
        messageList.add(message);
        //返回messageList
        return messageList;
    }
}
