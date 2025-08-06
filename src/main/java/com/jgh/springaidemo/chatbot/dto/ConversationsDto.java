package com.jgh.springaidemo.chatbot.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConversationsDto implements Serializable {

    private Long pageNum;

    private Long pageSize;

    private String keyword;

}
