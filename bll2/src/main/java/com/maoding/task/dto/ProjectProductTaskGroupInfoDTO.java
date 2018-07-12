package com.maoding.task.dto;

import com.maoding.core.base.dto.BaseShowDTO;

import java.util.List;

/**
 * 深圳市卯丁技术有限公司
 *
 * @author : 张成亮
 * @date : 2018/7/12
 * @description :
 */
public class ProjectProductTaskGroupInfoDTO {
    /** 生产任务标签列表 */
    private List<BaseShowDTO> tabList;

    public List<BaseShowDTO> getTabList() {
        return tabList;
    }

    public void setTabList(List<BaseShowDTO> tabList) {
        this.tabList = tabList;
    }
}
