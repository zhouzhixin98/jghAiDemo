package com.jgh.springaidemo.common.utils;

import com.jgh.springaidemo.common.config.Result;
import com.jgh.springaidemo.common.enums.ResultEnum;

public class ResultUtil {
    // 成功响应（带数据）
    public static <T> Result<T> success(T data) {
        return new Result<T>()
                .code(ResultEnum.SUCCESS.getCode())
                .message(ResultEnum.SUCCESS.getMessage())
                .data(data);
    }

    // 成功响应（无数据）
    public static Result<Void> success() {
        return success(null);
    }

    // 错误响应（标准状态码）
    public static Result<Void> error(ResultEnum resultEnum) {
        return new Result<Void>()
                .code(resultEnum.getCode())
                .message(resultEnum.getMessage());
    }

    // 错误响应（自定义消息）
    public static Result<Void> error(int code, String message) {
        return new Result<Void>()
                .code(code)
                .message(message);
    }
}