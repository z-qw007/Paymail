package com.zqw.common.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {
    public static final long serialVersionUID = 1L;

    /**
     * code
     */
    private String code;

    /**
     * msg
     */
    private String msg;

    public BusinessException(String code) {
        this.code = code;
    }

    public BusinessException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }
    public BusinessException(String code, String msg, Throwable cause) {
        super(cause);
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "com.zqw.common.exception.BusinessException{" +
                "code='" + code + '\'' +
                ", info='" + msg + '\'' +
                '}';
    }

}
