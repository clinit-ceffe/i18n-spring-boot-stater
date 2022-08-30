package org.github.i18n.core;


import org.github.i18n.properties.I18nMessageProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * 国际化异常消息适配器
 *
 * @author dengshuihong
 */
@Slf4j
@Order(-2147483647)
@RestControllerAdvice
@RequiredArgsConstructor
public class I18nExceptionMessageAdapter {

    private final MessageSource messageSource;
    private final ApplicationContext context;
    private final I18nMessageProperties i18nMessageProperties;
    private final I18nResponseStructAdapter<?> i18nResponseStructAdapter;

    @ExceptionHandler(Throwable.class)
    public Object restException(Throwable ex, HttpServletRequest request, HttpServletResponse response) throws Throwable {
        log.warn("Global Exception: intercept {} exception, error is `{}`", ex.getClass().getSimpleName(), ex.getMessage());
        Locale locale = RequestContextUtils.getLocale(request);
        Map<String, I18nMessageConverter> map = Optional.ofNullable(context.getBeansOfType(I18nMessageConverter.class)).orElseGet(HashMap::new);
        for (I18nMessageConverter<Throwable, String> converter : map.values()) {
            Class<? extends I18nMessageConverter> converterClass = converter.getClass();
            ParameterizedType type = converter instanceof AbstractI18nMessageConverter ?
                    (ParameterizedType) converterClass.getGenericSuperclass() :
                    (ParameterizedType) converterClass.getGenericInterfaces()[0];
            Class<?> eClass = (Class<?>) type.getActualTypeArguments()[0];
            if (ex.getClass().equals(eClass)) {
                return getResponse(converter.parse(ex, request, response, (code, args) -> messageSource.getMessage(code, args, locale)), ex);
            }
        }

        if (!i18nMessageProperties.isGlobal()) {
            throw ex;
        }

        String defaultMessage = i18nMessageProperties.getMessage().getDefaultMsg();
        try {
            return getResponse(messageSource.getMessage(ex.getMessage(), null, locale), ex);
        } catch (Exception e) {
            log.debug("Message Adapter: happen error with: {}", e.getMessage());
            return getResponse(messageSource.getMessage(defaultMessage, null, defaultMessage, locale), ex);
        }
    }


    /**
     * 获取响应格式
     *
     * @param message 消息
     * @param ex      异常
     * @return 返回值
     */
    public Object getResponse(String message, Throwable ex) {
        return i18nResponseStructAdapter.getResponse(message, ex);
    }
}
