package com.xdja.web.filter;

import com.xdja.web.configure.token.TokenFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 示例过滤器
 *
 * @author zk
 * @version 1.0
 * @date 2019/5/15 17:42
 */
@Slf4j
//@WebFilter(filterName = "adminFilter", value = {"/admin/*"})
public class AdminFilter implements Filter {
    @Autowired
    private TokenFactory tokenFactory;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                filterConfig.getServletContext());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //todo do filter
        String token = tokenFactory.getOperator().get("token");
        if (StringUtils.isBlank(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        tokenFactory.getOperator().delay("token");
        chain.doFilter(request, response);
    }
}

