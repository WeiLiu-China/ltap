package com.xdja.admin.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xdja.admin.bean.AuthBean;
import com.xdja.admin.bean.RoamAppAuthInfo;
import com.xdja.admin.entity.Person;
import com.xdja.admin.service.AuthService;
import com.xdja.admin.service.PersonService;
import com.xdja.common.exception.ErrorTipException;
import com.xdja.common.utils.EmptyUtil;
import com.xdja.common.utils.HttpUtil;
import com.xdja.framework.commons.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author yxb
 * @since 2020/4/9
 */
@Service
@Slf4j
@RefreshScope
public class AuthServiceImpl implements AuthService {

    @Value("${user.app.auth.url}")
    private String userAppAuthUrl;

    @Value("${local.regionalism.code}")
    private String localRegionalismCode;

    @Autowired
    private PersonService personService;

    @Override
    public List<String> authPower(AuthBean authBean) {
        if (EmptyUtil.isEmpty(localRegionalismCode)) {
            throw new ErrorTipException("未配置本地行政区划编码，请在application.properties中配置【local.regionalism.code】");
        }
        if (EmptyUtil.isEmpty(userAppAuthUrl)) {
            throw new ErrorTipException("未配置统一授权地址，请在application.properties中配置【user.app.auth.url】");
        }
        String personIdNos = authBean.getPersonIdNos();
        List<String> idNos = Arrays.asList(personIdNos.split(","));
        // 无效身份证号
        List<String> invalidIdNos = new ArrayList<>();
        List<RoamAppAuthInfo> roamAppAuthInfos = new ArrayList<>();
        String type = authBean.getType() == 1?"1":"3";
        if (EmptyUtil.isNotEmpty(idNos)) {
            log.debug("开始对应用{}进行批量授权/取消授权",authBean.getAppId());
            idNos.forEach(idNo -> {
                List<Person> personList = personService.list(Wrappers.<Person>lambdaQuery()
                        .eq(Person::getFlag,"0")
                        .eq(Person::getIdentifier,idNo));
                if (EmptyUtil.isNotEmpty(personList)) {
                    Person person = personList.get(0);
                    log.debug("检索到人员：{}",person.getName());
                    RoamAppAuthInfo roamAppAuthInfo = new RoamAppAuthInfo();
                    roamAppAuthInfo.setAppId(authBean.getAppId());
                    roamAppAuthInfo.setAppRegionalismCode(authBean.getAppRegionalismCode());
                    roamAppAuthInfo.setPersonId(person.getId());
                    roamAppAuthInfo.setPersonRegionalismCode(localRegionalismCode);
                    roamAppAuthInfo.setType(type);
                    roamAppAuthInfos.add(roamAppAuthInfo);
                } else {
                    log.warn("未检索到人员：{}",idNo);
                    // 未查到
                    invalidIdNos.add(idNo);

                }
            });
            if (EmptyUtil.isEmpty(roamAppAuthInfos)) {
                throw new ErrorTipException("没有有效的人员需要授权");
            }
            if (log.isDebugEnabled()) {
                log.debug("权限导入请求参数：{}", JSON.toJSONString(roamAppAuthInfos));
            }
            String messageId = UUIDUtil.random();
            HttpUtil.ResponseWrap responseWrap = HttpUtil.createPost(userAppAuthUrl)
                    .addHeader("messageId", messageId)
                    .addHeader("appCredential", "1234")
                    .addJsonBody(roamAppAuthInfos)
                    .execute();
            if (responseWrap.statusCode() != 200) {
                log.error("调用统一授权进行授权失败，返回HTTP状态码异常：{}",responseWrap.statusCode());
                throw new ErrorTipException("调用统一授权进行授权失败，返回HTTP状态码异常");
            }
            String code = responseWrap.getResponseHeader("code");
            String message;
            try {
                message = URLDecoder.decode(responseWrap.getResponseHeader("message"),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                log.error("url decode error:{}",e.getMessage(),e);
                message = "";
            }
            log.debug("调用统一授权进行授权返回结果：code：{}，message：{}", code, message);
            if (!"0".equals(code)) {
                throw new ErrorTipException("调用统一授权进行授权失败，返回code异常："+code+"message："+message);
            }
        } else {
            throw new ErrorTipException("没有有效的人员需要授权");
        }
        return invalidIdNos;
    }
}
