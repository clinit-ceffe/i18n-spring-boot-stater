package org.github.i18n.exception;

import lombok.Data;

/**
 * @author DengSH
 */
@Data
public abstract class SuperException extends RuntimeException {

    /**
     * 状态码
     */
    protected Integer status;

    /**
     * 模板参数 按自然序填充 {0}, {1}...
     */
    protected Object[] args;

    public SuperException(String message) {
        super(message);
    }

    public SuperException(Integer status, Object[] args) {
        this.status = status;
        this.args = args;
    }

    public SuperException(String message, Integer status, Object[] args) {
        super(message);
        this.status = status;
        this.args = args;
    }
}
