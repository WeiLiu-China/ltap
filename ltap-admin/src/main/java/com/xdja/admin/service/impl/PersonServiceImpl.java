package com.xdja.admin.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdja.admin.entity.Person;
import com.xdja.admin.mapper.PersonMapper;
import com.xdja.admin.service.PersonService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 人员信息表 服务实现类
 * </p>
 *
 * @author lw
 * @since 2020-04-09
 */
@Service
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements PersonService {

}
