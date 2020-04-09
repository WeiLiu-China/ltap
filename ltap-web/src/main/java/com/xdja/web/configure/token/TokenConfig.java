package com.xdja.web.configure.token;

import java.util.function.Supplier;

/**
 * token 配置
 *
 * @author lvhao
 * @version 1.0
 * @date 2019/5/16 14:06
 */
public class TokenConfig {
    /**
     * @Fields keyGenerator: Key生成器
     */
    private Supplier<String> keyGenerator;
    /**
     * @Fields expiredTimeInMinutes: 过期时间，单位为分钟
     */
    private long expiredTimeInMinutes = 30;

    /**
     * @Fields autoDelay: 是否自动延期，当获取Token或Token被更新时，自动延期。
     */
    private boolean autoDelay = true;

    public Supplier<String> getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(Supplier<String> keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public long getExpiredTimeInMinutes() {
        return expiredTimeInMinutes;
    }

    public void setExpiredTimeInMinutes(long expiredTimeInMinutes) {
        this.expiredTimeInMinutes = expiredTimeInMinutes;
    }

    public boolean isAutoDelay() {
        return autoDelay;
    }

    public void setAutoDelay(boolean autoDelay) {
        this.autoDelay = autoDelay;
    }
}

