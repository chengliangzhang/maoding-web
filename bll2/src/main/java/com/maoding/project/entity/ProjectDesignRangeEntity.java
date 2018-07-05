package com.maoding.project.entity;

import com.maoding.core.base.entity.BaseEntity;
import com.maoding.core.util.StringUtil;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：DesignRangeEntity
 * 类描述：权限实体
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午3:46:50
 */
public class ProjectDesignRangeEntity extends BaseEntity {
    /**
     * 项目id
     */
    private String projectId;

    /**
     * 设计范围名称
     */
    private String designRange;

    /**
     * 设计范围选择日期
     */
    private String rangeDate;

    /**
     * 0:生效1，不生效
     */
    private String status;

    /**
     * 排序
     */
    private int seq;

    /**
     *范围类型（0:默认数据。1：自定义数据）
     */
    private int rangeType;
    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getDesignRange() {
        return designRange;
    }

    public void setDesignRange(String designRange) {
        this.designRange = designRange;
    }

    public String getRangeDate() {
        if(StringUtil.isNullOrEmpty(rangeDate)){
            return null;
        }
        return rangeDate;
    }

    public void setRangeDate(String rangeDate) {
        this.rangeDate = rangeDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getRangeType() {
        return rangeType;
    }

    public void setRangeType(int rangeType) {
        this.rangeType = rangeType;
    }
}
