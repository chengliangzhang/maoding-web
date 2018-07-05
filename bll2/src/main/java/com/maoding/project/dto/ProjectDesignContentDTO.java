package com.maoding.project.dto;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.task.entity.ProjectProcessTimeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectDcontent
 * 类描述：设计阶段DTO
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午4:48:50
 */
public class ProjectDesignContentDTO extends BaseDTO {
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
     * 0 修改插入，1 删除
     */
    private String insertStatus;
    /**
     * 完成比例
     */
    private String completeRatio;

    /**
     * 是否有子任务（0，有，1没有）
     */
    private String isHas;
    /**
     * 排序字段
     */
    private String seq;

    /**
     *开始时间
     */
    private String startTime;

    /**
     *结束时间
     */
    private String endTime;

    /**
     * 变更记录（第一条记录为合同约定时间，第二条开始为合同约定变更记录）
     */
    private List<ProjectProcessTimeEntity> projectProcessTimeEntityList = new ArrayList<ProjectProcessTimeEntity>();





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

    public String getCompleteRatio() {
        return completeRatio;
    }

    public void setCompleteRatio(String completeRatio) {
        this.completeRatio = completeRatio;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public List<ProjectProcessTimeEntity> getProjectProcessTimeEntityList() {
        return projectProcessTimeEntityList;
    }

    public void setProjectProcessTimeEntityList(List<ProjectProcessTimeEntity> projectProcessTimeEntityList) {
        this.projectProcessTimeEntityList = projectProcessTimeEntityList;
    }

    public String getIsHas() {
        return isHas;
    }

    public void setIsHas(String isHas) {
        this.isHas = isHas;
    }

    public String getInsertStatus() {
        return insertStatus;
    }

    public void setInsertStatus(String insertStatus) {
        this.insertStatus = insertStatus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
