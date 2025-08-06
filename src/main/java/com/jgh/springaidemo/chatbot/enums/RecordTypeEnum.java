package com.jgh.springaidemo.chatbot.enums;

import lombok.Getter;

/**
 * @Author: JiuGHim
 * @CreateTime: 2025-08-06
 * @Description: 响应类型枚举
 * @Version: 1.0
 */
@Getter
public enum RecordTypeEnum {
    START("start","开始流式输出"),
    REASONING("reasoning","推理内容"),
    TEXT("text","正式回答")
    ;

    private String code;

    private String description;

    RecordTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
