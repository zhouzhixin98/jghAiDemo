package com.jgh.springaidemo.chatbot.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 聊天响应DTO
 */
@Data
@Accessors(chain = true)
public class AiChatResponse {

    /**
     * reasoning--推理内容，text--正文
     */
    private String recordType;
    
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 响应内容
     */
    private String content;
    
    /**
     * 是否完成（流式输出时使用）
     */
    private boolean finished;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 消息ID
     */
    private String messageId;
    
    /**
     * 对话ID
     */
    private String conversationId;
    
    /**
     * 使用的模型名称
     */
    private String modelName;
    
    /**
     * Token使用量
     */
    private Integer tokenUsage;
    
    /**
     * 响应时间（毫秒）
     */
    private Long responseTime;

    /**
     * 创建成功响应
     */
    public static AiChatResponse success(String content, boolean finished,String recordType) {
        return new AiChatResponse()
                .setSuccess(true)
                .setContent(content)
                .setFinished(finished)
                .setRecordType(recordType);
    }

    /**
     * 创建错误响应
     */
    public static AiChatResponse error(String errorMessage) {
        return new AiChatResponse()
                .setSuccess(false)
                .setErrorMessage(errorMessage)
                .setFinished(true);
    }
}