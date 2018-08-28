package com.maoding.project.dao;

import com.maoding.commonModule.dto.ContentDTO;
import com.maoding.core.base.dao.BaseDao;
import com.maoding.core.base.dto.CoreShowDTO;
import com.maoding.project.dto.*;
import com.maoding.project.entity.ProjectEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectDao
 * 类描述：项目（dao）
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午5:20:47
 */
public interface ProjectDao extends BaseDao<ProjectEntity> {


    /**
     * 方法描述：根据companyId查询所有有效项目(我要报销 选择项目下拉框 )
     * 作   者：LY
     * 日   期：2016/8/8 14:38
     */
    List<ProjectDTO> getProjectListByCompanyId(ProjectDTO dto);


    /**
     * 方法描述：更改项目状态（删除项目使用）
     * 作者：MaoSF
     * 日期：2016/11/29
     */
    int updatePstatus(ProjectEntity entity);

    int updateCompanyId(ProjectEntity entity);
  //============================================新接口===============================================================================

    /**
     * 方法描述：查询项目列表（进行中的项目）
     * 作者：TangY
     * 日期：2016/7/29
     */
    List<ProjectTableDTO> getProjectListByProcessing(QueryProjectDTO param);


    /**
     * 方法描述：查询项目数量（进行中的项目）
     * 作者：TangY
     * 日期：2016/7/29
     */
    int getProjectListByProcessingCount(QueryProjectDTO param);
    int getProjectListByProcessingCount();

    /**
     * 方法描述：获取全部的project
     * 作者：TangY
     * 日期：2016/7/29
     */
    List<ProjectEntity> selectAll();

    /**
     * 方法描述：获取建筑功能
     * 作者：MaoSF
     * 日期：2017/3/11
     */
    List<Map<String,String>> getBuildType(Map<String,Object> map);

    /**
     * 方法描述：获取建筑功能
     * 作者：MaoSF
     * 日期：2017/3/11
     */
    List<String> getProjectBuildType(String companyId);


    int update(ProjectEntity entity);

    /**
     * 方法描述：获取项目名称
     * 作者：ZCL
     * 日期：2017-5-25
     */
    String getProjectName(String id);

    /** 根据查询条件获取项目实体 */
    ProjectEntity getProject(ProjectQueryDTO query);

    /** 根据id查询项目详细信息（带自定义字段）*/
    ProjectDetailsDTO getProjectDetailsById(String id);

    /**
     * 获取甲方名字
     */
    String getEnterpriseName(String enterpriseOrgId);

    /**
     * 获取甲方名字
     */
    String getEnterpriseNameByProjectId(String projectId);

    /**
     * 获取工时列表
     */
    List<ProjectWorkingHourTableDTO> getProjectWorking(ProjectWorkingHoursDTO hoursDTO);

    /**
     * 获取工时列表分页
     */
    Integer getProjectWorkingCount(ProjectWorkingHoursDTO hoursDTO);

    /**
     * @description 获取默认的功能分类列表
     * @author  张成亮
     * @date    2018/6/25 17:50
     * @param   query 项目查询条件
     * @return  默认的功能分类列表
     **/
    List<ContentDTO> listBuiltTypeConst(QueryProjectDTO query);

    /**
     * @description 获取自定义的功能分类列表
     * @author  张成亮
     * @date    2018/6/25 17:53
     * @param   query 项目查询条件
     * @return  自定义的功能分类列表
     **/
    List<ContentDTO> listBuiltTypeCustom(QueryProjectDTO query);

    /**
     * 根据名字查询类型，无需去重，供项目列表查询
     */
    List<ContentDTO> listBuildTypeByName(QueryProjectDTO query);
    
    /**
     * 描述       查询项目
     * 日期       2018/8/10
     * @author   张成亮
     **/
    List<ProjectSimpleDTO> listProject(QueryProjectDTO query);

    /**
     * 获取记录总数
     */
    int getLastQueryCount();

    /**
     * 描述       查询项目基本信息标题栏过滤列表
     * 日期       2018/8/24
     * @author   张成亮
     **/
    List<CoreShowDTO> getProjectFilterList(ProjectFilterQueryDTO query);

    /**
     * 描述       通过项目基本信息查询项目列表
     * 日期       2018/8/24
     * @author   张成亮
     **/
    List<ProjectVariableDTO> listProjectBasic(ProjectQueryDTO query);

    /**
     * 描述       通过项目功能分类信息查询项目列表
     * 日期       2018/8/24
     * @author   张成亮
     **/
    List<ProjectVariableDTO> listProjectBuildType(ProjectQueryDTO query);

    /**
     * 描述       通过项目参与人信息查询项目列表
     * 日期       2018/8/24
     * @author   张成亮
     **/
    List<ProjectVariableDTO> listProjectMember(ProjectQueryDTO query);

    /**
     * 描述       通过项目合作组织信息查询项目列表
     * 日期       2018/8/24
     * @author   张成亮
     **/
    List<ProjectVariableDTO> listProjectRelation(ProjectQueryDTO query);
}
