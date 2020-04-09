package com.xdja.admin.service;

import com.xdja.admin.bean.AuthBean;

import java.util.List;

/**
 * @author yxb
 * @since 2020/4/9
 */
public interface AuthService {

    List<String> authPower(AuthBean authBean);

}
