package com.jgh.springaidemo.chatbot.controller;

import com.jgh.springaidemo.chatbot.dto.AiChatResponse;
import com.jgh.springaidemo.chatbot.dto.ChatRequest;
import com.jgh.springaidemo.chatbot.service.ChatService;
import com.jgh.springaidemo.common.config.Result;
import com.jgh.springaidemo.common.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * 聊天机器人接口
 *
 * @author JiuGHim
 */
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    /**
     * 智能对话
     * conversationId 会话ID -- 为空或者错误会创建新会话
     * message 用户会话内容
     * modelType 模型类型
     */
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AiChatResponse> streamChat(@RequestBody ChatRequest request,
                                           @RequestHeader(value = "vendor", defaultValue = "dashscope", required = false) String vendor,
                                           @RequestHeader(value = "model", required = false) String model) {
        //fixme 流式智能对话
        return chatService.chatStream(request, vendor, model);
    }

    /**
     * 中止对话
     */
    public Result<Void> stopChat() {
        //todo 中止对话
        return ResultUtil.success();
    }

}
