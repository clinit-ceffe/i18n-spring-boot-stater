package org.github.i18n.exception;

import org.springframework.http.HttpStatus;

/**
 * web层统一异常处理
 *
 * @author Administrator
 */
public class RestException extends SuperException {

    public RestException(String message) {
        super(message, HttpStatus.BAD_REQUEST.value(), null);
    }

    public RestException(String message, Integer status) {
        super(message, status, null);
        this.status = status;
    }

    public RestException(String message, Integer status, Object... args) {
        super(message, status, args);
        this.status = status;
        this.args = args;
    }
}
