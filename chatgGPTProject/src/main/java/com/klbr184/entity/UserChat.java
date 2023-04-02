package com.klbr184.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * (UserChat)表实体类
 *
 * @author makejava
 * @since 2023-03-27 17:01:39
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChat {
    //用户ID
    private Long userId;
    //聊天ID
    @TableId
    private Long chatId;
    //文本随机性(0-2)
    @Builder.Default
    private Float temperature = 1f;
    //文本多样性(0-1)
    @Builder.Default
    private Float topP = 1f;
    //内容重复度(0-1)
    @Builder.Default
    private Float presencePenalty = 0f;
    //字词重复度(0-1)
    @Builder.Default
    private Float frequencyPenalty = 0f;
    //OpenAI API中的Model
    private String model;
    //OpenAI API中usage下的prompt_tokens
    private Integer usagePromptTokens;
    //OpenAI API中usage下的completion_tokens
    private Integer usageCompletionTokens;
    //OpenAI API中usage下的total_tokens
    private Integer usageTotalTokens;
    //OpenAI API中usage下的total_tokens
    private Integer deleted;
}

