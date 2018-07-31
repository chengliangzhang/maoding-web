package com.maoding.activiti.dto;

import com.maoding.core.base.dto.CoreQueryDTO;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/30
 * @description : 流程元素查询器
 */
public class FlowElementQueryDTO extends CoreQueryDTO {
    /** id:流程内节点编号，例如：用户任务、序列、网关的编号 */

    /** 所处的流程编号 */
    private String deploymentId;
    /** 上一个元素的编号 */
    private String prevElementId;
    /** 下一个元素的编号 */
    private String nextElementId;


    public String getPrevElementId() {
        return prevElementId;
    }

    public void setPrevElementId(String prevElementId) {
        this.prevElementId = prevElementId;
    }

    public String getNextElementId() {
        return nextElementId;
    }

    public void setNextElementId(String nextElementId) {
        this.nextElementId = nextElementId;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }
}
