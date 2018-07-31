package com.maoding.activiti.dto;


import com.maoding.core.base.dto.CoreEditDTO;

/**
 * 深圳市设计同道技术有限公司
 * @author : 张成亮
 * @date   : 2018/7/27
 * @package: FlowElementEditDTO
 * @description : 流程元素（包括任务节点、连接线、判断节点等）的编辑信息
 */
public class FlowElementEditDTO extends CoreEditDTO {
    /** id:流程内节点编号，例如：用户任务、序列、网关的编号 */

    /** 流程内节点元素名称 */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
