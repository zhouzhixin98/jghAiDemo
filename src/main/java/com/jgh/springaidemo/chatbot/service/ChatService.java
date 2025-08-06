package com.jgh.springaidemo.chatbot.service;

import com.jgh.springaidemo.chatbot.dto.AiChatResponse;
import com.jgh.springaidemo.chatbot.dto.ChatRequest;
import reactor.core.publisher.Flux;

/**
 * @Author: JiuGHim
 * @CreateTime: 2025-08-06
 * @Description: 智能对话
 * @Version: 1.0
 */
public interface ChatService {

    /**
     * 流式聊天
     *
     * @param request
     * @return
     */
    Flux<AiChatResponse> chatStream(ChatRequest request);

}
