package org.github.i18n.handler;

import org.github.i18n.core.I18nMessageConverter;
import org.github.i18n.exception.AuthException;
import org.github.i18n.exception.RestException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.function.BiFunction;

/**
 * 默认消息转换
 *
 * @author dengshuihong
 */
public class AuthI18nMessageConverterHandler implements I18nMessageConverter<AuthException, String> {

    @Override
    public String parse(AuthException ex, HttpServletRequest request, HttpServletResponse response, BiFunction<String, Object[], String> process) {
        if (Objects.nonNull(ex.getStatus())) {
            response.setStatus(ex.getStatus());
        }
        String message = process.apply(ex.getMessage(), ex.getArgs());
        return message;
    }
}
