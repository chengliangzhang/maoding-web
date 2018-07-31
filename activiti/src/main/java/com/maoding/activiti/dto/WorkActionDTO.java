package com.maoding.activiti.dto;


import com.maoding.core.base.dto.CoreEditDTO;

import java.util.Map;

/**
 * @author  张成亮
 * @date    2018/7/27
 * @description     工作流任务执行信息
 **/
public class WorkActionDTO extends CoreEditDTO {
    /** 工作流任务执行结果 */
    private Map<String,Object> resultMap;

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }
}
