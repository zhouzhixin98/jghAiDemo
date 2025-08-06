package com.jgh.springaidemo.chatbot.enums;

import lombok.Getter;

@Getter
public enum ConversationsStatusEnum {

    ACTIVE("active", "正常"),
    ARCHIVED("archived", "归档"),
    DELETED("deleted", "删除")

    ;

    private String code;
    private String name;



    ConversationsStatusEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
