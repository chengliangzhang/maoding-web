package com.maoding.project.service;

import com.maoding.core.base.dto.CoreQueryDTO;
import com.maoding.core.base.service.BaseService;
import com.maoding.project.dto.OptionalTitleGroupDTO;
import com.maoding.project.dto.ProjectConditionDTO;
import com.maoding.project.entity.ProjectConditionEntity;

import java.util.List;
import java.util.Map;

/**
 * 作    者 : DongLiu
 * 日    期 : 2018/1/25 17:20
 * 描    述 :
 */
public interface ProjectConditionService extends BaseService<ProjectConditionEntity> {
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
    List<OptionalTitleGroupDTO> listOptionalTitle(CoreQueryDTO query);
}
