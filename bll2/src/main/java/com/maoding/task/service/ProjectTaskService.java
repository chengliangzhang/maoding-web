package com.maoding.task.service;

import com.maoding.core.base.service.BaseService;
import com.maoding.core.bean.AjaxMessage;
import com.maoding.mytask.dto.HandleMyTaskDTO;
import com.maoding.task.dto.*;
import com.maoding.task.entity.ProjectTaskEntity;

import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectTaskService
 * 类描述：项目任务
 * 作    者：MaoSF
 * 日    期：2016年12月28日-下午5:28:54
 */
 public interface ProjectTaskService extends BaseService<ProjectTaskEntity>{

    /**
     * 生产查询任务列表
     * @param projectId
     */
     List<ProjectDesignTaskShow> getProjectDesignTaskShowList(String companyId,String projectId,String companyUserId) throws Exception;

    /**
     * 生产查询任务列表
     * @param projectId
     */
    List<ProjectDesignTaskShow> getProjectDesignTaskList(String companyId,String projectId,String companyUserId) throws Exception;

    /**
     * 生产任务界面数据
     */
    ProductTaskInfoDTO getProductTaskInfo(QueryProjectTaskDTO query) throws Exception;


    /**
     * 查询经营列表
     * @param companyId
     * @param projectId
     */
      List<ProjectDesignTaskShow> getOperatingTaskShowList(String companyId,String projectId,String companyUserId) throws Exception;

    /**
     * 方法描述：经营任务分解
     * 作者：CHENZHUJIE
     * 日期：2017/2/24
     */
     AjaxMessage saveProjectTask2(SaveProjectTaskDTO dto)throws Exception;


    /**
     * 方法描述：经营界面,生产界面（任务签发--组织关系数据）
     * 作者：MaoSF
     * 日期：2017/3/1
     */
     List<ProjectManagerDataDTO> getProjectTaskCoopateCompany(Map<String,Object> map) throws Exception;

    /**
     * 方法描述：经营界面,生产界面（任务签发--组织关系数据） 获取乙方信息
     * 作者：MaoSF
     * 日期：2017/3/1
     */
     ProjectManagerDataDTO getProjectTaskPartBCompany(Map<String,Object> map) throws Exception;

     AjaxMessage  updateByTaskId(ProjectTaskEntity projectTaskEntity);

    AjaxMessage  updateByTaskIdNew(ProjectTaskEntity projectTaskEntity) throws Exception;

    /**
     * 方法描述：修改项目状态
     * 作者：MaoSF
     * 日期：2017/6/21
     */
    AjaxMessage  updateByTaskIdStatus(ProjectTaskEntity projectTaskEntity,String taskStatus) throws Exception;
    AjaxMessage  updateByTaskIdStatus(String id,String taskStatus) throws Exception;

    /**
     * 方法描述：处理任务完成时间
     * 作者：MaoSF
     * 日期：2017/3/12
     */
     AjaxMessage handleProjectTaskCompleteDate(String projectId,String companyId,String accountId) throws Exception;

    /**
     * 方法描述：删除任务
     * 作者：MaoSF
     * 日期：2017/3/12
     */
     AjaxMessage deleteProjectTaskById(String id, String accountId,String currentCompanyId) throws Exception;

    /**
     * 方法描述：删除设计阶段的时候调用
     * 作者：MaoSF
     * 日期：2017/4/8
     */
     AjaxMessage deleteProjectTaskByIdForDesignContent(String id, String accountId,String currentCompanyId) throws Exception;

    /**
     * 方法描述:获取任务签发组织
     * 作者：MaoSF
     * 日期：2017/3/17
     */
     AjaxMessage getIssueTaskCompany(String id,String projectId,String companyId)throws Exception;


    /**
     * 方法描述：
     * 作者：MaoSF
     * 日期：2017/3/17
     * @param:map(projectId,toCompanyId(签发给这个组织的id)
     */
     AjaxMessage validateIssueTaskCompany(Map<String,Object> map) throws Exception;

    /**
     * 方法描述：移交设计负责人请求数据
     * 作者：MaoSF
     * 日期：2017/3/22
     */
     AjaxMessage getProjectTaskForChangeDesigner(Map<String,Object> map) throws Exception;

    /**
     * 获取签发任务列表
     */
    List<ProjectIssueTaskDTO> getProjectIssueTaskList(QueryProjectTaskDTO query) throws Exception;

    /**
     * 获取签发界面数据
     */
    IssueTaskInfoDTO getIssueInfo(QueryProjectTaskDTO query) throws Exception;

    /**
     * 进行签发
     */
    AjaxMessage publishIssueTask(List<String> idList, String currentCompanyUserId,String currentCompanyId) throws Exception;


    /**
     * 进行生产任务发布
     */
    AjaxMessage publishProductTask(List<String> idList, String currentCompanyUserId,String currentCompanyId) throws Exception;

    /**
     * 方法描述：获取生产总览列表
     * 作者：wrb
     * 日期：2017/5/17
     */
    List<ProjectProductTaskDTO> getProductTaskOverview(QueryProjectTaskDTO query) throws Exception;

    /**
     * 新增获取成产安排列表
     * */
    List<ProjectProducttaskViewDTO> getProductTaskOverviewNew(QueryProjectTaskDTO query) throws Exception;

    /**
     * 方法描述：获取项目立项人、经营负责人、项目负责人
     * 作者：wrb
     * 日期：2017/5/18
     */
    Map<String,Object> getProjectInfoForTask(String projectId) throws Exception;

    /**
     * 获取签发概览数据（版本2.0）
     */
    List<ProjectIssueTaskOverviewDTO> listProjectTaskIssueOverview(String projectId,String companyId) throws Exception;

    /**
     * 方法描述：完成生产任务
     * 作者：MaoSF
     * 日期：2017/3/12
     */
    AjaxMessage completeProductTask(HandleMyTaskDTO dto) throws Exception;

    /**
     * 方法描述：对象信息复制（用于数据记录更新的时候，不存在被修改的记录，则产生一条永不修改的记录数据）
     * 作者：MaoSF
     * 日期：2017/6/21
     */
    String copyProjectTask(SaveProjectTaskDTO dto, ProjectTaskEntity issuedEntity) throws Exception;

    /** 交换两个任务排序位置 */
    void exchangeTask(List<TaskSequencingDTO> taskList, String operatorId);

    /** 任务负责人确认完成任务 */
    AjaxMessage completeTask(HandleMyTaskDTO completeTaskDTO) throws Exception;

    /**
     * 项目归属组织变更，更变所有原来组织的任务的内容信息
     */
    void updateProjectTaskForChangeCompany(TaskChangeCompanyDTO dto) throws Exception;

    boolean isEditIssueTask(String projectId,String companyId,String accountId) throws Exception;

}
