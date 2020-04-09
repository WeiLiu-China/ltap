package com.xdja.admin.bean;


import lombok.Data;

@Data
public class RoamAppAuthInfo {

    private String appId;
    private String appRegionalismCode;
    private String personId;
    private String personRegionalismCode;
    /**
     * 1-新增 2-修改 3-删除
     */
    private String type;
}
