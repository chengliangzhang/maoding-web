package com.maoding.project.dao.impl;

import com.maoding.core.base.dao.GenericDao;
import com.maoding.project.dao.ProjectConditionDao;
import com.maoding.project.dto.OptionalTitleGroupDTO;
import com.maoding.project.dto.ProjectConditionDTO;
import com.maoding.project.dto.TitleColumnDTO;
import com.maoding.project.dto.TitleQueryDTO;
import com.maoding.project.entity.ProjectConditionEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 作    者 : DongLiu
 * 日    期 : 2018/1/25 17:20
 * 描    述 :
 */
@Service("projectConditionDao")
public class ProjectConditionDaoImpl extends GenericDao<ProjectConditionEntity> implements ProjectConditionDao {
    @Override
    public List<ProjectConditionDTO> selProjectConditionList(Map<String, Object> param) {
        return this.sqlSession.selectList("ProjectConditionEntityMapper.selProjectConditionList", param);
    }

    /**
     * 描述     查找可选标题栏常量信息
     * 日期     2018/8/23
     *
     * @param query
     * @author 张成亮
     */
    @Override
    public List<OptionalTitleGroupDTO> listOptionalTitleGroup(TitleQueryDTO query) {
        return sqlSession.selectList("ProjectConditionEntityMapper.listOptionalTitleGroup", query);
    }

    /**
     * 描述       查找显示的项目标题栏
     * 日期       2018/8/23
     *
     * @param query
     * @author 张成亮
     */
    @Override
    public List<TitleColumnDTO> listTitle(TitleQueryDTO query) {
        return sqlSession.selectList("ProjectConditionEntityMapper.listTitle", query);
    }
}
