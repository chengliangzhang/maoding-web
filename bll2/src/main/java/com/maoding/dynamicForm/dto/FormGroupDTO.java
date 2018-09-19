package com.maoding.dynamicForm.dto;

import com.maoding.core.base.dto.CoreShowDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/14
 * 类名: com.maoding.dynamicForm.dto.FormGroupDTO
 * 作者: 张成亮
 * 描述: 表单群组信息
 **/
public class FormGroupDTO extends CoreShowDTO {

    /**所属公司编号**/
    private String companyId;

    /**动态表单群组类型**/
    private String typeId;

    /**可编辑标识：1，可编辑，0；不可编辑**/
    private Integer isEdit;

    /**审批类型群组名称**/
    private String groupName;

    public List<FormDTO> getFormList() {
        return formList;
    }

    /** 此组包含的自定义表单 */
    private List<FormDTO> formList;

    public void setFormList(List<FormDTO> formList) {
        this.formList = formList;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public Integer getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(Integer isEdit) {
        this.isEdit = isEdit;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
