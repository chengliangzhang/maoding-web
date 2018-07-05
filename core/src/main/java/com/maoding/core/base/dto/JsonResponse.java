package com.maoding.core.base.dto;

import java.io.Serializable;

/**
 * Created by Xx on 2016/7/6.
 */
public class JsonResponse implements Serializable {

    private int code;

    private Object data;

    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static JsonResponse returnSuccess(Object data) {
        JsonResponse response = new JsonResponse();
        response.setCode(0);
        if (data != null)
            response.setData(data);
        return response;
    }

    public static JsonResponse returnSuccess(String msg) {
        JsonResponse response = new JsonResponse();
        response.setCode(0);
        response.setMsg(msg);
        return response;
    }

    public static JsonResponse returnFail(int code, String msg) {
        JsonResponse response = new JsonResponse();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }
}
