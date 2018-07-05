package com.maoding.task.dao;

import com.maoding.core.base.dao.BaseDao;
import com.maoding.task.dto.*;
import com.maoding.task.entity.ProjectTaskEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 深圳市设计同道技术有限公司
 * 类    名：ProjectTaskDao
 * 类描述：项目任务
 * 作    者：MaoSF
 * 日    期：2016年12月28日-上午10:13:28
 */
public interface ProjectTaskDao extends BaseDao<ProjectTaskEntity> {

    /**
     * 方法描述：完成自设计任务
     * 作者：ZhangChengliang
     * 日期：2018/3/29
     */
    void finishSubDesignTask(String taskId);
    
    /**
     * 方法描述：获取任务完整信息
     * 作者：ZhangChengliang
     * 日期：2017/7/6
     * @param id 任务编号，可以是设计内容、签发任务和生产任务
     * @return 任务的完整信息
     */
    TaskWithFullNameDTO getTaskByTaskId(String id);

    /**
     * 方法描述：根据taskPath设定节点的状态
     * 作者：MaoSF
     * 日期：2016/12/30
     * @param:
     * @return:
     */
    public int updateProjectTaskStatus(ProjectTaskEntity taskEntity);

    /**
     * 方法描述：查询项目任务的列表
     * 作者：TangY
     * 日期：2016/12/30
     * @param:
     * @return:
     */
    public List<ProjectTaskDTO> selectProjectTaskList(Map<String,Object> param);

    /**
     * 方法描述：修改任务状态
     * 作者：TangY
     * 日期：2016/12/30
     * @param:
     * @return:
     */
    public int updateTaskStatusByParam(Map<String,Object> param);


    /**
     * 方法描述：获取任务的父级的名称
     * 作者：MaoSF
     * 日期：2017/1/4
     *
     * @param:
     * @return:
     */
    public String getTaskParentName(String id);


    /**
     * 获取所有当前公司生产的根任务
     */
    String getProductRootTaskName(String projectId,String companyId);


    /**
     * 方法描述：查询子节点（此处全部为正式数据）
     * 作者：MaoSF
     * 日期：2017/3/12
     * @param:
     * @return:
     */
    List<ProjectTaskEntity> getProjectTaskByPid(String taskPid);

    /**
     * 获取子节点（包含未发布的数据）
     */
    List<ProjectTaskEntity> listTaskByPid(String taskPid);


    /**
     * 方法描述：根据项目id和公司id查询任务（用于经营分解时，判断是否存在已经签发给该公司的记录）
     * 作者：MaoSF
     * 日期：2017/2/27
     * @param:
     * @return:
     */
    List<ProjectTaskEntity> getProjectTaskByCompanyIdOfOperater(String projectId, String companyId);

    /**
     * 方法描述：根据taskPath查询所有的子任务
     * 作者：MaoSF
     * 日期：2017/1/4
     *
     * @param:
     * @return:
     */
    List<ProjectTaskEntity> getProjectTaskByTaskPath(String taskPath);

    /**
     * 方法描述：根据id查询
     * 作者：MaoSF
     * 日期：2017/1/4
     */
    ProjectTaskDataDTO getProjectTaskById(String id, String companyId);


    public int  updateByTaskId(ProjectTaskEntity projectTaskEntity);

    /**
     * 根据id和beModifyId更新seq的值
     */
    int updateByIdOrModifyId(ProjectTaskEntity projectTaskEntity);
    /**
     * 根据id和beModifyId更新seq的值
     */
    int updateById(ProjectTaskEntity projectTaskEntity);

    /**
     * 方法描述：把部门字段设置为null,用于更换任务的组织
     * 作者：MaoSF
     * 日期：2017/3/20
     * @param:
     * @return:
     */
    public int updateTaskOrgId(String id);

    /**
     * 方法描述：获取我是任务负责人的任务
     * 作者：MaoSF
     * 日期：2017/3/22
     * @param:map(projectId,companyUserId)
     * @return:
     */
    List<ProejctTaskForDesignerDTO> getMyProjectTask(Map<String,Object> map);

    /**
     * 方法描述：获取父级所以的companyId
     * 作者：MaoSF
     * 日期：2017/4/8
     * @param:
     * @return:
     */
    String getParentTaskCompanyId(Map<String,Object> map);

    /**
     * 方法描述：获取某项目内某承包方的任务
     * 作者：张成亮
     * 日期：2017/4/11
     */
    List<String> getProjectTaskOfToCompany(String projectId, String companyId);

    /**
     * 方法描述：根据taskPid,taskName获取（用于重名处理）
     * 作者：MaoSF
     * 日期：2017/4/21
     * @param:
     * @return:
     */
    ProjectTaskEntity getProjectTaskByPidAndTaskName(String projectId,String taskPid,String taskName);

    /**
     * 查询签发给某组织的所有任务名称（以逗号隔开）
     * 作者：wrb
     * 日期：2017/5/6
     * @param map{toCompanyId , fromCompanyId ,projectId }
     * @return
     */
    ProjectTaskEntity getAllTaskNameByToCompanyId(Map<String,Object> map);

