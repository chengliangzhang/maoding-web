package com.maoding.project.dto;

import com.maoding.core.base.dto.BaseDTO;

/**
 * 深圳市设计同道技术有限公司(2.0)
 * 类    名：ProjectDesignPeriodDTO
 * 类描述：设计阶段DTO
 * 作    者：MaoSF
 * 日    期：2016年12月12日-下午4:48:50
 */
public class ProjectDesignPeriodDTO extends BaseDTO {
    /**
     * 项目id
     */
    private String projectId;

    /**
     * 设计阶段名称
     */
    private String contentName;

    /**
     * 计划进度开始时间
     */
    private String planProgressStarttime;
    /**
     * 计划进度结束时间
     */
    private String planProgressEndtime;

    /**
     * 完成比例
     */
    private String completeRatio;


    /**
     * 排序字段
     */
    private String designContentSeq;

    /**
     * 1：正常完成，2：超期完成，3：正常进行，4：超期进行，5：无状态
     */
    private int designContentActualStatus;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }


    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }


    public String getPlanProgressStarttime() {
        return planProgressStarttime;
    }

    public void setPlanProgressStarttime(String planProgressStarttime) {
        this.planProgressStarttime = planProgressStarttime;
    }

    public String getPlanProgressEndtime() {
        return planProgressEndtime;
    }

    public void setPlanProgressEndtime(String planProgressEndtime) {
        this.planProgressEndtime = planProgressEndtime;
    }

    public String getDesignContentSeq() {
        return designContentSeq;
    }

    public void setDesignContentSeq(String designContentSeq) {
        this.designContentSeq = designContentSeq;
    }


    public String getCompleteRatio() {
        return completeRatio;
    }

    public void setCompleteRatio(String completeRatio) {
        this.completeRatio = completeRatio;
    }

    public int getDesignContentActualStatus() {
        return designContentActualStatus;
    }

    public void setDesignContentActualStatus(int designContentActualStatus) {
        this.designContentActualStatus = designContentActualStatus;
    }
}
