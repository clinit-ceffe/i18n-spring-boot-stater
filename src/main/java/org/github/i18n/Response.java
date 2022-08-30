package org.github.i18n;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 响应报文
 *
 * @author DengSH
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> implements Serializable {

    /**
     * 响应码 可参考
     *
     * @see org.springframework.http.HttpStatus
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    public static Response<Object> error(Integer status, String message) {
        return new Response<Object>(status, message, null);
    }

    public static Response<Object> success(Object data) {
        return new Response<Object>(HttpStatus.OK.value(), "成功", data);
    }
}
