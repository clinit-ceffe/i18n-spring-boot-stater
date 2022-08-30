package org.github.i18n.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.BiFunction;

/**
 * 统一消息i18n解析接口
 *
 * @author dengshuihong
 */
public interface I18nMessageConverter<E extends Throwable, R extends Object> {

    /**
     * 消息转换
     *
     * @param ex       异常信息
     * @param request  http request
     * @param response 响应体
     * @param process  消息处理
     * @return String i18n消息
     */
    String parse(E e, HttpServletRequest request, HttpServletResponse response, BiFunction<String, Object[], R> process);
}
