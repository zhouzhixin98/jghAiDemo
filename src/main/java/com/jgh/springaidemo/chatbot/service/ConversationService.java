package com.jgh.springaidemo.chatbot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jgh.springaidemo.chatbot.dto.ConversationsCreateDto;
import com.jgh.springaidemo.chatbot.dto.ConversationsDto;
import com.jgh.springaidemo.chatbot.entity.Conversation;
import com.jgh.springaidemo.chatbot.vo.ConversationVO;

/**
 * @author JiuGHim
 */
public interface ConversationService extends IService<Conversation> {

    /**
     * 获取对话框列表
     * @param dto
     * @param userId
     * @return
     */
    IPage<ConversationVO> page(ConversationsDto dto, Long userId);

    boolean removeConversation(String id);

    boolean updateConversation(String id, ConversationsCreateDto vo);
}
