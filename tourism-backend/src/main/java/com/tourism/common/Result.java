package com.tourism.common;

public class Result<T> {

    private int code;
    private String message;
    private T data;

    private Result() {}

    public static <T> Result<T> ok() {
        return build(ResultCode.SUCCESS, null);
    }

    public static <T> Result<T> ok(T data) {
        return build(ResultCode.SUCCESS, data);
    }

    public static <T> Result<T> ok(String message, T data) {
        Result<T> result = build(ResultCode.SUCCESS, data);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> fail() {
        return build(ResultCode.INTERNAL_ERROR, null);
    }

    public static <T> Result<T> fail(String message) {
        Result<T> result = build(ResultCode.INTERNAL_ERROR, null);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        return build(resultCode, null);
    }

    public static <T> Result<T> fail(ResultCode resultCode, String message) {
        Result<T> result = build(resultCode, null);
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> fail(int code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    private static <T> Result<T> build(ResultCode resultCode, T data) {
        Result<T> result = new Result<>();
        result.setCode(resultCode.getCode());
        result.setMessage(resultCode.getMessage());
        result.setData(data);
        return result;
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
