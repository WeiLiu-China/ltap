package com.xdja.admin.service.impl;

import com.xdja.admin.bean.AuthBean;
import com.xdja.admin.entity.Person;
import com.xdja.admin.service.AuthService;
import com.xdja.admin.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class AuthServiceImpl implements AuthService {

    @Autowired
    private PersonService personService;

    @Override
    public List<String> authPower(AuthBean authBean) {
        String personIdNos = authBean.getPersonIdNos();
        List<String> idNos = Arrays.asList(personIdNos.split(","));
        Collection<Person> personList = personService.listByIds(idNos);
        List<String> effectiveIdNos = personList.stream().map(Person::getIdentifier).collect(Collectors.toList());
        // 筛选无效身份证号
        idNos.removeAll(effectiveIdNos);
        return idNos;

    }
}
