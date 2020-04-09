package com.xdja.admin.controller;


import com.xdja.admin.bean.AuthBean;
import com.xdja.admin.bean.RoamAppInfo;
import com.xdja.admin.entity.SbmaRegionalism;
import com.xdja.admin.service.SbmaRegionalismService;
import com.xdja.common.constants.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 行政区划表 前端控制器
 * </p>
 *
 * @author lw
 * @since 2020-04-09
 */
@RestController
@RequestMapping("/sbma-regionalism")
public class SbmaRegionalismController {

	@Autowired
	private SbmaRegionalismService sbmaRegionalismService;

	@PostMapping("/getRegionList")
	public Object getRegionList() {
		List<SbmaRegionalism> list = sbmaRegionalismService.list();
		return JsonResponse.success(list);
	}

	@PostMapping("/getApplyList")
	public Object getApplyList(@RequestBody SbmaRegionalism appRegiona) throws Exception {
		List<RoamAppInfo> roamAppListlist = sbmaRegionalismService.getRoamAppListlist(appRegiona.getCode());
		return JsonResponse.success(roamAppListlist);
	}

}
