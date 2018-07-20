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
    /** 用户参与的任务编号（包括参与了子任务） */
    private String parentTaskId;

    public String getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId;
    }
}
