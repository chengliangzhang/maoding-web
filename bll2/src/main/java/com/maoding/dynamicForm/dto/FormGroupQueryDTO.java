package com.maoding.dynamicForm.dto;

import com.maoding.core.base.dto.CoreQueryDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/19
 * 类名: com.maoding.dynamicForm.dto.FormGroupQueryDTO
 * 作者: 张成亮
 * 描述: 动态表单群组查询条件
 **/
public class FormGroupQueryDTO extends CoreQueryDTO {
    /** id:群组编号 **/

    /** 是否包含表单 **/
    private Integer isIncludeForm;

    /** 表单是否启用:0-仅包含未启用表单，1-仅包含启用表单 **/
    private Integer status;

    /**isEdit ：1 = 可编辑的表单，0：不可编辑的表单  */
    private Integer isEdit;

    private String notIncludeGroupName;

    /** 是否需要抄送人员：0-不需要，1-需要 **/
    private Integer needCC;

    public Integer getNeedCC() {
        return needCC;
    }

    public void setNeedCC(Integer needCC) {
        this.needCC = needCC;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsIncludeForm() {
        return isIncludeForm;
    }

    public void setIsIncludeForm(Integer isIncludeForm) {
        this.isIncludeForm = isIncludeForm;
    }

    public Integer getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(Integer isEdit) {
        this.isEdit = isEdit;
    }

    public String getNotIncludeGroupName() {
        return notIncludeGroupName;
    }

    public void setNotIncludeGroupName(String notIncludeGroupName) {
        this.notIncludeGroupName = notIncludeGroupName;
    }
}
