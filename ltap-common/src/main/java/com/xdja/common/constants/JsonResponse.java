package com.xdja.common.constants;

import lombok.Data;

/**
 * JsonResponse
 *
 * @author jhm
 * @apiDefine Response
 * @apiSuccess {String} flag 0: fail, 1: success
 * @apiSuccess {String} message the error message when flag= 0, "成功" when flag= 1
 */
@Data
public class JsonResponse {

    private int flag;
    private String message;
    private Object result;

    private JsonResponse(int code, String message, Object result) {
        this.flag = code;
        this.message = message;
        this.result = result;
    }

    public static JsonResponse success(Object result) {
        return new JsonResponse(1, "成功", result);
    }

    public static JsonResponse failure(String message) {
        return new JsonResponse(0, message, null);
    }

    public int getFlag() {
        return flag;
    }

    public String getMessage() {
        return message;
    }

    public Object getResult() {
        return result;
    }
}
