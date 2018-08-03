package com.maoding.activiti.dto;

import com.maoding.core.base.dto.CoreShowDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/8/3
 * @description :
 */
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
