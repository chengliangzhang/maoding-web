package com.maoding.activiti.dto;

import com.maoding.core.base.dto.CoreQueryDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/13
 * 类名: com.maoding.activiti.dto.ProcessDefineQueryDTO
 * 作者: 张成亮
 * 描述: 流程查询条件
 **/
public class ProcessDefineQueryDTO extends CoreQueryDTO {
    /** id: 流程key */

    /** 可以启动流程的用户编号 */
    private String userId;

    /** 流程分类关键字，定义见ProcessTypeConst.PROCESS_TYPE_xxx */
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
