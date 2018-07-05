package com.maoding.project.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 作    者 : DongLiu
 * 日    期 : 2018/2/26 10:34
 * 描    述 :
 */
public class ImportExpFixedDTO extends ImportProjectDefaultParamDTO {
    /**
     * 总导入记录数
     */
    Integer totalCount;
    /**
     * 有效记录列表
     */
    List<ExpFixedDTO> validList;
    /**
     * 失败记录列表
     */
    List<ExpFixedDTO> invalidList;

    public void addTotalCount() {
        if (totalCount == null) totalCount = 0;
        totalCount++;
    }

    public void addInvalid(ExpFixedDTO record) {
        if (invalidList == null) invalidList = new ArrayList<>();
        invalidList.add(record);
    }

    public void addValid(ExpFixedDTO record) {
        if (validList == null) validList = new ArrayList<>();
        validList.add(record);
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<ExpFixedDTO> getValidList() {
        return validList;
    }

    public void setValidList(List<ExpFixedDTO> validList) {
        this.validList = validList;
    }

    public List<ExpFixedDTO> getInvalidList() {
        return invalidList;
    }

    public void setInvalidList(List<ExpFixedDTO> invalidList) {
        this.invalidList = invalidList;
    }

    public Integer getValidCount() {
        return (validList != null) ? validList.size() : 0;
    }
}
