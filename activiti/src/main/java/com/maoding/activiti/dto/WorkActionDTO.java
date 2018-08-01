package com.maoding.activiti.dto;


import com.maoding.core.base.dto.CoreEditDTO;

import java.util.Map;

/**
 * @author  张成亮
 * @date    2018/7/27
 * @description     工作流任务执行信息
 **/
public class WorkActionDTO extends CoreEditDTO {
    /** id: 启动流程时，是流程编号，完成工作流任务时，是工作流执行任务编号 */

    /** 流程名称，仅在启动流程时使用 */
    private String key;

    /** 是否通过 */
    private String isPass;

    /** 任务执行附加参数 */
    private Map<String,Object> resultMap;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIsPass() {
        return isPass;
    }

    public void setIsPass(String isPass) {
        this.isPass = isPass;
    }

    public Map<String, Object> getResultMap() {
        return resultMap;
    }

    public void setResultMap(Map<String, Object> resultMap) {
        this.resultMap = resultMap;
    }
}
