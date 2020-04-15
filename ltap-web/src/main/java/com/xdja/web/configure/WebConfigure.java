package com.xdja.web.configure;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.xdja.web.configure.token.TokenConfig;
import com.xdja.web.configure.token.TokenFactory;
import com.xdja.web.configure.token.operator.RedisTokenOperator;
import com.xdja.framework.commons.utils.UUIDUtil;
import org.apache.commons.codec.Charsets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;

import java.util.Arrays;

/**
 * web security configure
 *
 * @author zk
 * @since 2019/9/27
 */
@ServletComponentScan("com.xdja.web.filter")
@Configuration
public class WebConfigure {

    /**
     * token机制，默认使用redis存储session信息
     *
     * @param redisTemplate redisTemplate
     * @return tokenFactory
     */
    //@Bean
    //public TokenFactory getTokenFactoryBean(RedisTemplate<String, String> redisTemplate) {
    //    TokenFactory tokenFactory = new TokenFactory();
    //    TokenConfig tokenConfig = new TokenConfig();
    //    tokenConfig.setAutoDelay(true);
    //    tokenConfig.setExpiredTimeInMinutes(tokenExpiredTime);
    //    tokenConfig.setKeyGenerator(UUIDUtil::random);
    //
    //    RedisTokenOperator redisTokenOperator = new RedisTokenOperator(tokenConfig);
    //    redisTokenOperator.setRedisTemplate(redisTemplate);
    //    //默认redis存储session信息
    //    tokenFactory.setOperator(redisTokenOperator);
    //    return tokenFactory;
    //}

    /**
     * http message converter
     *
     * @return fastJsonHttpMessageConverter
     */
    @Bean
    public FastJsonHttpMessageConverter fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        fastJsonHttpMessageConverter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN));
        fastJsonHttpMessageConverter.setDefaultCharset(Charsets.UTF_8);
        return fastJsonHttpMessageConverter;
    }
}