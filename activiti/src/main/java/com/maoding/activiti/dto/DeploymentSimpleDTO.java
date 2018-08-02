package com.maoding.activiti.dto;

import com.maoding.core.base.dto.CoreShowDTO;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/8/2
 * @description :
 */
public class DeploymentSimpleDTO extends CoreShowDTO {
    /** id: 流程key */

    /** 流程说明 */
    private String documentation;
    /** 流程分类关键字，定义见ProcessTypeConst.PROCESS_TYPE_xxx */
    private String key;
    /** 流程类型，定义见ProcessTypeConst.TYPE_xxx */
    private Integer type;

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
