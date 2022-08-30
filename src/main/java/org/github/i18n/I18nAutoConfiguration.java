package org.github.i18n;

import lombok.extern.slf4j.Slf4j;
import org.github.i18n.core.I18nBasicErrorController;
import org.github.i18n.core.I18nExceptionMessageAdapter;
import org.github.i18n.core.I18nResponseStructAdapter;
import org.github.i18n.handler.AuthI18nMessageConverterHandler;
import org.github.i18n.handler.I18nNormalResponseStructAdapter;
import org.github.i18n.handler.RestI18nMessageConverterHandler;
import org.github.i18n.handler.ServiceI18nMessageConverterHandler;
import org.github.i18n.properties.I18nMessageProperties;
import org.github.i18n.properties.I18nMessageSourceProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;

import javax.servlet.Filter;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 国际化配置
 *
 * @author dengshuihong
 */
@Slf4j
@ConditionalOnProperty(prefix = "spring.i18n", value = "enabled", havingValue = "true", matchIfMissing = true)
public class I18nAutoConfiguration {


    @Bean
    @ConfigurationProperties(prefix = "spring.i18n")
    public I18nMessageProperties i18nMessageProperties() {
        return new I18nMessageProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.messages")
    public I18nMessageSourceProperties messageSourceProperties() {
        return new I18nMessageSourceProperties();
    }

    @Bean(name = "messageSource")
    @ConditionalOnMissingBean
    public ResourceBundleMessageSource getMessageResource(I18nMessageSourceProperties messages) {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        Optional.ofNullable(messages.getBaseNames()).ifPresent(names -> {
            messageSource.setBasenames(names.toArray(new String[0]));
        });
        Optional.ofNullable(messages.getBasename()).ifPresent(messageSource::setBasename);
        messageSource.setDefaultEncoding(messages.getEncoding().name());
        messageSource.setFallbackToSystemLocale(messages.isFallbackToSystemLocale());
        messageSource.setUseCodeAsDefaultMessage(messages.isUseCodeAsDefaultMessage());
        log.info("I18N: i18n message configuration is loading..., path with {}", messages.getBasename());
        return messageSource;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "spring.i18n.message", value = "custom", havingValue = "true", matchIfMissing = true)
    public I18nResponseStructAdapter<?> i18nResponseStructAdapter() {
        return new I18nNormalResponseStructAdapter();
    }

    @Bean
    @ConditionalOnBean(I18nResponseStructAdapter.class)
    @ConditionalOnMissingBean(RestI18nMessageConverterHandler.class)
    public RestI18nMessageConverterHandler restI18nMessageConverter() {
        return new RestI18nMessageConverterHandler();
    }

    @Bean
    @ConditionalOnBean(I18nResponseStructAdapter.class)
    @ConditionalOnMissingBean(ServiceI18nMessageConverterHandler.class)
    public ServiceI18nMessageConverterHandler serviceI18nMessageConverter() {
        return new ServiceI18nMessageConverterHandler();
    }

    @Bean
    @ConditionalOnBean(I18nResponseStructAdapter.class)
    @ConditionalOnMissingBean(AuthI18nMessageConverterHandler.class)
    public AuthI18nMessageConverterHandler authI18nMessageConverterHandler() {
        return new AuthI18nMessageConverterHandler();
    }

    @Bean
    public I18nExceptionMessageAdapter exceptionAdapterHandler(MessageSource messageSource,
                                                               ApplicationContext context,
                                                               I18nMessageProperties i18nMessageProperties,
                                                               I18nResponseStructAdapter<?> i18nResponseStructAdapter) {
        return new I18nExceptionMessageAdapter(messageSource, context, i18nMessageProperties, i18nResponseStructAdapter);
    }

    @Primary
    @Bean
    public ErrorController basicErrorController(ErrorAttributes errorAttributes,
                                                             ServerProperties serverProperties,
                                                             ObjectProvider<ErrorViewResolver> errorViewResolvers) {
        return new I18nBasicErrorController(errorAttributes, serverProperties.getError(), errorViewResolvers.orderedStream().collect(Collectors.toList()));
    }


    @Bean
    public FilterRegistrationBean exceptionFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new ErrorPushFilter());
        registration.setOrder(Integer.MIN_VALUE + 1);
        return registration;
    }
}
