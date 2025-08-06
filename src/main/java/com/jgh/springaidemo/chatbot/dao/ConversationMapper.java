package com.jgh.springaidemo.chatbot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jgh.springaidemo.chatbot.entity.Conversation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConversationMapper extends BaseMapper<Conversation> {
}
