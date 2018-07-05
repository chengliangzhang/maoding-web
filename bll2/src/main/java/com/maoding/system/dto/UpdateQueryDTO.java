package com.maoding.system.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.base.dto.BaseQueryDTO;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Chengliang.zhang on 2017/8/4.
 */
public class UpdateQueryDTO extends BaseQueryDTO {
    /** 要查询的平台 */
    String platform;

    /** 要查询的时间段在此字段之前 */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    Date createDateBefore;

    /** 要查询的时间段在此字段之后 */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    Date createDateAfter;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Date getCreateDateBefore() {
        return createDateBefore;
    }

    public void setCreateDateBefore(Date createDateBefore) {
        this.createDateBefore = createDateBefore;
    }

    public Date getCreateDateAfter() {
        return createDateAfter;
    }

    public void setCreateDateAfter(Date createDateAfter) {
        this.createDateAfter = createDateAfter;
    }
}
