package com.xdja.web.aop;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * controller 入参和出参aspect
 *
 * @author zk
 * @since 2019/3/29
 */
@Component
@Aspect
@Slf4j
public class ApiLogAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PatchMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void controllerAspect() {
    }

    @Before(value = "controllerAspect()")
    public void doBefore(JoinPoint joinPoint) {
        if (log.isDebugEnabled()) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            String headerToken = request.getHeader("token");
            if (StringUtils.isEmpty(headerToken)) {
                log.debug("----------> {} - 请求入参: {}", request.getRequestURL().toString(), JSON.toJSONString(filterArgs(joinPoint.getArgs())));
            } else {
                log.debug("----------> {} - token:[{}] - 请求出参: {}", request.getRequestURL().toString(), headerToken, JSON.toJSONString(filterArgs(joinPoint.getArgs())));
            }
        }
    }

    @AfterReturning(value = "controllerAspect()", returning = "result")
    public void doAfter(Object result) {
        if (log.isDebugEnabled()) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            log.debug("----------> {} - 返回参数：{}", request.getRequestURL().toString(), JSON.toJSONString(result));
        }
    }

    /**
     * 过滤特殊参数
     *
     * @param args args
     * @return result
     */
    private List<Object> filterArgs(Object[] args) {
        List<Object> result = new ArrayList<>();
        if (args != null && args.length > 0) {
            for (Object arg : args) {
                if (arg instanceof HttpServletRequest) {
                    continue;
                }
                if (arg instanceof HttpServletResponse) {
                    continue;
                }
                if (arg instanceof BindingResult) {
                    continue;
                }
                result.add(arg);
            }
        }
        return result;
    }

}
