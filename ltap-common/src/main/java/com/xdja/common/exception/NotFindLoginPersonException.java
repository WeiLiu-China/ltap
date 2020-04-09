package com.xdja.common.exception;

/**
 * 未获取到当前登录人信息
 *
 * @author zk
 * @since 2020/1/3
 */
public class NotFindLoginPersonException extends RuntimeException {
    public NotFindLoginPersonException(String msg) {
        super(msg);
    }
}
