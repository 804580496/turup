package com.sea.turtle.soup.turup.util;

import lombok.Data;

@Data
public class Result<T> {
    private int code;       // 状态码
    private String message; // 消息
    private T data;         // 数据

    // 构造方法
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功响应
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "成功", data);
    }

    // 失败响应
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }


}

