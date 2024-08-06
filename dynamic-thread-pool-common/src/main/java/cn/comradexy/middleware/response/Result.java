package cn.comradexy.middleware.response;

import lombok.*;
import org.springframework.http.HttpStatus;

/**
 * 响应结果包装类
 *
 * @Author: ComradeXY
 * @CreateTime: 2024-08-06
 * @Description: 响应结果包装类
 */
@Setter
@Getter
@AllArgsConstructor
public class Result<T> {
    /**
     * 响应码
     */
    private int code;
    /**
     * 响应消息
     */
    private String msg;
    /**
     * 响应数据
     */
    private T data;

    /**
     * 成功
     */
    public static <T> Result<T> success() {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), null);
    }

    /**
     * 成功
     *
     * @param data 数据
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
    }

    /**
     * 失败
     */
    public static <T> Result<T> error() {
        return new Result<>(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
    }

    /**
     * 失败
     *
     * @param msg  消息
     */
    public static <T> Result<T> error(String msg) {
        return new Result<>(HttpStatus.BAD_REQUEST.value(), msg, null);
    }
}
