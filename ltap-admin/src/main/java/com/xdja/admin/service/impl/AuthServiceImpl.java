package com.xdja.admin.service.impl;

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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
        String personIdNos = authBean.getPersonIdNos();
        List<String> idNos = Arrays.asList(personIdNos.split(","));
        Collection<Person> personList = personService.listByIds(idNos);
        // 有效身份证号
        List<String> effectiveIdNos = personList.stream().map(Person::getIdentifier).collect(Collectors.toList());
        List<RoamAppAuthInfo> roamAppAuthInfos = new ArrayList<>();
        String type = authBean.getType() == 1?"1":"3";
        if (EmptyUtil.isNotEmpty(personList)) {
            personList.forEach(person -> {
                RoamAppAuthInfo roamAppAuthInfo = new RoamAppAuthInfo();
                roamAppAuthInfo.setAppId(authBean.getAppId());
                roamAppAuthInfo.setAppRegionalismCode(authBean.getAppRegionalismCode());
                roamAppAuthInfo.setPersonId(person.getId());
                roamAppAuthInfo.setPersonRegionalismCode(localRegionalismCode);
                roamAppAuthInfo.setType(type);
                roamAppAuthInfos.add(roamAppAuthInfo);
            });
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
        // 筛选无效身份证号
        idNos.removeAll(effectiveIdNos);
        return idNos;
    }
}
