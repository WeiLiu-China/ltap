package com.xdja.web.configure;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis configuration
 *
 * @author zk
 * @since 2019/9/12
 */
@MapperScan("com.xdja.dao")
@Configuration
public class MybatisConfigure {
    @Bean
    public PaginationInterceptor getMySqlPaginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        //设置方言类型
        page.setDialectType("oracle");
        return page;
    }
}
