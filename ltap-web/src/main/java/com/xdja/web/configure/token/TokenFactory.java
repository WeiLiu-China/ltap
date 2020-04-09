package com.xdja.web.configure.token;


import com.xdja.web.configure.token.operator.TokenOperator;

/**
 * @author lvhao
 * @version 1.0
 * @date 2019/5/16 14:04
 */
public class TokenFactory {
    private TokenOperator operator;

    public TokenOperator getOperator() {
        return operator;
    }

    public void setOperator(TokenOperator operator) {
        this.operator = operator;
    }
}
