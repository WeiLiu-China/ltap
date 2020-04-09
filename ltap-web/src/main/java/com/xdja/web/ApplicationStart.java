package com.xdja.web;

import com.xdja.web.configure.MybatisConfigure;
import com.xdja.web.configure.SystemConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * application start
 *
 * @author zk
 * @since 2019/9/11
 */
@SpringBootApplication
@MapperScan
@EnableScheduling
@Import({SystemConfigure.class, MybatisConfigure.class})
public class ApplicationStart extends SpringBootServletInitializer {
    /**
     * 配置文件路径, 以','分割的字符串. 配置采用覆盖式, 当有多个配置路径, 且包含相同配置属性时, 后者会覆盖前者. (windows环境下 /home/...以当前磁盘为根目录)
     */
    public final static String CONFIG_FILES_PATH = "classpath:application.properties,file:/home/xdja/conf/ltap/application.properties";

    /**
     * main方法启动
     */
    public static void main(String[] args) {
        SpringApplication.run(ApplicationStart.class, "--spring.config.location=" + CONFIG_FILES_PATH);
    }

    /**
     * tomcat启动
     */
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter("spring.config.location", CONFIG_FILES_PATH);
        super.onStartup(servletContext);
    }
}
