package com.maoding.activiti.dto;

import com.maoding.core.base.dto.CoreShowDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/13
 * 类名: com.maoding.activiti.dto.ProcessDefineGroupDTO
 * 作者: 张成亮
 * 描述: 流程群组信息
 **/
public class ProcessDefineGroupDTO extends CoreShowDTO {
    /** 此组包含的流程定义 */
    private List<ProcessDefineDTO> processDefineList;

    public List<ProcessDefineDTO> getProcessDefineList() {
        return processDefineList;
    }

    public void setProcessDefineList(List<ProcessDefineDTO> processDefineList) {
        this.processDefineList = processDefineList;
    }

    public ProcessDefineGroupDTO(){}
    public ProcessDefineGroupDTO(String name,List<ProcessDefineDTO> processDefineList){
        setName(name);
        setProcessDefineList(processDefineList);
    }

}
