package com.maoding.dynamicForm.dto;

import com.maoding.commonModule.dto.AuditCopyDataDTO;
import com.maoding.core.base.dto.BaseIdObject;

import java.util.ArrayList;
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

    /** 表单编号 **/
    private String formId;

    /** 流程类型：见ProcessTypeConst内 **/
    private Integer type;

    /** 表单名称 **/
    private String name;

    /** 表单说明 */
    private String documentation;

    /** 流程类型，定义见ProcessTypeConst.TYPE_xxx */
    private String formType;

    /** 分条件流程变量名称 **/
    private String varName;

    /** 分条件流程变量单位 **/
    private String varUnit;

    /** 是否启用 **/
    private Integer status;

    /** 是否系统表单 **/
    private Integer isSystem;

    /** 图标关键字 **/
    private String iconKey;

    /** 控件列表 **/
    private List<DynamicFormFieldDTO> fieldList;

    List<AuditCopyDataDTO> copyList = new ArrayList<>();

    public String getIconKey() {
        return iconKey;
    }

    public void setIconKey(String iconKey) {
        this.iconKey = iconKey;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public List<AuditCopyDataDTO> getCopyList() {
        return copyList;
    }

    public void setCopyList(List<AuditCopyDataDTO> copyList) {
        this.copyList = copyList;
    }
}
