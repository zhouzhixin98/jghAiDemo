package com.jgh.springaidemo.chatbot;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.memory.jdbc.MysqlChatMemoryRepository;
import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import com.jgh.springaidemo.chatbot.dto.ActorFilms;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/helloworld")
public class HelloworldController {
  private static final String DEFAULT_PROMPT = "你是一个博学的智能聊天助手，请根据用户提问回答！";

  private final ChatClient dashScopeChatClient;

  public HelloworldController(JdbcTemplate jdbcTemplate, ChatClient.Builder chatClientBuilder) {
    // 构造 ChatMemoryRepository 和 ChatMemory
    //mysql存储
//    ChatMemoryRepository chatMemoryRepository = MysqlChatMemoryRepository.mysqlBuilder()
//            .jdbcTemplate(jdbcTemplate)
//            .build();
    //redis存储
    ChatMemoryRepository chatMemoryRepository = RedisChatMemoryRepository.builder().build();

    ChatMemory chatMemory = MessageWindowChatMemory.builder()
            .chatMemoryRepository(chatMemoryRepository)
            .build();
    this.dashScopeChatClient = chatClientBuilder
            .defaultSystem(DEFAULT_PROMPT)
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
  * ChatClient 简单调用
  */
  @GetMapping("/simple/chat")
  public ChatResponse simpleChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？")String query,
                                 @RequestParam(value = "chat-id", defaultValue = "1") String chatId) {

    return dashScopeChatClient.prompt(query)
            .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, chatId))
            .call()
            .chatResponse();
//            .entity(ActorFilms.class);
  }

  /**
   * ChatClient 流式调用
   */
  @GetMapping(value = "/stream/chat", produces = "text/event-stream")
  public Flux<ChatResponse> streamChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？")String query, HttpServletResponse response) {

    response.setCharacterEncoding("UTF-8");
    return dashScopeChatClient.prompt(query).stream().chatResponse();
  }
}