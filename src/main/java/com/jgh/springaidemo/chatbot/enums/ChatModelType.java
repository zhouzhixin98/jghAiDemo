package com.jgh.springaidemo.chatbot.enums;

import lombok.Getter;

@Getter
public enum ChatModelType {
    /**
     * 火山引擎
     */
    VOLCENGINE("volcengine"),

    /**
     * 百炼
     */
    DASH_SCOPE("dashscope")



    ;

    private String code;

    ChatModelType(String code) {
        this.code = code;
    }

    public static ChatModelType getByCode(String code) {
        for (ChatModelType value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
