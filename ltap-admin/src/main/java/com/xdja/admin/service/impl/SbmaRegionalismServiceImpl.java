package com.xdja.admin.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdja.admin.bean.RoamAppList;
import com.xdja.admin.bean.SynRst;
import com.xdja.admin.entity.SbmaRegionalism;
import com.xdja.admin.mapper.SbmaRegionalismMapper;
import com.xdja.admin.service.SbmaRegionalismService;
import com.xdja.common.utils.EffectiveHttp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
@Slf4j
public class SbmaRegionalismServiceImpl extends ServiceImpl<SbmaRegionalismMapper, SbmaRegionalism> implements SbmaRegionalismService {

	@Value("${sod.app.appList}")
	private String sod_app_appList;

	@Override
	public List<SbmaRegionalism> list() {
		return this.list(Wrappers.<SbmaRegionalism>lambdaQuery()
				.eq(SbmaRegionalism::getStatus, UN_BE_DELETED)
		);
	}

	@Override
	public List<RoamAppList> getRoamAppListlist(String appRegionalismCode) throws Exception {
		Integer pageNo = 1;
		Integer pageSize = 500;
		EffectiveHttp.ResponseWrap responseWrap = EffectiveHttp.createGet(sod_app_appList)
				.addParameter("appRegionalismCode", "")
				.addParameter("appName", "")
				.addParameter("pageNo", pageNo + "")
				.addParameter("pageSize", pageSize + "")
				.execute();

		String result = responseWrap.getString();

		if (StringUtils.isEmpty(result)) {
			throw new Exception("调用sod接口出错，返回内容为空");
		}
		//解析数据
		SynRst<RoamAppList> synRst = JSON.parseObject(result, new TypeReference<SynRst<RoamAppList>>() {
		});
		SynRst.Result synResult = synRst.getResult();
		if ("1".equals(synResult.getFlag())) {
			log.error("同步单位出错：" + synResult.getMessage());
			throw new Exception(synResult.getMessage());
		}
		List<RoamAppList> list = synRst.getList();
		return list;
	}

}
