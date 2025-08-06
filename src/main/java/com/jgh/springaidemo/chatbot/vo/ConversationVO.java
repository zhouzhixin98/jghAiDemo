package com.jgh.springaidemo.chatbot.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ConversationVO implements Serializable {
    /**
     * 对话ID
     */
    private String id;

    /**
     * 对话标题
     */
    private String title;


    /**
     * 对话状态
     */
    private String status;


    /**
     * 消息总数
     */
    private Integer messageCount;

    /**
     * 最后一条消息时间
     */
    private LocalDateTime lastMessageTime;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 消息列表（可选）
     */
    private List<MessageVO> messages;

}
