package com.xdja.admin.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xdja.admin.bean.*;
import com.xdja.admin.entity.SbmaRegionalism;
import com.xdja.admin.mapper.SbmaRegionalismMapper;
import com.xdja.admin.service.SbmaRegionalismService;
import com.xdja.common.utils.EffectiveHttp;
import com.xdja.framework.commons.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
@RefreshScope
public class SbmaRegionalismServiceImpl extends ServiceImpl<SbmaRegionalismMapper, SbmaRegionalism> implements SbmaRegionalismService {

	private static Integer pageNo = 1;
	private static Integer pageSize = 500;

	@Value("${sod.app.appList}")
	private String sod_app_appList;

	@Override
	public List<SbmaRegionalism> list() {
		return this.list(Wrappers.<SbmaRegionalism>lambdaQuery()
				.eq(SbmaRegionalism::getStatus, UN_BE_DELETED)
		);
	}

	@Override
	public List<RoamAppInfo> getRoamAppListlist(String appRegionalismCode) throws Exception {
		// 查询组装查询条件
		RestfulQuery<RoamAppSearchBean> restfulQuery = new RestfulQuery<>();
		RoamAppSearchBean searchBean = new RoamAppSearchBean();
		searchBean.setPageNo(pageNo);
		searchBean.setPageSize(pageSize);
		searchBean.setAppRegionalismCode(appRegionalismCode);
		String messageId = UUIDUtil.random();
		restfulQuery.setMessageId(messageId);
		restfulQuery.setVersion("1.0");
		restfulQuery.setParameter(searchBean);

		EffectiveHttp httpRequestUtil = new EffectiveHttp();
		String result = httpRequestUtil.httpPostJson(sod_app_appList, JSON.toJSONString(restfulQuery));
		log.debug("sod返回结果:{}", result);
		RestfulResult<RoamAppInfoResult> restfulResult = RestfulResult.getInstance(result, RoamAppInfoResult.class);

		Assert.notNull(restfulResult, "从sod获取异地应用结果为空");
		Assert.state(messageId.equals(restfulResult.getMessageId()), "sod返回的请求ID异常");
		Assert.state("200".equals(restfulResult.getCode()), String.format("sod返回状态异常:%s", restfulResult.getMessage()));

		RoamAppInfoResult roamAppInfoResult = restfulResult.getData();
		Assert.notNull(roamAppInfoResult, "mdp接口返回数据为空");
		return roamAppInfoResult.getData();
	}

}
