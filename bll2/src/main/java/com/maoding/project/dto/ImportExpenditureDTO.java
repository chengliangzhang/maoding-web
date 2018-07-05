package com.maoding.project.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 作    者 : DongLiu
 * 日    期 : 2018/2/26 14:43
 * 描    述 :
 */
public class ImportExpenditureDTO extends ImportProjectDefaultParamDTO {
    /**
     * 总导入记录数
     */
    Integer totalCount;
    /**
     * 有效记录列表
     */
    List<ExpenditureDTO> validList;
    /**
     * 失败记录列表
     */
    List<ExpenditureDTO> invalidList;

    public void addTotalCount() {
        if (totalCount == null) totalCount = 0;
        totalCount++;
    }

    public void addInvalid(ExpenditureDTO record) {
        if (invalidList == null) invalidList = new ArrayList<>();
        invalidList.add(record);
    }

    public void addValid(ExpenditureDTO record) {
        if (validList == null) validList = new ArrayList<>();
        validList.add(record);
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<ExpenditureDTO> getValidList() {
        return validList;
    }

    public void setValidList(List<ExpenditureDTO> validList) {
        this.validList = validList;
    }

    public List<ExpenditureDTO> getInvalidList() {
        return invalidList;
    }

    public void setInvalidList(List<ExpenditureDTO> invalidList) {
        this.invalidList = invalidList;
    }
}
