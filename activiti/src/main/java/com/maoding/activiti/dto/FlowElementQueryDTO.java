package com.maoding.activiti.dto;

import com.maoding.core.base.dto.CoreQueryDTO;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/30
 * @description : 流程元素查询器
 */
public class FlowElementQueryDTO<T extends FlowElementDTO> extends CoreQueryDTO<T> {
    /** 所处的流程 */
    private DeploymentDTO deployment;

    public DeploymentDTO getDeployment() {
        return deployment;
    }

    public void setDeployment(DeploymentDTO deployment) {
        this.deployment = deployment;
    }

}
