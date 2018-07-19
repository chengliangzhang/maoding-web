package com.maoding.project.dto;

import com.maoding.core.base.dto.BaseDTO;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/19
 * @description :
 */
public class ProjectSkyDriverQueryDTO extends BaseDTO {
    /** 相关交付任务编号 */
    private String deliverId;

    public String getDeliverId() {
        return deliverId;
    }

    public void setDeliverId(String deliverId) {
        this.deliverId = deliverId;
    }
}
