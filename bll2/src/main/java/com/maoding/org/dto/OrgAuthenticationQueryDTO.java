package com.maoding.org.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Chengliang.zhang on 2017/7/11.
 */
public class OrgAuthenticationQueryDTO {
    /**
     * 组织ID
     */
    String id;

    /**
     * 认证状态(0.否，1.是，2申请认证)
     */
    Integer authenticationStatus;

    /**
     * 有效期需在此日期之前
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    Date expiryDateBefore;

    /**
     * 组织法定名称过滤条件
     */
    String orgNameMask;
    /**
     * 组织在卯丁内定义名称的过滤条件
     */
    String orgAliasMask;

    /**
     * 分页时的页码
     */
    Integer pageIndex;
    /**
     * 分页查询时的页面大小
     */
    Integer pageSize;
    /**
     * 起始记录数
     */
    Integer startLine;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAuthenticationStatus() {
        return authenticationStatus;
    }

    public void setAuthenticationStatus(Integer authenticationStatus) {
        this.authenticationStatus = authenticationStatus;
    }

    public Date getExpiryDateBefore() {
        return expiryDateBefore;
    }

    public void setExpiryDateBefore(Date expiryDateBefore) {
        this.expiryDateBefore = expiryDateBefore;
    }

    public String getOrgNameMask() {
        return orgNameMask;
    }

    public void setOrgNameMask(String orgNameMask) {
        this.orgNameMask = orgNameMask;
    }

    public String getOrgAliasMask() {
        return orgAliasMask;
    }

    public void setOrgAliasMask(String orgAliasMask) {
        this.orgAliasMask = orgAliasMask;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
        if ((this.pageIndex != null) && (pageSize != null)){
            startLine = this.pageIndex * pageSize;
        }
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        if ((this.pageSize != null) && (pageIndex != null)){
            startLine = pageIndex * this.pageSize;
        }
    }

    public Integer getStartLine() {
        return startLine;
    }

    public void setStartLine(Integer startLine) {
        this.startLine = startLine;
        if ((this.startLine != null) && (pageSize != null)){
            pageIndex = (this.startLine/pageSize) + 1;
        }
    }
}
