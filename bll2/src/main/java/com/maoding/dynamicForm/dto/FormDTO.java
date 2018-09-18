package com.maoding.dynamicForm.dto;

import com.maoding.core.base.dto.BaseIdObject;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/13
 * 类名: com.maoding.dynamicForm.dto.FormDTO
 * 作者: 张成亮
 * 描述: 动态表单的属性及控件信息
 **/
public class FormDTO extends BaseIdObject {
    /** id:动态表单模板编号 **/

    /** 表单名称 **/
    private String name;

    /** 表单说明 */
    private String documentation;

    /** 流程分类关键字，定义见ProcessTypeConst.PROCESS_TYPE_xxx */
    private String formCode;

    /** 流程类型，定义见ProcessTypeConst.TYPE_xxx */
    private Integer formType;

    /** 分条件流程变量名称 **/
    private String varName;

    /** 分条件流程变量单位 **/
    private String varUnit;

    /** 是否启用 **/
    private Integer isActive;

    /** 是否系统表单 **/
    private Integer isSystem;

    /** 控件列表 **/
    private List<DynamicFormFieldDTO> fieldList;

    /** 编辑时可使用的控件列表 **/
    private List<DynamicFormFieldDTO> optionalFieldList;

    public List<DynamicFormFieldDTO> getOptionalFieldList() {
        return optionalFieldList;
    }

    public void setOptionalFieldList(List<DynamicFormFieldDTO> optionalFieldList) {
        this.optionalFieldList = optionalFieldList;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public Integer getFormType() {
        return formType;
    }

    public void setFormType(Integer formType) {
        this.formType = formType;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getVarUnit() {
        return varUnit;
    }

    public void setVarUnit(String varUnit) {
        this.varUnit = varUnit;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Integer isSystem) {
        this.isSystem = isSystem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DynamicFormFieldDTO> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<DynamicFormFieldDTO> fieldList) {
        this.fieldList = fieldList;
    }
}
