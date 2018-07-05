package com.maoding.project.entity;

import com.maoding.core.base.entity.BaseEntity;
import com.maoding.core.util.StringUtil;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectDesignContentDetailEntity
 * 类描述：设计阶段详情
 * 作    者：MaoSF
 * 日    期：2016年7月19日-下午4:11:50
 */
public class ProjectDesignContentDetailEntity extends BaseEntity{

    /**
     * 设计阶段id
     */
    private String designContentId;

    /**
     * 合同计划开始时间
     */
    private String contractProgressStarttime;

    /**
     * 合同计划结束时间
     */
    private String contractProgressEndtime;
    /**
     * 备忘录
     */
    private String memo;

    /**
     * 排序
     */
    private int seq;

    public String getDesignContentId() {
        return designContentId;
    }

    public void setDesignContentId(String designContentId) {
        this.designContentId = designContentId == null ? null : designContentId.trim();
    }

    public String getContractProgressStarttime() {
        if(StringUtil.isNullOrEmpty(this.contractProgressStarttime)){
            return null;
        }
        return contractProgressStarttime;
    }

    public void setContractProgressStarttime(String contractProgressStarttime) {
        this.contractProgressStarttime = contractProgressStarttime;
    }

    public String getContractProgressEndtime() {
        if(StringUtil.isNullOrEmpty(this.contractProgressEndtime)){
            return null;
        }
        return contractProgressEndtime;
    }

    public void setContractProgressEndtime(String contractProgressEndtime) {
        this.contractProgressEndtime = contractProgressEndtime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}