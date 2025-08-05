package com.jgh.springaidemo.chatbot;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
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

  public HelloworldController(ChatClient.Builder chatClientBuilder) {
    this.dashScopeChatClient = chatClientBuilder
        .defaultSystem(DEFAULT_PROMPT)
        // 实现 Logger 的 Advisor
        .defaultAdvisors(
            new SimpleLoggerAdvisor()
        )
        // 设置 ChatClient 中 ChatModel 的 Options 参数
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
  public String simpleChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？")String query) {

    return dashScopeChatClient.prompt(query).call().content();
  }


  /**
   * ChatClient 流式调用
   */
  @GetMapping(value = "/stream/chat", produces = "text/event-stream")
  public Flux<String> streamChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？")String query, HttpServletResponse response) {

    response.setCharacterEncoding("UTF-8");
    return dashScopeChatClient.prompt(query).stream().content();
  }
}