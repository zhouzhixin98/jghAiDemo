package com.jgh.springaidemo.chatbot.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 聊天请求DTO
 */
@Data
@Accessors(chain = true)
public class ChatRequest {
    
    /**
     * 对话ID（新建对话时可为空）
     */
    private String conversationId;

    
    /**
     * 用户消息
     */
//    @NotBlank(message = "消息内容不能为空")
    private String message;

    /**
     * 是否开启深度思考
     */
    private Boolean enableThinking;

    /**
     * 是否联网搜索
     */
    private Boolean enableSearch;

    /**
     * 文件url
     */
    private String fileUrl;


}