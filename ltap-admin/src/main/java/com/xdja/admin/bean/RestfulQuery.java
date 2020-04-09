package com.xdja.admin.bean;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.UUID;

/**
 * 部里restful请求参数风格
 *
 * @author zk
 * @since 2019/3/26
 */
public class RestfulQuery<T> {
    /**
     * 请求id
     */
    private String messageId;
    /**
     * 请求version
     */
    private String version;
    /**
     * 请求参数
     */
    private T parameter;

    public static <T> RestfulQuery<T> getInstace(String param, Class<T> clazz) {

        JSONObject jsonObject = JSON.parseObject(param);
        RestfulQuery<T> restfulQuery = new RestfulQuery<>();
        restfulQuery.messageId = jsonObject.getString("messageId");
        restfulQuery.version = jsonObject.getString("version");
        restfulQuery.parameter = jsonObject.getJSONObject("parameter").toJavaObject(clazz);
        return restfulQuery;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public T getParameter() {
        return parameter;
    }

    public void setParameter(T parameter) {
        this.parameter = parameter;
    }

    public static <T> RestfulQuery<T> getRestfulQuery(T parameter) {
        RestfulQuery<T> restfulQuery = new RestfulQuery<>();
        restfulQuery.setVersion("1.0");
        restfulQuery.setMessageId(UUID.randomUUID().toString().replaceAll("-",""));
        restfulQuery.setParameter(parameter);
        return restfulQuery;
    }
}
