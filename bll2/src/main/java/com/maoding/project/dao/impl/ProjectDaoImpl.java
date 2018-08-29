package com.maoding.project.dao.impl;

import com.maoding.commonModule.dto.ContentDTO;
import com.maoding.core.base.dao.GenericDao;
import com.maoding.core.base.dto.CoreShowDTO;
import com.maoding.project.dao.ProjectDao;
import com.maoding.project.dto.*;
import com.maoding.project.entity.ProjectEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectDaoImpl
 * 类描述：projectDao
 * 作    者：ChenZJ
 * 日    期：2016年7月7日-上午5:21:29
 */
@Service("projectDao")
public class ProjectDaoImpl extends GenericDao<ProjectEntity> implements ProjectDao {


    /**
     * 方法描述：查询项目列表（进行中的项目）
     * 作者：TangY
     * 日期：2016/7/29
     */
    @Override
    public List<ProjectTableDTO> getProjectListByProcessing(QueryProjectDTO param) {
        return this.sqlSession.selectList("GetProjectEntityPageMapper.getProjectListByProcessing", param);
    }

    /**
     * 方法描述：查询项目列表（进行中的项目）
     * 作者：TangY
     * 日期：2016/7/29
     */
    @Override
    public int getProjectListByProcessingCount(QueryProjectDTO param) {
        return this.sqlSession.selectOne("GetProjectEntityPageMapper.getProjectListByProcessingCount", param);
    }

    @Override
    public int getProjectListByProcessingCount() {
        return this.sqlSession.selectOne("GetProjectEntityPageMapper.getProjectListByProcessingCountImmediately");
    }

    /**
     * 方法描述：根据companyId查询所有有效项目(我要报销 选择项目下拉框 )
     * 作   者：LY
     * 日   期：2016/8/8 14:38
     */
    @Override
    public List<ProjectDTO> getProjectListByCompanyId(ProjectDTO dto) {
        return this.sqlSession.selectList("GetProjectEntityPageMapper.getProjectListByCompanyId", dto);
    }

    /**
     * 方法描述：更改项目状态（删除项目使用）
     * 作者：MaoSF
     * 日期：2016/11/29
     */
    @Override
    public int updatePstatus(ProjectEntity entity) {
        return this.sqlSession.update("ProjectEntityMapper.updatePstatus", entity);
    }

    @Override
    public int updateCompanyId(ProjectEntity entity) {
        return this.sqlSession.update("ProjectEntityMapper.updateCompanyId", entity);
    }


    /**
     * 方法描述：获取全部的project
     * 作者：TangY
     * 日期：2016/7/29
     */
    @Override
    public List<ProjectEntity> selectAll() {
        return this.sqlSession.selectList("ProjectEntityMapper.selectAll");
    }

    /**
     * 方法描述：获取建筑功能
     * 作者：MaoSF
     * 日期：2017/3/11
     */
    @Override
    public List<Map<String, String>> getBuildType(Map<String, Object> map) {
        return this.sqlSession.selectList("GetProjectEntityPageMapper.getBuildType", map);
    }

    /**
     * 方法描述：获取建筑功能
     * 作者：MaoSF
     * 日期：2017/3/11
     */
    @Override
    public List<String> getProjectBuildType(String companyId) {
        return this.sqlSession.selectList("GetProjectEntityPageMapper.getProjectBuildType", companyId);
    }

    @Override
    public int update(ProjectEntity entity) {
        return this.sqlSession.update("ProjectEntityMapper.update", entity);
    }

    /**
     * 方法描述：获取项目名称
     * 作者：ZCL
     * 日期：2017-5-25
     *
     * @param id 项目编号
     */
    @Override
    public String getProjectName(String id) {
        ProjectEntity p = selectById(id);
        return (p != null) ? p.getProjectName() : null;
    }

    /**
     * 根据查询条件获取项目实体
     *
     * @param query
     */
    @Override
    public ProjectEntity getProject(ProjectQueryDTO query) {
        return this.sqlSession.selectOne("ProjectMapper.getProject", query);
    }

    /**
     * 根据id查询项目详细信息（带自定义字段）
     *
     * @param id 项目Id
     */
    @Override
    public ProjectDetailsDTO getProjectDetailsById(String id) {
        return this.sqlSession.selectOne("ProjectMapper.getProjectDetailsById", id);
    }

    @Override
    public String getEnterpriseName(String enterpriseOrgId) {
        return sqlSession.selectOne("EnterpriseMapper.getEnterpriseName", enterpriseOrgId);
    }

    @Override
    public String getEnterpriseNameByProjectId(String projectId) {
        return sqlSession.selectOne("EnterpriseMapper.getEnterpriseNameByProjectId", projectId);
    }

    @Override
    public List<ProjectWorkingHourTableDTO> getProjectWorking(ProjectWorkingHoursDTO hoursDTO) {
        return this.sqlSession.selectList("GetProjectEntityPageMapper.getProjectWorking", hoursDTO);
    }

    @Override
    public Integer getProjectWorkingCount(ProjectWorkingHoursDTO hoursDTO) {
        return this.sqlSession.selectOne("GetProjectEntityPageMapper.getProjectWorkingCount", hoursDTO);
    }

    /**
     * @param query 项目查询条件
     * @return 默认的功能分类列表
     * @description 获取默认的功能分类列表
     * @author 张成亮
     * @date 2018/6/25 17:50
     **/
    @Override
    public List<ContentDTO> listBuiltTypeConst(QueryProjectDTO query) {
        return this.sqlSession.selectList("ProjectMapper.listBuiltTypeConst", query);
    }

    /**
     * @param query 项目查询条件
     * @return 自定义的功能分类列表
     * @description 获取自定义的功能分类列表
     * @author 张成亮
     * @date 2018/6/25 17:53
     **/
    @Override
    public List<ContentDTO> listBuiltTypeCustom(QueryProjectDTO query) {
        return this.sqlSession.selectList("ProjectMapper.listBuiltTypeCustom", query);
    }

    @Override
    public List<ContentDTO> listBuildTypeByName(QueryProjectDTO query) {
        return this.sqlSession.selectList("ProjectMapper.listBuildTypeByName", query);
    }

    /**
     * 描述       查询项目
     * 日期       2018/8/10
     *
     * @param query
     * @author 张成亮
     */
    @Override
    public List<ProjectSimpleDTO> listProject(QueryProjectDTO query) {
        return sqlSession.selectList("ProjectMapper.listProject", query);
    }

    /**
     * 描述       查询项目基本信息标题栏过滤列表
     * 日期       2018/8/24
     *
     * @param query
     * @author 张成亮
     */
    @Override
    public List<CoreShowDTO> getProjectFilterList(ProjectFilterQueryDTO query) {
        return sqlSession.selectList("ProjectMapper.getProjectFilterList", query);
    }

    /**
     * 描述       查询项目列表
     * 日期       2018/8/24
     * @author   张成亮
     **/
    @Override
    public List<ProjectVariableDTO> listProjectBasic(ProjectQueryDTO query) {
        return sqlSession.selectList("ProjectMapper.listProjectBasic", query);
    }

    /**
     * 描述       查询项目个数
     * 日期       2018/8/29
     *
     * @param query
     * @author 张成亮
     */
    @Override
    public int countProject(ProjectQueryDTO query) {
        return sqlSession.selectOne("ProjectMapper.countProject", query);
    }

}


