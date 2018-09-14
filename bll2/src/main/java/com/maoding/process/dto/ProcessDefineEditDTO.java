package com.maoding.process.dto;

import com.maoding.core.util.StringUtils;
import com.maoding.dynamicForm.dto.SaveDynamicFormDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/14
 * 类名: com.maoding.process.dto.ProcessDefineEditDTO
 * 作者: 张成亮
 * 描述: 流程更改信息
 *      由于原有审批管理的查询接口在ProcessService内实现，因此保留一个接口，保持一致性
 *      但根据目前的设计，审批信息使用动态表单表来存储，因此在动态表单服务内进行具体实现，并在动态表单内也提供相同功能的接口
 **/
public class ProcessDefineEditDTO extends SaveDynamicFormDTO {
    /** 要更改到的流程名称 **/
    private String name;

    public String getName() {
        return StringUtils.isEmpty(name) ? getFormName() : name;
    }

    public void setName(String name) {
        this.name = name;
        setFormName(name);
    }
}
