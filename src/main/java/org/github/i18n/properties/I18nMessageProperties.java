package org.github.i18n.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * i18n 配置
 *
 * @author dengshuihong
 */
@Setter
@Getter
public class I18nMessageProperties{

    /**
     * 是否开启国际化， 默认为 true
     */
    private boolean enabled = true;

    /**
     * 是否开启全局模式(开启后所有异常均会被处理) 默认开启
     */
    private boolean global = true;

    /**
     * 消息配置
     */
    private Message message;

    @Setter
    @Getter
    public static class Message {

        /**
         * 默认消息
         */
        private String defaultMsg = "System Error!";

        /**
         * 是否开启消息相应结构自定义，开启后可以自定义返回结构,默认false未开启
         */
        private boolean custom = false;
    }
}
