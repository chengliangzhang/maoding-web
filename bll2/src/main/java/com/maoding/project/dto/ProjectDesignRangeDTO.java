package com.maoding.project.dto;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.util.BeanUtilsEx;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：DesignRangeDTO
 * 类描述：设计范围DTO
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午5:02:50
 */
public class ProjectDesignRangeDTO  extends BaseDTO {
    /**
     * 名称
     */
    private String designRange;

    /**
     * 排序
     */
    private int seq;

    public ProjectDesignRangeDTO(){}
    public ProjectDesignRangeDTO(Object obj){
        BeanUtilsEx.copyProperties(obj,this);
    }

    public String getDesignRange() {
        return designRange;
    }

    public void setDesignRange(String designRange) {
        this.designRange = designRange;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}
