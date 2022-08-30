package org.github.i18n.handler;

import org.github.i18n.core.I18nMessageConverter;
import org.github.i18n.exception.ServiceException;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.BiFunction;

/**
 * 默认消息转换
 *
 * @author dengshuihong
 */
public class ServiceI18nMessageConverterHandler implements I18nMessageConverter<ServiceException, String> {

    @Override
    public String parse(ServiceException ex, HttpServletRequest request, HttpServletResponse response, BiFunction<String, Object[], String> process) {
        int status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        response.setStatus(status);
        String message = process.apply(ex.getMessage(), ex.getArgs());
        return message;
    }
}
