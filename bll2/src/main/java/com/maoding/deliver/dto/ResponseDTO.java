package com.maoding.deliver.dto;

import com.maoding.core.base.dto.BaseShowDTO;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/18
 * @description : 查询交付任务时返回列表内的负责人信息
 */
public class ResponseDTO extends BaseShowDTO {
    /** 是否已完成 */
    private String isFinished;

    public String getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(String isFinished) {
        this.isFinished = isFinished;
    }
}
