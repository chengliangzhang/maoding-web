package com.maoding.task.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.project.dto.*;
import com.maoding.project.entity.ProjectConditionEntity;

import java.util.List;
import java.util.Map;

/**
 * 作    者 : DongLiu
 * 日    期 : 2018/1/25 17:20
 * 描    述 :
 */
public interface ProjectTaskConditionService extends BaseService<ProjectConditionEntity> {
    /**
     * 查询
     */
    List<ProjectConditionDTO> selProjectConditionList(Map<String, Object> param);

    /**
     * 新增
     * */
    int insertProjectCondition(Map<String, Object> param);

    /**
     * 描述     查询可选标题
     * 日期     2018/8/20
     * @author  张成亮
     * @return  可选择标题栏列表，两层列表，包含是否选中标志
     * @param   query 查询条件
     *                accountId 查询用户编号，默认为当前用户编号
     *                currentCompanyId 查询用户所在公司编号，默认为当前选择公司编号
     **/
    OptionalTitleSelectedDTO listOptionalTitle(TitleQueryDTO query);

    /**
     * 描述     保存标题设置
     * 日期     2018/8/23
     * @author  张成亮
     * @param   request 保存请求
     **/
    void changeOptionalTitle(TitleEditDTO request);

    /**
     * 描述       查询标题设置
     * 日期       2018/8/23
     * @author   张成亮
     **/
    List<TitleColumnDTO> listTitle(TitleQueryDTO query);
}