    /**
     * 方法描述：经营板块的数据
     * 作者：MaoSF
     * 日期：2017/5/15
     */
    List<ProjectIssueTaskDTO> getOperatorTaskList(QueryProjectTaskDTO dto);

    /**
     * 方法描述：根据ID列表获取任务数据
     * 作者：ZCL
     * 日期：2017/5/16
     */
    List<ProjectTaskEntity> getTaskByIdList(List<String> idList);

    /**
     * 方法描述：根据taskPid获取任务ID及未发布的信息记录
     * 作者：MaoSF
     * 日期：2017/5/16
     * @param:
     * @return:
     */
    List<ProjectIssueTaskDTO> getTaskByTaskPidForChangeProcessTime(String taskPid);

    /**
     * 方法描述：获取生产总览列表
     * 作者：wrb
     * 日期：2017/5/17
     * @param dto
     * @return
     */
    List<ProjectProductTaskDTO> getProductTaskOverview(QueryProjectTaskDTO dto);

    /**
     * 新增获取成产安排列表
     * */
    List<ProjectProducttaskViewDTO> getProductTaskOverviewNew(QueryProjectTaskDTO dto);

    /**
     * 方法描述：获取生产总览列表
     * 作者：wrb
     * 日期：2017/5/18
     * @return
     */
    List<ProjectTaskDTO> getMyProjectProductTask(Map<String,Object> map);

    /**
     * 方法描述：生产界面数据
     * 作者：MaoSF
     * 日期：2017/5/18
     */
    List<ProjectDesignTaskShow> getProductTaskList(QueryProjectTaskDTO dto);

    /**
     * 方法描述：查找签发任务数据（版本1.0）
     * 作者：ZCL
     * 日期：2017/5/17
     */
    List<ProjectIssueTaskOverviewDTO> getProjectTaskIssueOverview(String projectId);

    /**
     * 方法描述：查找签发任务数据(版本2.0）
     * 作者：ZCL
     * 日期：2017/8/28
     */
    List<ProjectIssueTaskOverviewDTO> listProjectTaskIssueOverview(String projectId,String companyId);

    /**
     * 方法描述：根据参数查询
     * 作者：MaoSF
     * 日期：2017/5/16
     *
     * @param map
     */
    List<ProjectTaskEntity> selectByParam(Map<String, Object> map);

    /**
     * 作用：复位已完成状态
     * 作者：ZCL
     * 日期：2017-5-20
     */
    void resetTaskCompleteStatus(String taskId) throws Exception;
    void resetProcessNodeCompleteStatus(String processNodeId) throws Exception;

    /**
     * 作用：获取任务状态
     * 作者：ZCL
     * 日期：2017-5-24
     */
    Integer getTaskState(String taskId);
    String getStateText(Integer taskState,Date startTime,Date endTime,Date completeDate);
    String getStateText(Integer taskState,String startTime,String endTime,String completeDate);

    Map<String,Object> getTaskStateMap(Map<String,Object> map);
    /**
     * 方法描述：获取最大的seq
     * 作者：MaoSF
     * 日期：2017/6/9
     * @return:
     */
    int getProjectTaskMaxSeq(String projectId,String taskPid);

    /**
     * 方法描述：查找未完成的子任务
     * 作者：ZhangChengliang
     * 日期：2017/6/22
     */
    List<ProjectTaskEntity> listUnCompletedTask(String taskPid);
    /**
     * 方法描述：查找未完成的子任务(查询A-->B组织下的任务是否全部完成)
     */
    List<ProjectTaskEntity> listUnCompletedTaskByCompany(String projectId,String fromCompanyId,String toCompanyId);

    /**
     * 获取测试版本
     */
    List<ProjectTaskEntity> getTaskByBeModifyId(String beModifyId,String fromCompanyId);

    /**
     * 方法描述：发布当前记录后，修改该任务下的子集的taskPid，taskPath(重新设置为正式记录的taskPid)
     * param：publishId:被发布记录的ID，taskPid：新的taskPid,新的taskPath=parentPath+id
     * 作者：MaoSF
     * 日期：2017/6/23
     */
    int updateModifyTaskPid(String publishId,String taskPid,String parentPath);

    /**
     * 方法描述：获取设计内容（立项方（type=1）：设计阶段，合作方（type=2 and companyId），签发的数据）
     * 作者：MaoSF
     * 日期：2017/6/28
     */
    String getIssueTaskName(String projectId,String companyId,int type,String fromCompanyId);

    /**
     * 方法描述：获取我负责的任务
     * 作者：MaoSF
     * 日期：2017/6/30
     * @param:notComplete可以为null，其他不可以为null
     */
    List<ProjectTaskEntity> getMyResponsibleTask(String projectId,String companyId,String companyUserId,String notComplete);

    /**
     * 方法描述：获取任务下的所有生产子任务个数
     * 作者：ZhangChengliang
     * 日期：2017/6/30
     */
    Integer getSubProductTaskCountByTaskId(String taskPid);

    void changeCompany(TaskChangeCompanyDTO dto);
}