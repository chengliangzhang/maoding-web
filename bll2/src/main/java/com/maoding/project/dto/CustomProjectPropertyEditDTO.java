package com.maoding.project.dto;

import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/8/14.
 * 编辑中的自定义项目属性
 */
public class CustomProjectPropertyEditDTO {
    /** 所属的公司id */
    String companyId;
    /** 所属的项目id */
    String projectId;
    /** 操作者id */
    String operatorId;
    /** 通用属性列表 */
    List<CustomProjectPropertyDTO> basicPropertyList;
    /** 公司自定义属性列表 */
    List<CustomProjectPropertyDTO> customPropertyList;
    /** 此项目中需要展示的属性列表 */
    List<CustomProjectPropertyDTO> selectedPropertyList;
    /** 单位列表 */
    List<String> unitNameList;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public List<CustomProjectPropertyDTO> getBasicPropertyList() {
        return basicPropertyList;
    }

    public void setBasicPropertyList(List<CustomProjectPropertyDTO> basicPropertyList) {
        this.basicPropertyList = basicPropertyList;
    }

    public List<CustomProjectPropertyDTO> getCustomPropertyList() {
        return customPropertyList;
    }

    public void setCustomPropertyList(List<CustomProjectPropertyDTO> customPropertyList) {
        this.customPropertyList = customPropertyList;
    }

    public List<CustomProjectPropertyDTO> getSelectedPropertyList() {
        return selectedPropertyList;
    }

    public void setSelectedPropertyList(List<CustomProjectPropertyDTO> selectedPropertyList) {
        this.selectedPropertyList = selectedPropertyList;
    }

    public List<String> getUnitNameList() {
        return unitNameList;
    }

    public void setUnitNameList(List<String> unitNameList) {
        this.unitNameList = unitNameList;
    }
}
