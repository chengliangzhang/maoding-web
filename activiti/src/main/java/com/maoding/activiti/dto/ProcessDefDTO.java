package com.maoding.activiti.dto;

import com.maoding.core.base.dto.CoreShowDTO;

public class ProcessDefDTO extends CoreShowDTO {

    /** 流程分类关键字，定义见ProcessTypeConst.PROCESS_TYPE_xxx */
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
