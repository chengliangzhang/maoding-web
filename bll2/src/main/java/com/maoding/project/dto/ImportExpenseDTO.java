package com.maoding.project.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 作    者 : DongLiu
 * 日    期 : 2018/2/26 11:40
 * 描    述 :
 */
public class ImportExpenseDTO extends ImportProjectDefaultParamDTO {
    /**
     * 总导入记录数
     */
    Integer totalCount;
    /**
     * 有效记录列表
     */
    List<ExpenseDTO> validList;
    /**
     * 失败记录列表
     */
    List<ExpenseDTO> invalidList;

    public void addTotalCount() {
        if (totalCount == null) totalCount = 0;
        totalCount++;
    }

    public void addInvalid(ExpenseDTO record) {
        if (invalidList == null) invalidList = new ArrayList<>();
        invalidList.add(record);
    }

    public void addValid(ExpenseDTO record) {
        if (validList == null) validList = new ArrayList<>();
        validList.add(record);
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<ExpenseDTO> getValidList() {
        return validList;
    }

    public void setValidList(List<ExpenseDTO> validList) {
        this.validList = validList;
    }

    public List<ExpenseDTO> getInvalidList() {
        return invalidList;
    }

    public void setInvalidList(List<ExpenseDTO> invalidList) {
        this.invalidList = invalidList;
    }
}
