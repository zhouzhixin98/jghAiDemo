package com.jgh.springaidemo.common.config;

public class Result<T> {
    private int code;        // 响应状态码（如200成功）
    private String message;  // 响应消息（如"操作成功"）
    private T data;          // 泛型响应数据
    private long timestamp;  // 响应时间戳（用于追踪请求）

    // 私有构造器（强制使用工具类创建实例）
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }

    // --------------- Getter 方法 ---------------
    public int getCode() { return code; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public long getTimestamp() { return timestamp; }

    // --------------- 链式调用方法 ---------------
    public Result<T> code(int code) {
        this.code = code;
        return this;
    }

    public Result<T> message(String message) {
        this.message = message;
        return this;
    }

    public Result<T> data(T data) {
        this.data = data;
        return this;
    }
}