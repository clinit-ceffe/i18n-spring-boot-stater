# i18n-spring-boot-stater
一套快速整合项目的国际化解决方案
### 1. 前言

       为了统一化管理系统提示用语，规范化提示信息内容，方便未来对多语言支持，本文将提供i18n国际化集成配置说明，方便快速解决上述问题。

### 2. 版本

 迭代版本： 1.0.x-SNAPSHOT

 稳定版本： 1.0.0

### 3. 集成

1. 添加依赖

```xml
 <dependency>
    <groupId>cn.mi.i18n</groupId>
    <artifactId>i18n-spring-boot-stater</artifactId>
    <version>1.0.0-SNAPSHOT</version>
 </dependency>
```

2. 在项目中创建国际化文件

```java
|- xxx
|- xxxx
|- resource
     |—— messages
        |—— message_zh_CN.properties
        |—— message_us_EN.properties    
```

3. `message_zh_CN.properties` 配置

```properties
mi.msg.acl.unauth.error={0}还没有访问{1}的权限
mi.msg.sys.error=系统错误
```

4. `message_us_EN.properties` 配置

```properties
mi.msg.acl.unauth.error={0} does not yet have permission to access {1}!
mi.msg.sys.error=system error!
```

5. 启用配置

```yml
spring:
    messages:
        basename: messages/message   #指定路径
        encoding: UTF-8              #编码方式
```

    注： `spring.messages.basename` 为国际化message_*.propertis文件路径 ，其中     *.propertis省略不写。

6. 代码中使用

```java
import ...;

@RestController
@RequestMapping("/api/i18n")
public class I18nController {

    @GetMapping("/lang")
    public String test(String str) {
        if (Objects.equals(str, null)) {
            throw new RestException("mi.msg.acl.info.error", HttpStatus.UNAUTHORIZED.value(), "张三");
        }
        return "hello word";
    }

}
```

7. 切换语言

```http
GET http://localhost:9091/api/i18n/lang
accept-language: en-US
```

    请求头添加 `accept-languag` 来实现语言切换， 默认为简体中文    `zh_CN` 。

### 4. 配置

    i18n 提供了启停配置，以及国际化文件配置，具体配置参考如下：     

| 配置建                     | 类型      | 默认值          | 描述      |
|:----------------------- |:-------:|:------------:| ------- |
| mi.i18n.enable         | boolean | true         | 启停开关    |
| spring.message.basename | String  | i18n/message | 国际化配置路径 |
| spring.message.encoding | String  | GBK          | 字符编码    |

        

### 5. 原理

     采用基于messageSource + 全局异常处理实现， 其中`I18nExceptionMessageAdapter`负责处理异常拦截与消息转换工作。核心代码如下：

```java
@Slf4j
@Order(0)
@RestControllerAdvice
@RequiredArgsConstructor
public class I18nExceptionMessageAdapter {

    private final MessageSource messageSource;
    private final ApplicationContext context;

    @Value("${ofs.i18n.message.default: System Error!}")
    private String defaultMessage;
    
    
    @ExceptionHandler(Throwable.class)
    public Object restException(Throwable ex, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Global Exception: intercept {} exception, error is `{}`", ex.getClass().getSimpleName(), ex.getMessage());
        Locale locale = RequestContextUtils.getLocale(request);
        Map<String, I18nMessageConverter> map = Optional.ofNullable(context.getBeansOfType(I18nMessageConverter.class)).orElseGet(HashMap::new);
        for (I18nMessageConverter<Throwable> converter : map.values()) {
            ParameterizedType type = (ParameterizedType) converter.getClass().getGenericInterfaces()[0];
            Class<?> eClass = (Class<?>) type.getActualTypeArguments()[0];
            if (ex.getClass().equals(eClass)) {
                return converter.parse(ex, request, response, (code, args) -> messageSource.getMessage(code, args, locale));
            }
        }
        try {
            return messageSource.getMessage(ex.getMessage(), null, locale);
        } catch (Exception e) {
            log.debug("Message Adapter: happen error with: {}", e.getMessage());
            return messageSource.getMessage(defaultMessage, null, defaultMessage, locale);
        }
    }
}
```

    i18n stater 内置了两个异常处理类， 分别是： `RestException` 、`ServiceException` 位于cn.mi.i18n.exception包下。

    为了能适应与各个项目，仅对内置的两个异常类是无法满足的， 还需提供定制化的能力， 因此，i18n提供扩展接口类 `I18nMessageConverter` i18n消息解析器，可以实现自定义异常消息转换处理。下面为代码演示：

```java
package xx.xx;

import xxxx;

@Component
public class RuntimeI18nMessageConverterHandler implements I18nMessageConverter<RuntimeException> {

    @Override
    public Object parse(RuntimeException e, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BiFunction<String, Object[], Object> biFunction) {
        return biFunction.apply(e.getMessage(), null);
    }
}
```

    

    以上为对 `RuntimeException`类型异常的支持， 使用方式与内置方式一致，只需在对于业务代码中抛出RuntimeException 异常即可， 代码如下：

```java
package xx.xx;

import xxxx;

@RestController
@RequestMapping("/api/i18n")
public class I18nController {

    @GetMapping("/lang")
    public String test(String str) {
        if (Objects.equals(str, null)) {
            throw new RestException("mi.msg.acl.info.error", HttpStatus.UNAUTHORIZED.value(), "张三");
        }
        return "hello word";
    }

    @GetMapping("/lang2")
    public String test2(String str) {
        if (Objects.equals(str, null)) {
            throw new RuntimeException("mi.msg.acl.info.error");
        }
        return "hello word";
    }

}
```

### 5. Issue

