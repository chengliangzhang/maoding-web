package com.maoding.dynamicForm.dto;

import com.maoding.core.base.dto.CoreEditDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/18
 * 类名: com.maoding.dynamicForm.dto.FormEditDTO
 * 作者: 张成亮
 * 描述:
 **/
public class FormEditDTO extends CoreEditDTO {
    /** id:要编辑的动态表单模板编号，如果为空则编辑空白动态表单，否则加载指定表单信息并编辑 **/

    /** 表单名称 **/
    private String name;

    /** 表单说明 */
    private String documentation;

    /** 表单所属群组编号 */
    private Integer formType;

    /** 是否启用 **/
    private Integer isActive;

    /** 控件列表 **/
    private List<DynamicFormFieldDTO> fieldList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public Integer getFormType() {
        return formType;
    }

    public void setFormType(Integer formType) {
        this.formType = formType;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public List<DynamicFormFieldDTO> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<DynamicFormFieldDTO> fieldList) {
        this.fieldList = fieldList;
    }
}
