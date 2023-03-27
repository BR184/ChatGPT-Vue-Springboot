package com.klbr184.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (UserChat)表实体类
 *
 * @author makejava
 * @since 2023-03-27 17:01:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChat {
    //用户ID
    private Long userId;
    //聊天ID
    private Long chatId;
    //文本随机性(0-2)
    private Float temperature;
    //文本多样性(0-1)
    private Float topP;
    //内容重复度(0-1)
    private Float presencePenalty;
    //字词重复度(0-1)
    private Float frequencyPenalty;
    //OpenAI API中的ID
    private String msId;
    //OpenAI API中的Object
    private String object;
    //OpenAI API中的Model
    private String model;
    //OpenAI API中usage下的prompt_tokens
    private Integer usagePromptTokens;
    //OpenAI API中usage下的completion_tokens
    private Integer usageCompletionTokens;
    //OpenAI API中usage下的total_tokens
    private Integer usageTotalTokens;
}

