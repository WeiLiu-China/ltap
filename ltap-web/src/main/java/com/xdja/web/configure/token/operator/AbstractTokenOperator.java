package com.xdja.web.configure.token.operator;


import com.xdja.web.configure.token.TokenConfig;

/**
 * 会话操作抽象类
 *
 * @author lvhao
 * @version 1.0
 * @date 2019/8/14 18:04
 */
public abstract class AbstractTokenOperator implements TokenOperator {
    /**
     * @Fields config: Token配置设置
     */
    protected TokenConfig config;

    public AbstractTokenOperator(TokenConfig config) {
        this.config = config;
    }
}
