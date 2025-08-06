package com.jgh.springaidemo.chatbot.service.impl;

import cn.hutool.core.util.StrUtil;
import com.jgh.springaidemo.chatbot.dto.AiChatResponse;
import com.jgh.springaidemo.chatbot.dto.ChatRequest;
import com.jgh.springaidemo.chatbot.entity.Conversation;
import com.jgh.springaidemo.chatbot.enums.ConversationsStatusEnum;
import com.jgh.springaidemo.chatbot.enums.ModelEnums;
import com.jgh.springaidemo.chatbot.enums.RecordTypeEnum;
import com.jgh.springaidemo.chatbot.service.ChatService;
import com.jgh.springaidemo.chatbot.service.ConversationService;
import com.jgh.springaidemo.chatbot.strategy.LlmService;
import com.jgh.springaidemo.chatbot.strategy.LlmServiceFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @Author: JiuGHim
 * @CreateTime: 2025-08-06
 * @Description: 智能对话实现类
 * @Version: 1.0
 */
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {


    private final ConversationService conversationService;

    private final LlmServiceFactory llmServiceFactory;

    /**
     * 流式聊天
     *
     * @param request
     * @return
     */
    @Override
    public Flux<AiChatResponse> chatStream(ChatRequest request) {
        //todo 根据模型选择厂商
        Conversation conversation = this.getOrCreateConversation(request, 1L, "1");
        request.setConversationId(conversation.getId());
        String modelName = request.getModelName();
        if (modelName.equals("deepseek")){
            if (request.getEnableThinking()){
                modelName = "deepseek-r1";
                request.setModelName(modelName);
            }else {
                modelName = "deepseek-v3";
                request.setModelName(modelName);
            }
        }
        ModelEnums modelEnum = ModelEnums.getByModel(modelName);
        if (modelEnum == null){
            throw new RuntimeException("未支持该模型 ==》 " + request.getModelName());
        }
        LlmService service = llmServiceFactory.getService(modelEnum.getVendor());
        
        // 创建自定义开头内容
        AiChatResponse customPrefix = AiChatResponse.success(
                "【系统提示】正在为您生成回答，请稍候...\n\n", 
                false,
                RecordTypeEnum.START.getCode()
        );
        
        // 在原始流开头添加自定义内容
        return Flux.just(customPrefix)
                .concatWith(service.chatStream(request, conversation.getId()));
    }


    /**
     * 获取或创建对话
     */
    private Conversation getOrCreateConversation(ChatRequest request, Long userId, String uid) {
        if (StrUtil.isNotBlank(request.getConversationId())) {
            Conversation conversation = conversationService.getById(request.getConversationId());
            if (conversation != null) {
                return conversation;
            }
        }

        // 创建新对话
        String title;
        if (request.getMessage().length() > 5){
            title = request.getMessage().substring(0, 5) + "...";
        }else {
            title = request.getMessage();
        }
//        String title = StrUtil.isNotBlank(request.getTitle()) ? request.getTitle() : "新对话";
        Conversation conversation = new Conversation()
//                .setId(IdUtil.fastSimpleUUID())
                .setTitle(title)
                .setUserId(userId)
                .setUid(uid)
                .setModelName(request.getModelName())
                .setConversationsStatus(ConversationsStatusEnum.ACTIVE.getCode())
                .setMessageCount(0);

        conversationService.save(conversation);
        return conversation;
    }

}
