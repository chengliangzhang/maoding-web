package com.maoding.project.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chengliang.zhang on 2017/7/20.
 */
public class ImportResultDTO extends ImportProjectDefaultParamDTO {
    /** 总导入记录数 */
    Integer totalCount;
    /** 有效记录列表 */
    List<ImportProjectDTO> validList;
    /** 失败记录列表 */
    List<ImportProjectDTO> invalidList;

    public void addTotalCount(){
        if (totalCount == null) totalCount = 0;
        totalCount++;
    }

    public void addInvalid(ImportProjectDTO record){
        if (invalidList == null) invalidList = new ArrayList<>();
        invalidList.add(record);
    }

    public void addValid(ImportProjectDTO record){
        if (validList == null) validList = new ArrayList<>();
        validList.add(record);
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getInvalidCount() {
        return (invalidList != null) ? invalidList.size() : 0;
    }

    public Integer getValidCount() {
        return (validList != null) ? validList.size() : 0;
    }

    public List<ImportProjectDTO> getValidList() {
        return validList;
    }

    public void setValidList(List<ImportProjectDTO> validList) {
        this.validList = validList;
    }

    public List<ImportProjectDTO> getInvalidList() {
        return invalidList;
    }

    public void setInvalidList(List<ImportProjectDTO> invalidList) {
        this.invalidList = invalidList;
    }
}
