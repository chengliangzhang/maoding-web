package com.maoding.project.service;

import com.maoding.commonModule.dto.ContentDTO;
import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.project.dto.*;
import com.maoding.project.entity.ProjectDesignContentEntity;
import com.maoding.project.entity.ProjectEntity;
import com.maoding.projectmember.entity.ProjectMemberEntity;
import com.maoding.system.dto.DataDictionaryDTO;
import com.maoding.task.dto.ProjectTaskDataDTO;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectService
 * 类描述：Service
 * 作    者：ChenZJ
 * 日    期：2016年7月19日-下午5:28:54
 */
public interface ProjectService extends BaseService<ProjectEntity> {


    /**
     * 方法描述：修改项目设计依据
     * 作        者：ChenZJ
     * 日        期：2016年7月20日-下午4:47:25
     */
    AjaxMessage saveProjectDesignBasic(ProjectDTO dto) throws Exception;

    /**
     * 方法描述：删除项目（逻辑删除）
     * 作者：MaoSF
     * 日期：2016/8/4
     */
    AjaxMessage deleteProjectById(String id, String currentCompanyUserId) throws Exception;

    /**
     * 方法描述：项目立项-界面展示数据
     * 作        者：ChenZJ
     * 日        期：2016年7月21日-上午10:42:43
     */
    ProjectShowDTO getProjectShow(Map<String, Object> map) throws Exception;

    /**
     * 方法描述：根据id查询项目信息
     * 作者：MaoSF
     * 日期：2016/7/28
     */
    ProjectDTO getProjectById(String id, String companyId, String userId) throws Exception;

    /**
     * 方法：根据id查询项目详细信息（带自定义字段）
     * 作者：Zhangchengliang
     * 日期：2017/8/15
     */
    ProjectDetailsDTO loadProjectDetails(String id, String companyId, String userId) throws Exception;

    /**
     * 方法：根据id查询项目自定义字段详细信息
     * 作者：Zhangchengliang
     * 日期：2017/8/17
     */
    CustomProjectPropertyEditDTO loadProjectCustomFields(ProjectCustomFieldQueryDTO query) throws Exception;

    List<ContentDTO> getProjectBuildType(List<String> projectIdList);

    /**
     * 方法：保存项目自定义字段详细信息，包括公司自定义属性
     * 作者：Zhangchengliang
     * 日期：2017/8/17
     */
    void saveProjectCustomFields(CustomProjectPropertyEditDTO properties) throws Exception;

    /**
     * 方法描述：根据companyId查询所有有效项目(我要报销 选择项目下拉框 )
     * 作   者：LY
     * 日   期：2016/8/8 14:38
     */
    List<ProjectDTO> getProjectListByCompanyId(ProjectDTO dto);


    //===============================2.0新接口===============================================

    /**
     * 方法描述：新增或修改项目
     * 作        者：TangY
     * 日        期：2016年7月20日-下午4:47:25
     *
     * @param dto
     * @return
     */
    AjaxMessage saveOrUpdateProjectNew(ProjectDTO dto) throws Exception;

    /**
     * 方述：建立新增项目的默认自定义属性
     * 作者：ZCL
     * 日期：2017/9/6
     *
     * @param project 要添加的项目对象
     */
    void createDefaultProjectProperty(ProjectEntity project);

    /**
     * 处理甲方
     */
    String handleProjectConstructName(String constructCompanyName, String companyId) throws Exception;

    /**
     * 方法描述：查询设计范围
     * 作        者：TangY
     * 日        期：2016年7月21日-上午10:42:43
     *
     * @return AjaxMessage
     */
    List<DataDictionaryDTO> getDesignRangeList() throws Exception;

    /**
     * 方法描述：查询设计阶段
     * 作        者：TangY
     * 日        期：2016年7月21日-上午10:42:43
     *
     * @return AjaxMessage
     */
    List<DataDictionaryDTO> getDesignContentList() throws Exception;

    /**
     * 方法描述：甲方查询 （按名称查询建筑团队）
     * 作        者：TangY
     * 日        期：2016年7月21日-上午10:42:43
     */
    List<ProjectConstructDTO> getProjectConstructListByName(Map<String, Object> map) throws Exception;

    /**
     * 方法描述：查询项目列表（进行中的项目）
     * 作者：TangY
     * 日期：2016/7/29
     */
    Map<String, Object> getProcessingProjectsByPage(Map<String, Object> param) throws Exception;

