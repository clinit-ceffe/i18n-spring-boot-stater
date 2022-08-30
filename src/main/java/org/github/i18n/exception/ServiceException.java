package org.github.i18n.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

/**
 * 业务异常
 *
 * @author dengshuihong
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends SuperException {

    public ServiceException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
    }


    public ServiceException(String message, Object... args) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR.value(), args);
    }

    public ServiceException(String message, Integer status) {
        super(message, status, null);
    }

    public ServiceException(String message, Integer status, Object[] args) {
        super(message, status, args);
    }
}
