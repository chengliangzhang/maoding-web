package com.maoding.project.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.project.dto.OptionalTitleGroupDTO;
import com.maoding.project.dto.ProjectConditionDTO;
import com.maoding.project.dto.TitleColumnDTO;
import com.maoding.project.dto.TitleQueryDTO;
import com.maoding.project.entity.ProjectConditionEntity;

import java.util.List;
import java.util.Map;

/**
 * 作    者 : DongLiu
 * 日    期 : 2018/1/25 17:11
 * 描    述 :
 */
public interface ProjectConditionDao extends BaseDao<ProjectConditionEntity> {
    /**
     * 查询
     */
    List<ProjectConditionDTO> selProjectConditionList(Map<String, Object> param);

    /**
     * 描述     查找可选标题栏常量信息
     * 日期     2018/8/23
     * @author  张成亮
     **/
    List<OptionalTitleGroupDTO> listOptionalTitleGroup(TitleQueryDTO query);

    /**
     * 描述       查找显示的项目标题栏
     * 日期       2018/8/23
     * @author   张成亮
     **/
    List<TitleColumnDTO> listTitle(TitleQueryDTO query);
}
