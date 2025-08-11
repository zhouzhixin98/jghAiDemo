package com.jgh.springaidemo.chatbot;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/more-model-chat-client")
public class MoreModelChatClientController {

	private final Set<String> modelList = Set.of(
			"deepseek-r1",
			"deepseek-v3",
			"qwen-plus",
			"qwen-max"
	);

	private final ChatClient chatClient;

	private final ChatModel openAIChatModel;

	public MoreModelChatClientController(
			@Qualifier("dashscopeChatModel") DashScopeChatModel chatModel,
			@Qualifier("openAiChatModel") ChatModel openAIChatModel
	) {

		this.openAIChatModel = openAIChatModel;
		// 构建 chatClient
		this.chatClient = ChatClient.builder(chatModel).build();
	}

	@GetMapping
	public Flux<String> stream(
			@RequestParam("prompt") String prompt,
			@RequestHeader(value = "models", required = false) String models,
			@RequestHeader(value = "platform", required = false) String platform
	) {

		if (!StringUtils.hasText(platform)) {
			return Flux.just("platform not exist");
		}

		if (!modelList.contains(models)) {

			return Flux.just("model not exist");
		}

		if (Objects.equals("dashscope", platform)) {
			System.out.println("命中 dashscope ......");
			return chatClient.prompt(prompt)
					.options(DashScopeChatOptions.builder()
							.withModel(models)
							.build()
					).stream()
					.content();
		}

		if (Objects.equals("openai", platform)) {

			ChatClient chatClient1 = ChatClient.create(openAIChatModel);
			System.out.println("命中 openai ......");
            Flux<String> content = ChatClient.builder(openAIChatModel).build().prompt(prompt)
                    .options(DashScopeChatOptions.builder()
                            .withModel(models)
                            .build()
                    ).stream()
                    .content();
            return content;
		}

		return Flux.just("platform not exist");

	}

//	public Flux<String> stream(
//			@RequestParam("prompt") String prompt
//	) {
//
//
//		DashScopeApi dashScopeApi = DashScopeApi.builder().apiKey(System.getenv("${AI_DASHSCOPE_API_KEY}")).build();
//		ChatClient build = ChatClient.builder(
//				DashScopeChatModel.builder().dashScopeApi(dashScopeApi)
//						.build()
//		).build();
//	}

}