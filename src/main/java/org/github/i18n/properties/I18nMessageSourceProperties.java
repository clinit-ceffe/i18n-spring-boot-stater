package org.github.i18n.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.context.MessageSourceProperties;

import java.util.Set;

/**
 * @author DengSH
 */
@Setter
@Getter
public class I18nMessageSourceProperties extends MessageSourceProperties {

    private Set<String> baseNames;
}
