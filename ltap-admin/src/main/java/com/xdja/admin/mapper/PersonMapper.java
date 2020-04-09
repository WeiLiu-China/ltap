package com.xdja.admin.mapper;

import com.xdja.admin.entity.Person;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 人员信息表 Mapper 接口
 * </p>
 *
 * @author lw
 * @since 2020-04-09
 */
@Mapper
public interface PersonMapper extends BaseMapper<Person> {

}
