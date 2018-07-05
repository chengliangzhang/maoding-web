package com.maoding.financial.entity;

import com.maoding.core.base.entity.BaseEntity;

import java.math.BigDecimal;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ExpDetailEntity
 * 描    述 : 报销明细实体
 * 作    者 : LY
 * 日    期 : 2016/7/26 15:14
 */
public class ExpDetailEntity extends BaseEntity {
    /**
     * 支出类别父名称
     */
    private String expPName;

    /**
     * 支出类别子名称
     */
    private String expName;

    /**
     * 支出类别父子名称
     */
    private String expAllName;

    /**
     * 报销主单id
     */
    private String mainId;

    /**
     * 支出类别
     */
    private String expType;


    /**
     * 排序字段
     */
    private int seq;



    /**
     * 款项用途
     */
    private String expUse;

    /**
     * 款项金额
     */
    private BigDecimal expAmount;

    /**
     * 关联项目id
     */
    private String projectId;

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId == null ? null : mainId.trim();
    }

    public String getExpType() {
        return expType;
    }

    public void setExpType(String expType) {
        this.expType = expType == null ? null : expType.trim();
    }

    public String getExpUse() {
        return expUse;
    }

    public void setExpUse(String expUse) {
        this.expUse = expUse == null ? null : expUse.trim();
    }

    public BigDecimal getExpAmount() {
        return expAmount;
    }

    public void setExpAmount(BigDecimal expAmount) {
        this.expAmount = expAmount;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId == null ? null : projectId.trim();
    }

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

    public String getExpPName() {
        return expPName;
    }

    public void setExpPName(String expPName) {
        this.expPName = expPName;
    }

    public String getExpAllName() {
        return expAllName;
    }

    public void setExpAllName(String expAllName) {
        this.expAllName = expAllName;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}