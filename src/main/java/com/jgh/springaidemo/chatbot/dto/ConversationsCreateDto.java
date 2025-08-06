package com.jgh.springaidemo.chatbot.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConversationsCreateDto implements Serializable {


    /**
     * 标题
     */
    private String title;


}
