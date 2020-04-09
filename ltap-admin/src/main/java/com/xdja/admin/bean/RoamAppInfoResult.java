package com.xdja.admin.bean;

import lombok.Data;

import java.util.List;

@Data
public class RoamAppInfoResult {

    private Integer count;

    private Integer totalPage;

    private List<RoamAppInfo> data;


}
