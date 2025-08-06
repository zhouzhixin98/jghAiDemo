package com.jgh.springaidemo.chatbot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jgh.springaidemo.chatbot.dao.ConversationMapper;
import com.jgh.springaidemo.chatbot.dto.ConversationsCreateDto;
import com.jgh.springaidemo.chatbot.dto.ConversationsDto;
import com.jgh.springaidemo.chatbot.entity.Conversation;
import com.jgh.springaidemo.chatbot.service.ConversationService;
import com.jgh.springaidemo.chatbot.vo.ConversationVO;
import com.jgh.springaidemo.common.utils.PageConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @Author: JiuGHim
 * @CreateTime: 2025-08-06
 * @Description: 对话框实现类
 * @Version: 1.0
 */
@Service
public class ConversationServiceImpl
        extends ServiceImpl<ConversationMapper, Conversation>
        implements ConversationService {

    @Override
    public IPage<ConversationVO> page(ConversationsDto dto, Long userId) {
        LambdaQueryWrapper<Conversation> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(dto.getKeyword())){
            wrapper.like(Conversation::getTitle, dto.getKeyword());
        }
        wrapper.eq(Conversation::getUserId, userId)
                .eq(Conversation::getStatus,1);

        Page<Conversation> page = this.page(new Page<Conversation>(dto.getPageNum(), dto.getPageSize()), wrapper);
        return PageConvertUtils.convertPage(page, ConversationVO.class);
    }

    @Override
    public boolean removeConversation(String id) {
        return this.removeById(id);
    }

    @Override
    public boolean updateConversation(String id, ConversationsCreateDto vo) {
        Conversation conversation = this.getById(id);
        if (conversation == null){
            throw new RuntimeException("查无此对话框");
        }
        conversation.setTitle(vo.getTitle());
        return this.updateById(conversation);
    }


}
