package com.xdja.web.configure;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * ltap system configure
 *
 * @author zk
 * @since 2019/9/12
 */
@EnableTransactionManagement
@ComponentScan(basePackages = "com.xdja.**")
@Configuration
public class SystemConfigure {

}
