package com.maoding.process.dto;

import com.maoding.core.base.dto.CoreEditDTO;

/**
 * 深圳市卯丁技术有限公司
 * 日期: 2018/9/14
 * 类名: com.maoding.process.dto.ProcessGroupEditDTO
 * 作者: 张成亮
 * 描述: 群组编辑信息
 **/
public class ProcessGroupEditDTO extends CoreEditDTO {
    /** id:群组编号 **/

    /** 群组名称 **/
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
