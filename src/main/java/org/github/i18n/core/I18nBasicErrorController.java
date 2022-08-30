package org.github.i18n.core;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Filter 异常处理
 *
 * @author DengSH
 */
@Slf4j
@RestController
public class I18nBasicErrorController extends BasicErrorController {

    public I18nBasicErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorProperties, errorViewResolvers);
    }

    @SneakyThrows
    @Override
    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request){
        Exception ex = (Exception) request.getAttribute("error");
        throw ex;
    }
}
