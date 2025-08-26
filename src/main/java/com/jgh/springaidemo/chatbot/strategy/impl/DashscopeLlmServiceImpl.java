package com.jgh.springaidemo.chatbot.strategy.impl;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.dashscope.image.DashScopeImageOptions;
import com.alibaba.cloud.ai.memory.jdbc.MysqlChatMemoryRepository;
import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import com.jgh.springaidemo.chatbot.dto.AiChatResponse;
import com.jgh.springaidemo.chatbot.dto.ChatRequest;
import com.jgh.springaidemo.chatbot.enums.ChatModelType;
import com.jgh.springaidemo.chatbot.enums.ModelEnums;
import com.jgh.springaidemo.chatbot.enums.RecordTypeEnum;
import com.jgh.springaidemo.chatbot.strategy.ChatClientFactory;
import com.jgh.springaidemo.chatbot.strategy.LlmService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: JiuGHim
 * @CreateTime: 2025-08-06
 * @Description: 百炼平台智能对话实现类
 * @Version: 1.0
 */
@Service
public class DashscopeLlmServiceImpl implements LlmService {

    private final ChatClient dashScopeChatClient;

    public DashscopeLlmServiceImpl(ChatClientFactory chatClientFactory) {
        this.dashScopeChatClient = chatClientFactory.getChatClient(ChatModelType.DASH_SCOPE);
    }

    private static final List<String> SUPPORTED_MODELS = Arrays.asList(
            "qwen", "deepseek", "kimi"
    );


    /**
     * 获取服务提供商类型
     */
    @Override
    public String getProviderType() {
        return "dashscope";
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
    public AiChatResponse chat(ChatRequest request, String sessionId,String model) {
        return null;
    }

    /**
     * 流式聊天
     *
     * @param request
     * @param session
     */
    @Override
    public Flux<AiChatResponse>


    chatStream(ChatRequest request, String session,String model) {

        DashScopeChatOptions options = DashScopeChatOptions.builder()
                .withModel(model)
                .withEnableThinking(request.getEnableThinking())
                .withEnableSearch(request.getEnableSearch()).build();
        if (StringUtils.isNotBlank(request.getFileUrl())){
            //如果传入文件
        }


        return dashScopeChatClient
                .prompt(request.getMessage())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, request.getConversationId()))
                .options(options)
                .stream()
                .chatResponse()
                .map(this::convertToAiChatResponse);
    }

    /**
     * 检查服务是否可用
     */
    @Override
    public boolean isAvailable() {
        return true;
    }

    private AiChatResponse convertToAiChatResponse(ChatResponse chatResponse) {
        // 提取所需字段（示例）
//        String content = chatResponse.getResult().getOutput().getContent();
//        Metadata metadata = extractMetadata(chatResponse); // 自定义元数据提取
        AssistantMessage output = chatResponse.getResult().getOutput();
        if ("STOP".equals(chatResponse.getResult().getMetadata().getFinishReason())){
            return AiChatResponse.success(chatResponse.getResult().getOutput().getText(),
                    true, RecordTypeEnum.TEXT.getCode());
        }else {
            String reasoningContent = output.getMetadata().get("reasoningContent").toString();
            if (StringUtils.isNotBlank(reasoningContent)){
                return AiChatResponse.success(reasoningContent,
                        false,RecordTypeEnum.REASONING.getCode());
            }
            return AiChatResponse.success(chatResponse.getResult().getOutput().getText(),
                    false,RecordTypeEnum.TEXT.getCode());
        }

    }
}
