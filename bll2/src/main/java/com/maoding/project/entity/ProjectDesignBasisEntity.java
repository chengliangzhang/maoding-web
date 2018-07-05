package com.maoding.project.entity;

import com.maoding.core.base.entity.BaseEntity;
import com.maoding.core.util.StringUtil;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：DesignBasisEntity
 * 类描述：权限实体
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午3:36:50
 */
public class ProjectDesignBasisEntity extends BaseEntity {
    /**
     * 项目id
     */
    private String projectId;

    /**
     * 设计依据名称
     */
    private String designBasis;

    /**
     * 设计依据选择日期
     */
    private String basisDate;

    /**
     * 0:生效，1：不生效
     */
    private String status;

    /**
     * 排序
     */
    private int seq;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getDesignBasis() {
        return designBasis;
    }

    public void setDesignBasis(String designBasis) {
        this.designBasis = designBasis;
    }

    public String getBasisDate() {
        if(StringUtil.isNullOrEmpty(basisDate)){
            return null;
        }
        return basisDate;
    }

    public void setBasisDate(String basisDate) {
        this.basisDate = basisDate;
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
}
