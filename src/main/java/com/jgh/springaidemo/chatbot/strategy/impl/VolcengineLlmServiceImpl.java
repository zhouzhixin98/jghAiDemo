package com.jgh.springaidemo.chatbot.strategy.impl;

import com.jgh.springaidemo.chatbot.dto.AiChatResponse;
import com.jgh.springaidemo.chatbot.dto.ChatRequest;
import com.jgh.springaidemo.chatbot.enums.ChatModelType;
import com.jgh.springaidemo.chatbot.enums.RecordTypeEnum;
import com.jgh.springaidemo.chatbot.strategy.ChatClientFactory;
import com.jgh.springaidemo.chatbot.strategy.LlmService;
import com.jgh.springaidemo.common.tools.impl.BaiduWebSearchToolImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.*;

/**
 * @Author: JiuGHim
 * @CreateTime: 2025-08-07
 * @Description: 火山方舟智能对话实现类
 * @Version: 1.0
 */
@Service
public class VolcengineLlmServiceImpl implements LlmService {

    private final ChatClient volcengineChatClient;

    private final SimpleVectorStore simpleVectorStore;

    private final String SAVEPATH = System.getProperty("user.dir") + "/src/main/resources/save.json";

//    private final ToolCallbackProvider toolCallbackProvider;


    public VolcengineLlmServiceImpl(ChatClientFactory chatClientFactory, EmbeddingModel embeddingModel) {
        this.volcengineChatClient = chatClientFactory.getChatClient(ChatModelType.VOLCENGINE);
        this.simpleVectorStore = SimpleVectorStore
                .builder(embeddingModel).build();
//        this.toolCallbackProvider = toolCallbackProvider;
    }


    private static final List<String> SUPPORTED_MODELS = Arrays.asList(
            "doubao", "deepseek"
    );


    /**
     * 获取服务提供商类型
     */
    @Override
    public String getProviderType() {
        Map<String, Map<String, String>> models = new HashMap<>();
        Map<String, String> dashscopeMap = Map.of("qwen", "qwen-turbo",
                "deepseek-r1", "deepseek-r1",
                "deepseek-v3", "deepseek-v3",
                "kimi", "Moonshot-Kimi-K2-Instruct");
        Map<String, String> volcengineMap = Map.of("doubao", "doubao-seed-1-6-250615");
        models.put("dashscope", dashscopeMap);
        models.put("volcengine", volcengineMap);


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
    public AiChatResponse chat(ChatRequest request, String sessionId, String model) {
        return null;
    }

    /**
     * 流式聊天
     *
     * @param request
     * @param session
     */
    @Override
    public Flux<AiChatResponse> chatStream(ChatRequest request, String session, String model) {
        OpenAiChatOptions options = OpenAiChatOptions.builder().model(model).build();
        String prompt = "你是一个博学的智能对话助手";
        Boolean enableSearch = request.getEnableSearch();
        ChatClient.ChatClientRequestSpec spec = volcengineChatClient
                .prompt(prompt + (enableSearch ? "，你可以使用百度搜索功能" : ""))
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, request.getConversationId()))
                .user(request.getMessage())
                .options(options);
        if (enableSearch) {
            spec.tools(new BaiduWebSearchToolImpl());
        }

        return spec
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
        if ("STOP".equals(chatResponse.getResult().getMetadata().getFinishReason())) {
            return AiChatResponse.success(chatResponse.getResult().getOutput().getText(),
                    true, RecordTypeEnum.TEXT.getCode());
        } else {
//            String reasoningContent = output.getMetadata().get("reasoningContent").toString();
            String reasoningContent = output.getText();
            if (StringUtils.isNotBlank(reasoningContent)) {
                return AiChatResponse.success(reasoningContent,
                        false, RecordTypeEnum.REASONING.getCode());
            }
            return AiChatResponse.success(chatResponse.getResult().getOutput().getText(),
                    false, RecordTypeEnum.TEXT.getCode());
        }

    }
}
