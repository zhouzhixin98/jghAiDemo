package com.jgh.springaidemo.chatbot.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 消息视图对象
 */
@Data
@Accessors(chain = true)
public class MessageVO {
    
    /**
     * 消息ID
     */
    private String id;

    private String messageId;
    /**
     * 对话ID
     */
    private String conversationId;
    
    /**
     * 消息角色
     */
    private String role;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 消息类型
     */
    private String messageType;
    
    /**
     * 使用的模型类型
     */
    private String modelType;
    
    /**
     * 使用的模型名称
     */
    private String modelName;
    
    /**
     * 消息状态
     */
    private String status;
    
    /**
     * 父消息ID
     */
    private String parentId;
    
    /**
     * token使用量
     */
    private Integer tokenUsage;
    
    /**
     * 响应时间（毫秒）
     */
    private Long responseTime;
    
    /**
     * 消息序号
     */
    private Integer sequence;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}