package com.jgh.springaidemo.chatbot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jgh.springaidemo.chatbot.dto.ConversationsCreateDto;
import com.jgh.springaidemo.chatbot.dto.ConversationsDto;
import com.jgh.springaidemo.chatbot.service.ConversationService;
import com.jgh.springaidemo.chatbot.vo.ConversationVO;
import com.jgh.springaidemo.common.config.Result;
import com.jgh.springaidemo.common.enums.ResultEnum;
import com.jgh.springaidemo.common.utils.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author JiuGHim
 */
@RestController
@RequestMapping("/conversation")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;


    /**
     * 获取对话框列表
     */
    @PostMapping("/page")
    public Result<IPage<ConversationVO>> getConversations(@RequestHeader("x-auth-userId") Long userId,
                                                          @RequestBody ConversationsDto dto) {
        return ResultUtil.success(conversationService.page(dto, userId));

    }

    /**
     * 删除对话框
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteConversation(@PathVariable("id") String id) {
        if (conversationService.removeConversation(id)) {
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ResultEnum.INTERNAL_ERROR);
        }
    }

    /**
     * 修改对话框
     */
    @PutMapping("/{id}")
    public Result<Void> updateConversation(@PathVariable("id") String id, @RequestBody ConversationsCreateDto vo) {
        if (conversationService.updateConversation(id, vo)) {
            return ResultUtil.success();
        } else {
            return ResultUtil.error(ResultEnum.INTERNAL_ERROR);
        }
    }


}
