package com.maoding.user.dto;

import com.maoding.core.base.dto.BaseDTO;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/20
 * @description :
 */
public class UserQueryDTO extends BaseDTO {
    /** 用户被分配的任务编号 */
    private String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