    /**
     * 描述     获取标题栏过滤列表
     * 日期     2018/8/21
     * @author  张成亮
     * @return  各个标题栏过滤列表
     * @param   param 查询条件
     **/
    Map<String, Object> getTitleFilter(Map<String, Object> param,Map<String, Object> condition) throws Exception;

    List<ProjectTableDTO> getProjectsByPage(QueryProjectDTO queryProjectDTO) throws Exception;

    /** 设置条件（暂时使用） */
    QueryProjectDTO getProjectParam(Map<String, Object> param) throws Exception;
    /**
     * 方法描述：获取当前公司的甲方
     * 作者：TangY
     * 日期：2016/7/29
     */
    List<ProjectConstructDTO> getProjectConstructList(String companyId) throws Exception;

    /**
     * 方法描述：获取本项目的设计阶段
     * 作者：TangY
     * 日期：2016/7/29
     */
    List<ProjectDesignContentEntity> getDesignContentListByProjectId(String projectId) throws Exception;

    /**
     * 方法描述：查询项目的设计范围
     * 作者：MaoSF
     * 日期：2016/7/28
     */
    List<ProjectDesignRangeDTO> getProjectDesignRangeById(String projectId) throws Exception;

    /**
     * 方法描述：获取项目基本信息完成个数
     * 作者：TangY
     * 日期：2016/7/29
     */
    Map<String, Object> getProjectBasicNum(ProjectDTO projectDTO) throws Exception;

    /**
     * 方法描述：删除动态
     * 作者：TangY
     * 日期：2016/7/29
     */
    int deleteProjectDynamics(String dynamicsId);

    /**
     * 方法描述：获取项目搜索条件
     * 作者：TangY
     * 日期：2016/7/29
     */
    AjaxMessage getSearchBaseData(Map<String, Object> param) throws Exception;

    /**
     * 方法描述：（新建项目，任务签发给其他组织）发送消息给相关组织成员
     * 作者：MaoSF
     * 日期：2017/2/20
     */
    void sendMsgToRelationCompanyUser(List<String> companyList);

    /**
     * 方法描述：获取建筑功能
     * 作者：MaoSF
     * 日期：2017/3/11
     */
    AjaxMessage getBuildType(String companyId) throws Exception;

    /**
     * 方法描述：获取项目菜单权限
     * 作者：MaoSF
     * 日期：2017/3/18
     */
    Map<String, Object> projectNavigationRole(String projectId, String companyId, String accountId, String companyUserId) throws Exception;

    /**
     * 方法描述：根据项目ID、组织ID判官当前组织最高权限的所有费控查看权限
     * 作者：wrb
     * 日期：2017/5/24
     */
    Map<String, Object> getCostRoleByCompanyId(String projectId, String companyId) throws Exception;

    /**
     * 方法描述：设置任务状态
     * 作者：MaoSF
     * 日期：2017/4/10
     */
    void setProjectTaskState(ProjectTaskDataDTO dataDTO) throws Exception;

    /**
     * 方法描述：设计阶段添加与修改
     * 作者：MaoSF
     * 日期：2017/4/20
     */
    AjaxMessage saveOrUpdateProjectDesign(ProjectDesignContentDTO designContentDTO) throws Exception;

    /**
     * 获取经营负责人姓名
     * 作者：ZCL
     * 日期：2017/4/25
     */
    String getManagerName(String projectId, String companyId) throws Exception;

    String getManagerName(ProjectMemberEntity projectManagerEntity, String companyId);

    /**
     * 获取设计负责人姓名
     * 作者：ZCL
     * 日期：2017/4/25
     */
    String getDesignerName(String projectId, String companyId) throws Exception;

    String getDesignerName(ProjectMemberEntity projectManagerEntity, String companyId);

    /**
     * 保存自定义字段的值
     * 作者：Zhangchengliang
     *
     * @param changedField 自定义字段值及更改者
     */
    void saveCustomProperty(ProjectFieldChangeDTO changedField);

    /**
     * 获取工时列表
     */
    List<ProjectWorkingHourTableDTO> getProjectWorking(ProjectWorkingHoursDTO hoursDTO);

    /**
     * 获取工时列表分页
     */
    Integer getProjectWorkingCount(ProjectWorkingHoursDTO hoursDTO);

    /**
     * 变更项目归属方
     */
    AjaxMessage changeSetUpCompany(ProjectDTO dto) throws Exception;

    /**
     * 是否具有项目编辑权限
     */
    boolean isEditProject(String projectId,String currentCompanyId,String account) throws Exception;

}
