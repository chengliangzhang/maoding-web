package com.maoding.project.entity;

import com.maoding.core.base.entity.BaseEntity;
import com.maoding.core.util.StringUtil;

import java.math.BigDecimal;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectDcontentEntity
 * 类描述：设计阶段
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午4:11:50
 */
public class ProjectDesignContentEntity extends BaseEntity {
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 企业id
     */
    private String companyId;
    /**
     * 设计阶段id(数据字典)
     */
    private String contentId;
    /**
     * 设计阶段名称
     */
    private String contentName;
    /**
     * 设计管理费
     */
    private BigDecimal manageFee;
    /**
     * 到款确认日期
     */
    private String confirmDate;
    /**
     * 到款确认日期
     */
    private String paidConfirm;
    /**
     * 计划进度开始时间
     */
    private String planProgressStarttime;
    /**
     * 计划进度结束时间
     */
    private String planProgressEndtime;
    /**
     * 完成时间
     */
    private String progressDate;
    /**
     * 进度状态(0=未完成,1=完成)
     */
    private String progressStatus;
    /**
     * 是否审查(0.未审查，1.已审查)
     */
    private String isMap;
    /**
     * 审查实际时间
     */
    private String mapDate;
    /**
     * 审查勾选的系统时间
     */
    private String currentMapDate;
    /**
     * 排序字段
     */
    private String seq;
    /**
     * 备忘录
     */
    private String memo;
    /**
     * 合同进度开始时间
     */
    private String contractProgressStarttime;
    /**
     * 合同进度结束时间
     */
    private String contractProgressEndtime;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public BigDecimal getManageFee() {
        return manageFee;
    }

    public void setManageFee(BigDecimal manageFee) {
        this.manageFee = manageFee;
    }

    public String getConfirmDate() {
        if(StringUtil.isNullOrEmpty(confirmDate)){
            return null;
        }
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getPaidConfirm() {
        return paidConfirm;
    }

    public void setPaidConfirm(String paidConfirm) {
        this.paidConfirm = paidConfirm;
    }

    public String getPlanProgressStarttime() {
        /*if(StringUtil.isNullOrEmpty(planProgressStarttime)){
            return null;
        }*/
        return planProgressStarttime;
    }

    public void setPlanProgressStarttime(String planProgressStarttime) {
        this.planProgressStarttime = planProgressStarttime;
    }

    public String getPlanProgressEndtime() {

        /*if(StringUtil.isNullOrEmpty(planProgressEndtime)){
            return null;
        }*/
        return planProgressEndtime;
    }

    public void setPlanProgressEndtime(String planProgressEndtime) {
        this.planProgressEndtime = planProgressEndtime;
    }

    public String getProgressDate() {
        if(StringUtil.isNullOrEmpty(progressDate)){
            return null;
        }
        return progressDate;
    }

    public void setProgressDate(String progressDate) {
        this.progressDate = progressDate;
    }

    public String getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(String progressStatus) {
        this.progressStatus = progressStatus;
    }

    public String getIsMap() {
        return isMap;
    }

    public void setIsMap(String isMap) {
        this.isMap = isMap;
    }

    public String getMapDate() {
        if(StringUtil.isNullOrEmpty(mapDate)){
            return null;
        }
        return mapDate;
    }

    public void setMapDate(String mapDate) {
        this.mapDate = mapDate;
    }

    public String getCurrentMapDate() {
        if(StringUtil.isNullOrEmpty(currentMapDate)){
            return null;
        }
        return currentMapDate;
    }

    public void setCurrentMapDate(String currentMapDate) {
        this.currentMapDate = currentMapDate;
    }


    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getContractProgressStarttime() {
        return contractProgressStarttime;
    }

    public void setContractProgressStarttime(String contractProgressStarttime) {
        this.contractProgressStarttime = contractProgressStarttime;
    }

    public String getContractProgressEndtime() {
        return contractProgressEndtime;
    }

    public void setContractProgressEndtime(String contractProgressEndtime) {
        this.contractProgressEndtime = contractProgressEndtime;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }
}
