package com.xdja.admin.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdja.admin.entity.SbmaRegionalism;
import com.xdja.admin.mapper.SbmaRegionalismMapper;
import com.xdja.admin.service.SbmaRegionalismService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.xdja.common.Bean.Constants.UN_BE_DELETED;

/**
 * <p>
 * 行政区划表 服务实现类
 * </p>
 *
 * @author lw
 * @since 2020-04-09
 */
@Service
public class SbmaRegionalismServiceImpl extends ServiceImpl<SbmaRegionalismMapper, SbmaRegionalism> implements SbmaRegionalismService {

	@Override
	public List<SbmaRegionalism> list() {
		return this.list(Wrappers.<SbmaRegionalism>lambdaQuery()
				.eq(SbmaRegionalism::getStatus, UN_BE_DELETED)
		);
	}

}
