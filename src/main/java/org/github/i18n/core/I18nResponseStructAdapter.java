package org.github.i18n.core;

import org.github.i18n.Response;
import org.github.i18n.exception.AuthException;
import org.github.i18n.exception.RestException;
import org.github.i18n.exception.ServiceException;
import org.github.i18n.exception.SuperException;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 相应体结构适配器
 *
 * @author dengshuihong
 */
public interface I18nResponseStructAdapter<R extends Object> {

    /**
     * 获取相应参数
     *
     * @param message 提示内容
     * @param ex      处理异常
     * @return 相应结构数据
     */
    default R getResponse(String message, Throwable ex) {
        if (ex instanceof AuthException || ex instanceof RestException || ex instanceof ServiceException) {
            return (R) Response.error(((SuperException) ex).getStatus(), message);
        }
        return (R) Response.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
    }


}
