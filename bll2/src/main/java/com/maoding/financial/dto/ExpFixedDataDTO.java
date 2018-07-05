package com.maoding.financial.dto;

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
public class ExpFixedDataDTO {

    private String userId;

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
     * 父级支出类别
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

    private Integer payType;

    private Integer seq;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }
}