package com.xdja.admin.bean;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 部里定的restful result格式
 *
 * @author zk
 * @since 2019/3/26
 */
public class RestfulResult<T> {
    /**
     * 消息id
     */
    private String messageId;
    /**
     * 请求版本
     */
    private String version;
    /**
     * 返回响应值
     */
    private String code;
    /**
     * 提示消息
     */
    private String message;
    /**
     * 返回data
     */
    private T data;

    public static <T> RestfulResult<T> getInstance(String result, Class<T> clazz) {
        if (!StringUtils.hasText(result)) return null;
        JSONObject jsonObject = JSON.parseObject(result);
        JSONObject data = jsonObject.getJSONObject("data");
        T d = data == null ? null : data.toJavaObject(clazz);
        return create(jsonObject.getString("messageId"),
                jsonObject.getString("version"),
                jsonObject.getString("code"),
                jsonObject.getString("message"),
                d);
    }

    public static <T> RestfulResult<List<T>> getInstanceListDate(String result, Class<T> clazz) {
        if (!StringUtils.hasText(result)) return null;
        JSONObject jsonObject = JSON.parseObject(result);

        RestfulResult<List<T>> restfulResult = new RestfulResult<>();
        restfulResult.setCode(jsonObject.getString("code"));
        restfulResult.setMessage(jsonObject.getString("message"));
        restfulResult.setMessageId(jsonObject.getString("messageId"));
        restfulResult.setVersion(jsonObject.getString("version"));

        JSONArray arr = jsonObject.getJSONArray("data");

        if (arr == null) {
            restfulResult.setData(null);
        } else{
            restfulResult.setData(arr.toJavaList(clazz));
        }
        return restfulResult;
    }

    public static <T> RestfulResult<T> create(String messageId,String version,String code, String message, T data) {
        RestfulResult<T> restfulResult = new RestfulResult<>();
        restfulResult.setCode(code);
        restfulResult.setMessage(message);
        restfulResult.setMessageId(messageId);
        restfulResult.setVersion(version);
        restfulResult.setData(data);
        return restfulResult;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
