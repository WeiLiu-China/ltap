package com.xdja.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xdja.admin.bean.RoamAppInfo;
import com.xdja.admin.entity.SbmaRegionalism;

import java.util.List;

/**
 * <p>
 * 行政区划表 服务类
 * </p>
 *
 * @author lw
 * @since 2020-04-09
 */
public interface SbmaRegionalismService extends IService<SbmaRegionalism> {
	List<SbmaRegionalism> list();

	List<RoamAppInfo> getRoamAppListlist(String appRegionalismCode) throws Exception;
}
