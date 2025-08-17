package com.jgh.springaidemo.chatbot.strategy;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.memory.jdbc.MysqlChatMemoryRepository;
import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import com.alibaba.cloud.ai.toolcalling.baidusearch.BaiduSearchService;
import com.jgh.springaidemo.chatbot.enums.ChatModelType;
import com.jgh.springaidemo.chatbot.properties.VolcengineProperties;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
@EnableConfigurationProperties({VolcengineProperties.class})
public class ChatClientFactory implements InitializingBean {

//    private final OpenAiApi baseOpenAiApi = OpenAiApi.builder().build();

    private final OpenAiChatModel baseChatModel;

    private final JdbcTemplate jdbcTemplate;

    private final ChatModel dashScopeChatModel;

//    private static final String DEFAULT_PROMPT = "你是一个博学的智能聊天助手，请根据用户提问回答！";

    //----------------配置类--------------------------------------
    @Resource
    private VolcengineProperties volcengineProperties;


    //----------------配置类--------------------------------------

    // 缓存所有创建好的ChatClient
    private final Map<ChatModelType, ChatClient> chatClients = new EnumMap<>(ChatModelType.class);

    @Autowired
    public ChatClientFactory(OpenAiChatModel baseChatModel,
                             JdbcTemplate jdbcTemplate,
                             @Qualifier("dashscopeChatModel") ChatModel dashScopeChatModel) {
//        this.baseOpenAiApi = baseOpenAiApi;
        this.baseChatModel = baseChatModel;
        this.jdbcTemplate = jdbcTemplate;
        this.dashScopeChatModel = dashScopeChatModel;
    }


    @Override
    public void afterPropertiesSet() {
        chatClients.put(ChatModelType.VOLCENGINE, createVolcengineClient());
        chatClients.put(ChatModelType.DASH_SCOPE, createDashscopeClient());
    }


    /**
     * 获取已初始化的ChatClient
     */
    public ChatClient getChatClient(ChatModelType type) {
        ChatClient client = chatClients.get(type);
        if (client == null) {
            throw new IllegalArgumentException("Unsupported chat model type: " + type);
        }
        return client;
    }

    /**
     * 根据模型类型创建对应的ChatClient
     */
    private ChatClient createChatClient(ChatModelType modelType) {
        switch (modelType) {

            case VOLCENGINE:
                return createVolcengineClient();
            case DASH_SCOPE:
                return createDashscopeClient();
            // 可以添加更多模型的创建逻辑
            default:
                throw new IllegalArgumentException("不支持的厂商类型: " + modelType);
        }
    }

    private ChatClient createDashscopeClient() {
        //redis存储
//        ChatMemoryRepository chatMemoryRepository = RedisChatMemoryRepository.builder().build();

        //mysql存储
        ChatMemoryRepository chatMemoryRepository = MysqlChatMemoryRepository.mysqlBuilder()
                .jdbcTemplate(jdbcTemplate)
                .build();

        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .build();

        return ChatClient.builder(dashScopeChatModel)
//                .defaultSystem(DEFAULT_PROMPT)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                // 注册Advisor
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultOptions(
                        DashScopeChatOptions.builder()
                                .withTopP(0.7)
                                .build()
                )
                .build();
    }

    /**
     * 创建火山引擎客户端
     */
    private ChatClient createVolcengineClient() {
        //redis存储
//        ChatMemoryRepository chatMemoryRepository = RedisChatMemoryRepository.builder().build();

        //mysql存储
        ChatMemoryRepository chatMemoryRepository = MysqlChatMemoryRepository.mysqlBuilder()
                .jdbcTemplate(jdbcTemplate)
                .build();
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(chatMemoryRepository)
                .build();

        //Secret Access Key：TmpnMlptVXpaV016TW1FM05HUTJZMkUyWmpKbE5qbGtNR1UwWkdObU5qVQ==
        final OpenAiApi baseOpenAiApi = OpenAiApi.builder()
                .baseUrl(volcengineProperties.getBaseUrl())
                .completionsPath(volcengineProperties.getCompletionsPath())
                .apiKey(volcengineProperties.getApiKey())
                .build();
        OpenAiApi groqApi = baseOpenAiApi.mutate()
                .build();

        OpenAiChatModel groqModel = baseChatModel.mutate()
                .openAiApi(groqApi)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("doubao-seed-1-6-250615")
                        .temperature(0.5)
                        .build())
                .build();

        return ChatClient.builder(groqModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

}
