package com.maoding.project.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maoding.core.util.DateUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Chengliang.zhang on 2017/8/1.
 */
public class ImportDesignContentDTO {
    /**
     * 设计阶段名称
     */
    private String contentName;
    /**
     * 设计阶段开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;
    /**
     * 设计阶段结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;
    /**
     * 计划进度显示字符串
     */
    private String periodString;
    //完成时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date finishDate;
    //设计组织
    private String designOrganization;

    public ImportDesignContentDTO() {
    }

    public ImportDesignContentDTO(String contentName, Date startDate, Date endDate, Date finishDate, String designOrganization) {
        setContentName(contentName);
        setStartDate(startDate);
        setEndDate(endDate);
        setPeriodString(DateUtils.date2Str(startDate, DateUtils.date_sdf2) + " — " + DateUtils.date2Str(endDate, DateUtils.date_sdf2));
        setFinishDate(finishDate);
        setDesignOrganization(designOrganization);
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getPeriodString() {
        String s = periodString;
        if (s == null) {
            s = DateUtils.date2Str(startDate, DateUtils.date_sdf2) + " — " + DateUtils.date2Str(endDate, DateUtils.date_sdf2);
        }
        return periodString;
    }

    public void setPeriodString(String periodString) {
        this.periodString = periodString;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public String getDesignOrganization() {
        return designOrganization;
    }

    public void setDesignOrganization(String designOrganization) {
        this.designOrganization = designOrganization;
    }
}
