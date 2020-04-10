package com.xdja.admin.controller;

import com.xdja.admin.bean.AuthBean;
import com.xdja.admin.service.AuthService;
import com.xdja.common.constants.JsonResponse;
import com.xdja.common.exception.ErrorTipException;
import com.xdja.common.utils.EmptyUtil;
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
        checkParam(authBean);
        List<String> invalidIdNos = authService.authPower(authBean);
        return JsonResponse.success(invalidIdNos);
    }

    private void checkParam(AuthBean authBean) {
        if (authBean == null) {
            throw new ErrorTipException("参数错误");
        }
        if (EmptyUtil.isEmpty(authBean.getAppId()) || EmptyUtil.isEmpty(authBean.getAppRegionalismCode())) {
            throw new ErrorTipException("appId和appRegionalism不能为空");
        }
        if (EmptyUtil.isEmpty(authBean.getPersonIdNos())) {
            throw new ErrorTipException("personIdNos不能为空");
        }
    }
}
