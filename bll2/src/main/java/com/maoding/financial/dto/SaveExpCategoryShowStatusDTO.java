package com.maoding.financial.dto;

import com.maoding.core.base.dto.BaseDTO;
import com.maoding.core.util.StringUtil;
import com.maoding.financial.entity.ExpCategoryRelationEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveExpCategoryShowStatusDTO extends BaseDTO {

    private List<String> idList = new ArrayList<>();

    private String ids;

    private String companyId;

    private Integer categoryType;

    private Integer payType;

    private Integer showStatus;

    private String relationCompanyId;

    private List<ExpCategoryRelationEntity> categoryTypeList = new ArrayList<>();

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Integer categoryType) {
        this.categoryType = categoryType;
    }

    public Integer getShowStatus() {
        return showStatus;
    }

    public void setShowStatus(Integer showStatus) {
        this.showStatus = showStatus;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getRelationCompanyId() {
        return relationCompanyId;
    }

    public void setRelationCompanyId(String relationCompanyId) {
        this.relationCompanyId = relationCompanyId;
    }

    public List<ExpCategoryRelationEntity> getCategoryTypeList() {
        categoryTypeList = new ArrayList<>();
        idList.stream().forEach(categoryTypeId->{
            ExpCategoryRelationEntity type = new ExpCategoryRelationEntity();
            type.initEntity();
            type.setCategoryTypeId(categoryTypeId);
            categoryTypeList.add(type);
        });
        return categoryTypeList;
    }

    public void setCategoryTypeList(List<ExpCategoryRelationEntity> categoryTypeList) {
        this.categoryTypeList = categoryTypeList;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }
}
