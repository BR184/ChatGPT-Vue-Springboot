package com.klbr184.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.klbr184.entity.Chat;
import com.klbr184.entity.UserChat;
import com.klbr184.exception.SystemException;
import com.klbr184.mapper.ChatMapper;
import com.klbr184.mapper.UserChatMapper;
import com.klbr184.req.SendMsgReq;
import com.klbr184.resp.CommonResp;
import com.klbr184.service.ChatService;
import com.klbr184.utils.MessageBuilderUtil;
import com.klbr184.utils.SecurityUtil;
import com.klbr184.vo.ChatListVo;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-27 11:57:42
 */
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ChatServiceImpl implements ChatService {
    @Autowired
    private ChatMapper chatMapper;
    @Autowired
    private UserChatMapper userChatMapper;

    @Override
    public CommonResp getChats() {
        List<ChatVo> list = chatMapper.selectChatByUserID(SecurityUtil.getUserId());
        return new CommonResp<List>(200, "操作成功", list);
    }

    @Override
    public CommonResp getChatsById(Long id) {
        List<ChatVo> list = chatMapper.selectChatByChatID(id, SecurityUtil.getUserId());
        list.removeIf(chatVo -> chatVo.getChatSide().equals("system"));
        return new CommonResp<List>(200, "操作成功", list);
    }

    @Override
    public CommonResp getChatsInfoById(Long id) {
        InfoVo infoVo = chatMapper.selectChatInfoByChatID(id, SecurityUtil.getUserId());
        return new CommonResp<>(200, "操作成功", infoVo);
    }

    @Override
    public CommonResp getChatsList() {
        List<ChatListVo> chatListVos = chatMapper.selectChatListByUserID(SecurityUtil.getUserId());
        return new CommonResp<>(200, "操作成功", chatListVos);
    }

    @Override
    public CommonResp getChatsExcludeSystem() {
        List<ChatVo> list = chatMapper.selectChatByUserID(SecurityUtil.getUserId());
        list.removeIf(chatVo -> chatVo.getChatSide().equals("system"));
        return new CommonResp<List>(200, "操作成功", list);
    }

    @Override
    public CommonResp deleteChat(Long id) {
        try {
            //判断是否属于该用户
            if (userChatMapper.selectById(id).getUserId().equals(SecurityUtil.getUserId())
                    && chatMapper.selectChatByChatID(id, SecurityUtil.getUserId()).size() > 0) {
                try {
                    userChatMapper.deleteById(id);
                    Map<String,Object> map = new HashMap<>();
                    map.put("chat_ID",id);
                    chatMapper.deleteByMap(map);
                }catch (Exception e) {
                    return new CommonResp<>(500, "操作失败", null);
                }
            }else {
                return new CommonResp<>(403, "操作失败!你没有足够的权限!", null);
            }
            return new CommonResp<>(200, "操作成功", null);

        } catch (Exception e) {
            return new CommonResp<>(500, "操作失败", null);
        }
    }

    @Override
    public CommonResp deleteLastChat(Long id) {
        List<ChatVo> chatVos = chatMapper.selectChatByChatID(id, SecurityUtil.getUserId());
        //新的聊天在前
        chatVos.sort(
                new Comparator<ChatVo>() {
                    @Override
                    public int compare(ChatVo o1, ChatVo o2) {
                        if (o1.getChatDate().getTime() > o2.getChatDate().getTime()) {
                            return -1;
                        } else if (o1.getChatDate().getTime() < o2.getChatDate().getTime()) {
                            return 1;
                        }
                        return 0;
                    }
                }
        );
        Boolean flag = true;
        long[] ids = new long[2];
        for (ChatVo chatVo : chatVos) {
            if (chatVo.getChatSide().equals("system")) {
                return new CommonResp<>(500, "没有更多聊天！", null);
            }
            if(flag && chatVo.getChatSide().equals("gpt")) {
                flag = false;
                ids[0] = chatVo.getId();
            }
            if(flag && chatVo.getChatSide().equals("user")) {
                return new CommonResp<>(500, "聊天顺序有误！请删除整个聊天！", null);
            }
            if(!flag && chatVo.getChatSide().equals("user")) {
                ids[1] = chatVo.getId();
                break;
            }
        }
        try {
            chatMapper.deleteById(ids[0]);
            chatMapper.deleteById(ids[1]);
        }catch (Exception e) {
            return new CommonResp<>(500, "操作失败", null);
        }
        UserChat userChat = userChatMapper.selectById(id);
        userChat.setUsageTotalTokens((int) (userChat.getUsageTotalTokens()*0.9));
        userChatMapper.updateById(userChat);
        return new CommonResp<>(200, "操作成功", null);
    }

    @Override
    public CommonResp regenLastChat(Long id) {
        List<ChatVo> chatVos = chatMapper.selectChatByChatID(id, SecurityUtil.getUserId());
        //对聊天进行排序，新的聊天在前
        chatVos.sort(new Comparator<ChatVo>() {
            @Override
            public int compare(ChatVo o1, ChatVo o2) {
                if (o1.getChatDate().getTime() > o2.getChatDate().getTime()) {
                    return -1;
                } else if (o1.getChatDate().getTime() < o2.getChatDate().getTime()) {
                    return 1;
                }
                return 0;
            }
        });
        //找到最后一条聊天，如果为用户或系统发送的则返回错误
        if (chatVos.get(0).getChatSide().equals("user") || chatVos.get(0).getChatSide().equals("system")) {
            return new CommonResp<>(500, "操作失败", null);
        }
        //找到最后一条聊天，如果为GPT发送的则先删除再重新生成
        if (chatVos.get(0).getChatSide().equals("gpt")) {
            try {
                chatMapper.deleteById(chatVos.get(0).getId());
                //然后重新发送上一条用户发送的聊天
                SendMsgReq sendMsgReq = new SendMsgReq();
                UserChat userChat = userChatMapper.selectById(id);
                if (userChat == null) {
                    throw new SystemException(500, "操作失败");
                }
                //复制类
                BeanUtil.copyProperties(userChat, sendMsgReq);
                for (ChatVo chatVo : chatVos) {
                    if(chatVo.getChatSide().equals("user")){
                        sendMsgReq.setId(id);
                        sendMsgReq.setMessage(chatVo.getChatContent());
                        chatMapper.deleteById(chatVo.getId());
                        break;
                    }
                    if (chatVo.getChatSide().equals("system")) {
                        throw new SystemException(500, "操作失败");
                    }
                }
                if (sendMsgReq.getMessage() == null) {
                    throw new SystemException(500, "操作失败");
                }
                return sendChat(sendMsgReq);
            }catch (Exception e) {
                throw new SystemException(500, "操作失败");
            }
        }
        //最后一条聊天为系统发送的则无法重新生成
        return new CommonResp<>(500, "操作失败", null);
    }

    @Override
    public CommonResp addNewChat(String title) {
        Long chatID = IdUtil.getSnowflakeNextId();
        UserChat userChat = new UserChat();
        userChat.setUserId(SecurityUtil.getUserId());
        userChat.setChatId(chatID);
        userChat.setTitle(title);
        userChat.setCreateTime(DateUtil.date());
        userChatMapper.insert(userChat);
        Chat chat = new Chat();
        chat.setChatId(chatID)
                .setChatSide("system")
                .setChatContent("You are a AI helper")
                .setChatDate(DateUtil.date());
        chatMapper.insert(chat);
        return new CommonResp<>(200, "操作成功", null);
    }

    @Override
    public CommonResp sendChat(SendMsgReq sendMsgReq) {
        //TODO 检测用户余额
        //获取数据库原本数据
        InfoVo oldInfoVo = chatMapper.selectChatInfoByChatID(sendMsgReq.getId(), SecurityUtil.getUserId());
        //查看system有无更改（需要不为空），有更改则更新
        if (oldInfoVo.getSystem().equals(sendMsgReq.getSystem())||sendMsgReq.getSystem()==null) {
        } else {
            Chat newC = new Chat();
            newC.setChatContent(sendMsgReq.getSystem());
            newC.setId(oldInfoVo.getId());
            chatMapper.updateById(newC);
        }
        //更新user_chat里的内容
        UserChat newUC = new UserChat();
        BeanUtil.copyProperties(sendMsgReq, newUC, "system", "message");
        newUC.setChatId(oldInfoVo.getChatId());
        userChatMapper.updateById(newUC);
        //获取数据库聊天内容
        List<ChatVo> chatVo = chatMapper.selectChatByChatID(sendMsgReq.getId(), SecurityUtil.getUserId());
        //获取数据库新的数据
        InfoVo newInfoVo = chatMapper.selectChatInfoByChatID(sendMsgReq.getId(), SecurityUtil.getUserId());
        //使用MessageBuilderUtil工具类构建Message
        List<Message> messageList = MessageBuilderUtil.buildMessage(chatVo, sendMsgReq.getMessage());
        String message;
        try {
            //国内访问需要做代理，国外服务器不需要
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient okHttpClient = new OkHttpClient
                    .Builder()
                    .proxy(proxy)//自定义代理
                    .addInterceptor(new OpenAiResponseInterceptor())//自定义返回值拦截
                    .connectTimeout(10, TimeUnit.SECONDS)//自定义超时时间
                    .writeTimeout(60, TimeUnit.SECONDS)//自定义超时时间
                    .readTimeout(60, TimeUnit.SECONDS)//自定义超时时间
                    .build();
            //构建客户端
            OpenAiClient openAiClient = OpenAiClient.builder()
                    //TODO apiKey相关的数据库操作
                    //TODO 敏感数据
                    .apiKey(Arrays.asList("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"))
                    .okHttpClient(okHttpClient)
                    .build();
            //聊天模型：gpt-3.5
            ChatCompletion chatCompletion = ChatCompletion.builder()
                    .model("gpt-3.5-turbo")
                    .messages(messageList)
                    .temperature(newInfoVo.getTemperature())
                    .topP(Double.valueOf(newInfoVo.getTopP()))
                    .presencePenalty(newInfoVo.getPresencePenalty())
                    .frequencyPenalty(newInfoVo.getFrequencyPenalty())
                    .build();
            ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);
            //把user返回的内容存入数据库
            Chat userChat = new Chat();
            userChat.setChatId(sendMsgReq.getId())
                    .setChatSide("user")
                    .setChatContent(sendMsgReq.getMessage())
                    .setChatDate(DateUtil.date());
            chatMapper.insert(userChat);
            //把chatCompletionResponse返回的内容存入数据库
            //先是gpt回复的消息
            Chat responseChat = new Chat();
            responseChat.setChatId(sendMsgReq.getId())
                    .setChatSide("gpt")
                    .setChatContent(chatCompletionResponse.getChoices().get(0).getMessage().getContent())
                    .setChatDate(DateUtil.offsetSecond(DateUtil.date(), 1));
            chatMapper.insert(responseChat);
            //再是user_chat里的内容：object,消息ID,tokens的占用情况
            UserChat responseUC = UserChat.builder()
                    .chatId(sendMsgReq.getId())
                    .usagePromptTokens((int) chatCompletionResponse.getUsage().getPromptTokens())
                    .usageCompletionTokens((int) chatCompletionResponse.getUsage().getCompletionTokens())
                    .usageTotalTokens((int) chatCompletionResponse.getUsage().getTotalTokens())
                    .build();
            //更新user_chat里的内容
            userChatMapper.updateById(responseUC);
            //消息处理完毕,更新message
            message = chatCompletionResponse.getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //返回gpt发的消息
        return new CommonResp<String>(200, "操作成功", message);
    }
}
