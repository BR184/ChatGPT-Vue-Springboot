package com.klbr184;

import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.ChatChoice;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.unfbx.chatgpt.interceptor.OpenAiResponseInterceptor;
import com.unfbx.chatgpt.sse.ConsoleEventSourceListener;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-29 23:33:59
 */
@SpringBootTest
public class ChatTest {
//    @Test
//    public void connectionTest() {
//
//        OkHttpClient client = new OkHttpClient.Builder()
//                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890)))
////                .proxyAuthenticator(new Authenticator() {
////                    @Nullable
////                    @Override
////                    public Request authenticate(@Nullable Route route, @NotNull Response response) throws IOException {
////                        String credential = Credentials.basic("", "9e1b61f6-81dc-4698-8ce1-f88d1d37c2c2");
////                        return response.request().newBuilder()
////                                .header("Proxy-Authorization", credential)
////                                .build();
////
////                    }
////                })
//                .build();
//        MediaType mediaType = MediaType.parse("application/json");
//        String content = "\"user:我是孙悟空\",\"assistant:你好,悟空\",\"user:今天师傅有没有被抓走？\"";
//        RequestBody body = RequestBody.create(mediaType, "{\"model\":\"gpt-3.5-turbo\",\"system\":\"你是一个小助手\",\"message\":[" + content + "],\"temperature\":\"0.9\"}");
//        Request request = new Request.Builder()
//                .url("https://api.openai.com/v1/completions")
//                .method("POST", body)
//                .addHeader("Authorization", "Bearer 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")
//                .addHeader("Content-Type", "application/json")
//                .build();
//        Response response;
//
//        {
//            try {
//                response = client.newCall(request).execute();
//                System.out.println(response);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
    @Test
    public void testGptJava(){


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
            Message message = Message.builder().role(Message.Role.USER).content("你好啊我的伙伴！").build();
            ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message)).build();
            ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);
            chatCompletionResponse.getChoices().forEach(e -> {
                System.out.println(e.getMessage());
                System.out.println(chatCompletionResponse.getUsage());
                System.out.println(chatCompletionResponse.getCreated());
                System.out.println(chatCompletionResponse.getId());
                System.out.println(chatCompletionResponse.getModel());
            });
        } catch (Exception e) {
            System.out.println("error detected");
            throw new RuntimeException(e);
        }


    }
}
