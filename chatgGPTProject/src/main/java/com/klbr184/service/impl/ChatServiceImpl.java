package com.klbr184.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.klbr184.entity.Chat;
import com.klbr184.entity.UserChat;
import com.klbr184.mapper.ChatMapper;
import com.klbr184.mapper.UserChatMapper;
import com.klbr184.req.SendMsgReq;
import com.klbr184.resp.CommonResp;
import com.klbr184.service.ChatService;
import com.klbr184.utils.SecurityUtil;
import com.klbr184.vo.ChatVo;
import com.klbr184.vo.InfoVo;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.unfbx.chatgpt.interceptor.OpenAiResponseInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    public CommonResp getChatsById(Long id) {
        List<ChatVo> list = chatMapper.selectChatByChatID(id,SecurityUtil.getUserId());
        return new CommonResp<List>(200,"操作成功",list);
    }

    @Override
    public CommonResp getChatsInfoById(Long id) {
        InfoVo infoVo = chatMapper.selectChatInfoByChatID(id,SecurityUtil.getUserId());
        return new CommonResp<>(200,"操作成功",infoVo);
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

    @Override
    public CommonResp sendChat(SendMsgReq sendMsgReq) {
        System.out.println(sendMsgReq.toString());
        //数据库调整基本数据
        InfoVo infoVo = chatMapper.selectChatInfoByChatID(sendMsgReq.getId(), SecurityUtil.getUserId());
        System.out.println(infoVo.toString());
        UserChat userChat = new UserChat();
        BeanUtil.copyProperties(sendMsgReq,userChat);
        System.out.println(userChat.toString());
//        try {
//            //国内访问需要做代理，国外服务器不需要
//            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
//            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
//            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//            OkHttpClient okHttpClient = new OkHttpClient
//                    .Builder()
//                    .proxy(proxy)//自定义代理
//                    .addInterceptor(new OpenAiResponseInterceptor())//自定义返回值拦截
//                    .connectTimeout(10, TimeUnit.SECONDS)//自定义超时时间
//                    .writeTimeout(30, TimeUnit.SECONDS)//自定义超时时间
//                    .readTimeout(30, TimeUnit.SECONDS)//自定义超时时间
//                    .build();
//            //构建客户端
//            OpenAiClient openAiClient = OpenAiClient.builder()
//                    .apiKey(Arrays.asList("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"))
//                    .okHttpClient(okHttpClient)
//                    .build();
//            //聊天模型：gpt-3.5
//            Message message = Message.builder().role(Message.Role.USER).content("你好啊我的伙伴！").build();
//            ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message)).build();
//            ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);
//            chatCompletionResponse.getChoices().forEach(e -> {
//                System.out.println(e.getMessage());
//                System.out.println(chatCompletionResponse.getUsage());
//                System.out.println(chatCompletionResponse.getCreated());
//                System.out.println(chatCompletionResponse.getId());
//                System.out.println(chatCompletionResponse.getModel());
//            });
//        } catch (Exception e) {
//            System.out.println("error detected");
//            throw new RuntimeException(e);
//        }
        return null;
    }

}
