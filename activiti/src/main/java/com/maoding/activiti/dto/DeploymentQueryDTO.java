package com.maoding.activiti.dto;

import com.maoding.core.base.dto.CoreQueryDTO;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/30
 * @description : 流程查询信息
 */
public class DeploymentQueryDTO extends CoreQueryDTO {
    /** id: 流程编号 */
    /** 可以启动流程的用户编号 */
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
