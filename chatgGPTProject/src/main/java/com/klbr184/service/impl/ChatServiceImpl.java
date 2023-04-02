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
import com.klbr184.utils.MessageBuilderUtil;
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
        //获取数据库原本数据
        InfoVo oldInfoVo = chatMapper.selectChatInfoByChatID(sendMsgReq.getId(), SecurityUtil.getUserId());
        //查看system有无更改，有更改则更新
        if(oldInfoVo.getSystem().equals(sendMsgReq.getSystem())){}
        else {
            Chat newC = new Chat();
            newC.setChatContent(sendMsgReq.getSystem());
            newC.setId(oldInfoVo.getId());
            chatMapper.updateById(newC);
        }
        //更新user_chat里的内容
        UserChat newUC = new UserChat();
        BeanUtil.copyProperties(sendMsgReq,newUC, "system","message");
        newUC.setChatId(oldInfoVo.getChatId());
        userChatMapper.updateById(newUC);
        List<ChatVo> chatVo = chatMapper.selectChatByChatID(sendMsgReq.getId(),SecurityUtil.getUserId());
        //获取数据库新的数据
        InfoVo newInfoVo = chatMapper.selectChatInfoByChatID(sendMsgReq.getId(), SecurityUtil.getUserId());
        //使用MessageBuilderUtil工具类构建Message
        List<Message> messageList = MessageBuilderUtil.buildMessage(chatVo, sendMsgReq.getMessage());
        //TODO 把数据库里的内容拿出来依次填入以下地方
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
                    .writeTimeout(30, TimeUnit.SECONDS)//自定义超时时间
                    .readTimeout(30, TimeUnit.SECONDS)//自定义超时时间
                    .build();
            //构建客户端
            OpenAiClient openAiClient = OpenAiClient.builder()
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
            //把chatCompletionResponse返回的内容存入数据库
            //先是gpt回复的消息
            Chat responseChat = new Chat();
            responseChat.setChatId(sendMsgReq.getId())
                    .setChatSide("gpt")
                    .setChatContent(chatCompletionResponse.getChoices().get(0).getMessage().getContent())
                    .setChatDate(DateUtil.offsetSecond(DateUtil.date(),1));
            //再是user_chat里的内容：object,消息ID,tokens的占用情况
            UserChat responseUC = UserChat.builder()
                    .chatId(sendMsgReq.getId())
                    .build();
            System.out.println(chatCompletionResponse.toString());
        } catch (Exception e) {
            System.out.println("error detected");
            throw new RuntimeException(e);
        }



        return null;
    }

}
