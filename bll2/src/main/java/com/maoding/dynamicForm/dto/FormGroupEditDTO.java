package com.maoding.dynamicForm.dto;

import com.maoding.core.base.dto.CoreEditDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/14
 * 类名: com.maoding.dynamicForm.dto.FormGroupEditDTO
 * 作者: 张成亮
 * 描述: 群组编辑信息
 **/
public class FormGroupEditDTO extends CoreEditDTO {
    /** id:群组编号 **/

    /** 目标群组名称 **/
    private String name;

    /* 需要更换排序的动态表单ID*/
    private String dynamicFromId1;

    /* 排序Seq*/
    private Integer seq;

    /* 需要更换排序的动态表单Seq*/
    private Integer seq1;

    /* 需要更换排序的动态表单ID*/
    private String dynamicFromId2;

    /* 需要更换排序的动态表单Seq*/
    private Integer seq2;

    /* 是否启用动态表单：0不启用，1启用*/
    private Integer isEdit;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDynamicFromId1() {
        return dynamicFromId1;
    }

    public void setDynamicFromId1(String dynamicFromId1) {
        this.dynamicFromId1 = dynamicFromId1;
    }

    public Integer getSeq1() {
        return seq1;
    }

    public void setSeq1(Integer seq1) {
        this.seq1 = seq1;
    }

    public String getDynamicFromId2() {
        return dynamicFromId2;
    }

    public void setDynamicFromId2(String dynamicFromId2) {
        this.dynamicFromId2 = dynamicFromId2;
    }

    public Integer getSeq2() {
        return seq2;
    }

    public void setSeq2(Integer seq2) {
        this.seq2 = seq2;
    }

    public Integer getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(Integer isEdit) {
        this.isEdit = isEdit;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
}
