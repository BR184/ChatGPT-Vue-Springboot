package com.klbr184.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-31 20:58:52
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoVo {
    @Builder.Default
    private String system = "You are a AI helper";
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
    //OpenAI API中的ID
    private String msId;
    //OpenAI API中的Object
    private String object;
    //OpenAI API中usage下的prompt_tokens
    private Integer usagePromptTokens;
    //OpenAI API中usage下的completion_tokens
    private Integer usageCompletionTokens;
    //OpenAI API中usage下的total_tokens
    private Integer usageTotalTokens;
}
