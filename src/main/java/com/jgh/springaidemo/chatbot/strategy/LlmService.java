package com.jgh.springaidemo.chatbot.strategy;

import com.jgh.springaidemo.chatbot.dto.AiChatResponse;
import com.jgh.springaidemo.chatbot.dto.ChatRequest;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @Author: JiuGHim
 * @CreateTime: 2025-08-06
 * @Description: llm对话接口
 * @Version: 1.0
 */
public interface LlmService {

    /**
     * 获取服务提供商类型
     */
    String getProviderType();

    /**
     * 获取支持的模型列表
     */
    List<String> getSupportedModels();

    /**
     * 同步聊天
     */
    AiChatResponse chat(ChatRequest request, String sessionId);

    /**
     * 流式聊天
     */
    Flux<AiChatResponse> chatStream(ChatRequest request, String session);


    /**
     * 检查服务是否可用
     */
    boolean isAvailable();


}
