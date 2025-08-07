package com.jgh.springaidemo.chatbot.strategy.impl;

import com.jgh.springaidemo.chatbot.dto.AiChatResponse;
import com.jgh.springaidemo.chatbot.dto.ChatRequest;
import com.jgh.springaidemo.chatbot.strategy.LlmService;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: JiuGHim
 * @CreateTime: 2025-08-07
 * @Description: 火山引擎智能对话实现类
 * @Version: 1.0
 */
public class VolcengineLlmServiceImpl implements LlmService {

    private static final List<String> SUPPORTED_MODELS = Arrays.asList(
            "doubao", "deepseek"
    );



    /**
     * 获取服务提供商类型
     */
    @Override
    public String getProviderType() {
        return "volcengine";
    }

    /**
     * 获取支持的模型列表
     */
    @Override
    public List<String> getSupportedModels() {
        return null;
    }

    /**
     * 同步聊天
     *
     * @param request
     * @param sessionId
     */
    @Override
    public AiChatResponse chat(ChatRequest request, String sessionId) {
        return null;
    }

    /**
     * 流式聊天
     *
     * @param request
     * @param session
     */
    @Override
    public Flux<AiChatResponse> chatStream(ChatRequest request, String session) {
        return null;
    }

    /**
     * 检查服务是否可用
     */
    @Override
    public boolean isAvailable() {
        return false;
    }
}
