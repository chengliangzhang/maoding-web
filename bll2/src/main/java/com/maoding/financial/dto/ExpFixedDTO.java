package com.maoding.financial.dto;

import com.maoding.core.base.dto.BaseDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 深圳市设计同道技术有限公司
 * 类    名 : ExpFixedEntity
 * 描    述 : 固定支出DTO
 * 作    者 : LY
 * 日    期 : 2016/8/4 10:59
 */
public class ExpFixedDTO {

    private String id;

    /**
     * 支出类别
     */
    private String expType;

    /**
     * code
     */
    private String code;

    /**
     * 支出类别(父类别)
     */
    private String expTypeParentName;

    /**
     * 支出类别
     */
    private String expTypeName;

    /**
     * 金额
     */
    private BigDecimal expAmount;

    /**
     * 格式化之后的金额
     */
    private String expAmountFormat;

    private Integer seq;

    /**
     * 子项数据
     */
    private List<ExpFixedDTO> detailList = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getExpType() {
        return expType;
    }

    public void setExpType(String expType) {
        this.expType = expType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExpTypeName() {
        return expTypeName;
    }

    public void setExpTypeName(String expTypeName) {
        this.expTypeName = expTypeName;
    }

    public BigDecimal getExpAmount() {
        return expAmount;
    }

    public void setExpAmount(BigDecimal expAmount) {
        this.expAmount = expAmount;
    }

    public String getExpAmountFormat() {
        return expAmountFormat;
    }

    public void setExpAmountFormat(String expAmountFormat) {
        this.expAmountFormat = expAmountFormat;
    }

    public List<ExpFixedDTO> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<ExpFixedDTO> detailList) {
        this.detailList = detailList;
    }

    public String getExpTypeParentName() {
        return expTypeParentName;
    }

    public void setExpTypeParentName(String expTypeParentName) {
        this.expTypeParentName = expTypeParentName;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
}