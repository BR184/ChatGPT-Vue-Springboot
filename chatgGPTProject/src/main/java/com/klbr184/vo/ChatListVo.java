package com.klbr184.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author KL
 * @version 1.0
 * @since 2023-04-03 11:47:57
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatListVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long chatID;
    @Builder.Default
    private String title = "新的对话";
}
