package com.klbr184.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KL
 * @version 1.0
 * @since 2023-03-29 16:34:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bot {
    private String model;
    private String messageOfSystem;
    private List messagesOfAssistant;
    private List messagesOfUser;
    private Float temperature;
    private Float top_p;
    private Float n;
    private Float presence_penalty;
    private Float frequency_penalty;
}
