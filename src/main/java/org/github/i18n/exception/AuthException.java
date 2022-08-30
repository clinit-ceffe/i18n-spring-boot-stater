package org.github.i18n.exception;

import org.springframework.http.HttpStatus;

/**
 * 权限异常
 *
 * @author DengSH
 */
public class AuthException extends SuperException {
    public AuthException() {
        this(null);
    }

    public AuthException(String message) {
        this(message, null);
    }


    public AuthException(String message, Object[] args) {
        super(message, HttpStatus.UNAUTHORIZED.value(), args);
    }
}
