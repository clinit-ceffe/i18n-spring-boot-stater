package org.github.i18n.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.Properties;

/**
 * 文档导出
 *
 * @author dengshuihong
 */
@Slf4j
public class DocumentExportHelper {


    public void exportHtml() {
        ClassPathResource resource = new ClassPathResource("messages");
        Properties properties = new Properties();
        try (InputStream in = resource.getInputStream()) {
            properties.load(in);
            properties.forEach((key, value) -> {
                //mock down
            });
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
