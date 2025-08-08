package com.jgh.springaidemo.chatbot.strategy.impl;

import com.jgh.springaidemo.chatbot.dto.AiChatResponse;
import com.jgh.springaidemo.chatbot.dto.ChatRequest;
import com.jgh.springaidemo.chatbot.strategy.LlmService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.model.openai.autoconfigure.OpenAiChatAutoConfiguration;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: JiuGHim
 * @CreateTime: 2025-08-07
 * @Description: 火山引擎智能对话实现类
 * @Version: 1.0
 */
@Service
public class VolcengineLlmServiceImpl implements LlmService {
    

    public VolcengineLlmServiceImpl() {

    }


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
        return SUPPORTED_MODELS;
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
