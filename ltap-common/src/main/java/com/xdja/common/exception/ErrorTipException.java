package com.xdja.common.exception;

/**
 * 错误提示异常
 *
 * @author zk
 * @since 2019/10/11
 */
public class ErrorTipException extends RuntimeException {
    public ErrorTipException(String msg) {
        super(msg);
    }
}
