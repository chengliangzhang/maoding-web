package com.maoding.core.bean;

/**
 * Created by Wuwq on 2016/12/14.
 */
public enum ApiResultCode {

    Success("0", "操作成功"),
    Failed("1", "操作失败"),
    Error("-1", "发生异常"),
    DataNotFound("-2", "找不到数据"),
    NoPermission("-3", "未授权"),
    UrlNotFound("404", "找不到URL");

    private String code;
    private String comment;

    ApiResultCode(String code, String comment) {
        this.code = code;
        this.comment = comment;
    }

    public String getCode() {
        return code;
    }

    public String getComment() {
        return comment;
    }
}
