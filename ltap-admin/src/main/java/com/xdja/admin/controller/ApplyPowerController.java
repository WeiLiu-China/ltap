package com.xdja.admin.controller;

import com.xdja.admin.bean.AuthBean;
import com.xdja.admin.service.AuthService;
import com.xdja.common.constants.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yxb
 * @since 2020/4/9
 */
@RestController
public class ApplyPowerController {

    @Autowired
    private AuthService authService;


    @PostMapping("/admin/power/authPower")
    public Object authPower(@RequestBody AuthBean authBean) {
        List<String> invalidIdNos = authService.authPower(authBean);
        return JsonResponse.success(invalidIdNos);
    }
}
