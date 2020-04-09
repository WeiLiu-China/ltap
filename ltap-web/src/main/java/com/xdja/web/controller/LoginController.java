package com.xdja.web.controller;

import com.xdja.common.constants.JsonResponse;
import com.xdja.web.configure.token.TokenFactory;
import com.xdja.framework.commons.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 登录验证
 *
 * @author zk
 * @since 2020/1/14
 */
@Slf4j
@RestController
@RequestMapping(value = "/public/admin")
public class LoginController {

    //@Autowired
    //private TokenFactory tokenFactory;

    /**
     * 登录逻辑
     *
     * @return token
     */
    //@PostMapping(value = "/login")
    //public JsonResponse login(){
    //    String token = tokenFactory.getOperator().add(UUIDUtil.random());
    //    return JsonResponse.success(token);
    //}
}
