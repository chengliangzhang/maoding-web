package com.maoding.core.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by Wuwq on 2016/12/14.
 */
public class ApiResult<T> implements Serializable {

    private String error;
    private String code;
    private String msg;
    private T data;

    public ApiResult() {

    }

    public ApiResult(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <E> ApiResult<E> success(String msg, E data) {
        ApiResultCode code = ApiResultCode.Success;
        return new ApiResult<>(code.getCode(), msg == null ? code.getComment() : msg, data);
    }

    public static <E> ApiResult<E> success(E data) {
        return success(null,data);
    }

    public static <E> ApiResult<E> failed(String msg, E data) {
        ApiResultCode code = ApiResultCode.Failed;
        return new ApiResult<>(code.getCode(), msg == null ? code.getComment() : msg, data);
    }

    public static <E> ApiResult<E> failed(String msg) {
        return failed(msg,null);
    }

    public static <E> ApiResult<E> error(String msg, E data) {
        ApiResultCode code = ApiResultCode.Error;
        return new ApiResult<>(code.getCode(), msg == null ? code.getComment() : msg, data);
    }

    public static <E> ApiResult<E> dataNotFound(String msg, E data) {
        ApiResultCode code = ApiResultCode.DataNotFound;
        return new ApiResult<>(code.getCode(), msg == null ? code.getComment() : msg, data);
    }

    public static <E> ApiResult<E> urlNotFound(String msg, E data) {
        ApiResultCode code = ApiResultCode.UrlNotFound;
        return new ApiResult<>(code.getCode(), msg == null ? code.getComment() : msg, data);
    }

    public static <E> ApiResult<E> noPermission(String msg, E data) {
        ApiResultCode code = ApiResultCode.NoPermission;
        return new ApiResult<>(code.getCode(), msg == null ? code.getComment() : msg, data);
    }

    @JsonIgnore
    public boolean isSuccessful() {
        return this.code.equalsIgnoreCase(ApiResultCode.Success.getCode());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        this.error = this.code;
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}