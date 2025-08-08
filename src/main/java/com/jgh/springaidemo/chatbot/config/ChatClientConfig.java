package com.jgh.springaidemo.chatbot.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.model.openai.autoconfigure.OpenAiChatAutoConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @Author: JiuGHim
 * @CreateTime: 2025-08-08
 * @Description: 模型厂商配置类
 * @Version: 1.0
 */
public class ChatClientConfig {

    private final ChatModel dashScopeChatModel;

    private final ChatModel volcengineOpenAIChatModel;

    public ChatClientConfig(@Qualifier("dashscopeChatModel")ChatModel dashScopeChatModel,
                            @Qualifier("volcengineOpenAiChatModel") ChatModel volcengineOpenAIChatModel) {
        this.dashScopeChatModel = dashScopeChatModel;
        this.volcengineOpenAIChatModel = volcengineOpenAIChatModel;
    }
}
